package com.example.messages;

import java.io.Serializable;

/**
 * @author <a href="mailto:khanc.dev@gmail.com">韩超</a>
 * @since 1.0
 */
public record OrderStockCallback(Long orderId, int status, String message) implements Serializable {


    private static final long serialVersionUID = -5584175346176397161L;

    @Override
    public String toString() {
        return "OrderStockCallback{" +
                "orderId=" + orderId +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
