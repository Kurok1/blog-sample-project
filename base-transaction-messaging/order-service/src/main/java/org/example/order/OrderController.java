package org.example.order;

import org.example.order.entity.Order;
import org.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="mailto:khanc.dev@gmail.com">韩超</a>
 * @since 1.0
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.createOrder(order.getCode(), order.getItemName(), order.getQty());
    }

    @GetMapping("/{code}")
    public Order findByCode(@PathVariable("code") String code) {
        return orderService.findByCode(code);
    }

}
