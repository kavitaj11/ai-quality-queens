package com.k11.pwj.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/** An order as returned by POST /api/checkout and GET /api/orders/{id}. */
public record Order(String orderId, String status, String username, String currency,
                    BigDecimal total, OffsetDateTime createdAt, List<OrderLine> lines) {

    public record OrderLine(int productId, String name, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
    }
}
