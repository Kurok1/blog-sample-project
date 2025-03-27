package org.example.stock.controller;

import org.example.stock.entity.Stock;
import org.example.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="mailto:khanc.dev@gmail.com">韩超</a>
 * @since 1.0
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{itemName}")
    public Stock findByItemName(@PathVariable("itemName") String itemName) {
        return stockService.findByItemName(itemName);
    }

    @PostMapping("/tryLockQty")
    public boolean tryLockQty(@RequestBody Stock stock) {
        return stockService.tryLockQty(stock);
    }

}
