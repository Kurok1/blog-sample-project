package org.example.order.listener;

import com.example.messages.OrderStockCallback;
import org.example.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:khanc.dev@gmail.com">韩超</a>
 * @since 1.0
 */
@Component
public class OrderCallbackListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCallbackListener.class);

    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = "order-callback")
    public void onEvent(@Payload OrderStockCallback value) {
        log.info("接收消息：{}" , value);
        if (value.status() == 1)
            orderService.onSuccess(value);
        else
            orderService.onFailure(value);
    }

}
