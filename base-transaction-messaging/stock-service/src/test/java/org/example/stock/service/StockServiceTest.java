package org.example.stock.service;

import org.example.stock.entity.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Test
    void tryLockQty() throws Exception {
        Stock test1 = new Stock();
        test1.setItemName("item1");
        test1.setTotalQty(new BigDecimal("10"));
        assertTrue(stockService.tryLockQty(test1));

        test1.setTotalQty(new BigDecimal("91"));
        assertFalse(stockService.tryLockQty(test1));
    }
}