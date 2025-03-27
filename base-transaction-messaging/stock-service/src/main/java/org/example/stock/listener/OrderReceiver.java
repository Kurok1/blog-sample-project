package org.example.stock.listener;

import com.example.messages.CreateOrderMessage;
import org.example.stock.service.StockService;
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
public class OrderReceiver {

    @Autowired
    private StockService stockService;
    private static final Logger log = LoggerFactory.getLogger(OrderReceiver.class);

    @RabbitListener(queues = "order")
    public void onOrder(@Payload CreateOrderMessage message) {
        log.info("接收消息：{}" , message);
        //扣减库存
        this.stockService.reduceStock(message);
    }

}
