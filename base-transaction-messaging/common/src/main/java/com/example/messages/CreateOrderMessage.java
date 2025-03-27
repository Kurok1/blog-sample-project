package com.example.messages;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author <a href="mailto:khanc.dev@gmail.com">韩超</a>
 * @since 1.0
 */
public record CreateOrderMessage(Long orderId, String orderCode, String itemName, BigDecimal qty) implements Serializable {


    private static final long serialVersionUID = 2785045088898668862L;

    @Override
    public String toString() {
        return "CreateOrderMessage{" +
                "orderId=" + orderId +
                ", orderCode='" + orderCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", qty=" + qty +
                '}';
    }
}
