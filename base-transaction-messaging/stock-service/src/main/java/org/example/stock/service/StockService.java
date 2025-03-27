package org.example.stock.service;

import com.example.messages.CreateOrderMessage;
import com.example.messages.OrderStockCallback;
import org.example.stock.entity.Stock;
import org.example.stock.lock.StockLock;
import org.example.stock.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:khanc.dev@gmail.com">韩超</a>
 * @since 1.0
 */
@Service
public class StockService {

    private static final Logger log = LoggerFactory.getLogger(StockService.class);
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StockLock lock;

    public Stock findByItemName(String itemName) {
        return this.stockRepository.findByItemName(itemName);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean tryLockQty(Stock stock) {

        ReentrantLock lock = this.lock.getLock();
        try {

            if (!lock.tryLock(3, TimeUnit.SECONDS)) {
                return false;
            }
            Stock stockInDb = stockRepository.findByItemName(stock.getItemName());
            if (stockInDb == null) {
                return false;
            }

            if (stockInDb.getTotalQty().compareTo(stockInDb.getLockedQty().add(stock.getTotalQty())) <= 0)
                return false;

            int effective = this.stockRepository.lockStock(stockInDb.getId(), stock.getTotalQty());
            return effective > 0;
        } catch (Exception e) {
            return false;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void reduceStock(CreateOrderMessage message) {
        //由于之前流程已经锁定库存了，所以只需要直接扣减即可
        ReentrantLock lock = this.lock.getLock();
        lock.lock();

        try {
            //扣减库存
            log.warn("订单[{}]扣减货品[{}]库存数量[{}]", message.orderId(), message.itemName(), message.qty());
            Stock stock = stockRepository.findByItemName(message.itemName());
            if (stock == null || stock.getLockedQty().compareTo(message.qty()) < 0) {
                OrderStockCallback callback = new OrderStockCallback(message.orderId(), 2, "库存检查失败");
                this.rabbitTemplate.convertAndSend("order-callback-exchange", "order-callback-routing-key", callback);
            }
            int effective = this.stockRepository.reduceStock(stock.getId(), message.qty());
            OrderStockCallback callback;
            if (effective > 0) {
                callback = new OrderStockCallback(message.orderId(), 1, "success");
            } else {
                callback = new OrderStockCallback(message.orderId(), 2, "库存扣减失败");
            }
            this.rabbitTemplate.convertAndSend("order-callback-exchange", "order-callback-routing-key", callback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
