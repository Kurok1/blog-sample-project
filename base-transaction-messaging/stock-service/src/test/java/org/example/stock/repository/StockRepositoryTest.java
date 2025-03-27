package org.example.stock.repository;

import org.example.stock.entity.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    void findByItemName() throws Exception {
        Stock stock = stockRepository.findByItemName("item1");
        assertNotNull(stock);
    }

    @Test
    void lockStock() throws Exception {
        Stock stock = stockRepository.findByItemName("item1");
        assertNotNull(stock);
        int result = stockRepository.lockStock(stock.getId(), new BigDecimal("10"));
        assertEquals(1, result);
    }

    @Test
    void reduceStock() throws Exception {
        Stock stock = stockRepository.findByItemName("item1");
        assertNotNull(stock);
        int result = stockRepository.reduceStock(stock.getId(), new BigDecimal("10"));
        assertEquals(1, result);
    }
}