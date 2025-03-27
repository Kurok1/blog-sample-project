package org.example.order.service;

import com.example.messages.CreateOrderMessage;
import com.example.messages.OrderStockCallback;
import org.example.order.entity.Order;
import org.example.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:khanc.dev@gmail.com">韩超</a>
 * @since 1.0
 */
@Service
public class OrderService {


    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private RestTemplate restTemplate = new RestTemplate();

    public Order findByCode(String code) {
        return this.orderRepository.findByCode(code).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(String code, String itemName, BigDecimal qty) {
        if (qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }
        Order order = new Order();

        order.setCode(code);
        order.setItemName(itemName);
        order.setQty(qty);
        if (!tryLockQty(itemName, qty)) {
            throw new IllegalStateException("锁定库存失败");
        }
        // 1. 创建订单
        orderRepository.save(order);
        log.info("创建订单成功，订单号：{}-{}", order.getCode(), order.getId());
        // 2. 发送消息到 RabbitMQ
        CreateOrderMessage message = new CreateOrderMessage(order.getId(), order.getCode(), order.getItemName(), order.getQty());
        rabbitTemplate.convertAndSend("order-exchange", "order-routing-key", message);
        return order;
    }

    private boolean tryLockQty(String itemName, BigDecimal qty) {
        // 1. 调用库存服务的锁定库存接口
        String url = "http://localhost:9001/stock/tryLockQty";
        Map<String, Object> params = new HashMap<>();
        params.put("itemName", itemName);
        params.put("totalQty", qty);
        try {
            return restTemplate.postForObject(url, params, Boolean.class);
        } catch (Exception e) {
            log.error("调用库存服务锁定库存失败", e);
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void onSuccess(OrderStockCallback message) {
        // 处理成功回调
        log.info("订单[{}]扣减库存成功", message.orderId());
        this.orderRepository.updateStatus(message.orderId(), 1);
    }

    @Transactional(rollbackFor = Exception.class)
    public void onFailure(OrderStockCallback message) {
        // 处理失败回调
        log.info("订单[{}]扣减库存失败", message.orderId());
        this.orderRepository.updateStatus(message.orderId(), 2);
    }

}
