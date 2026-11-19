package com.k11.pwj.models;

import java.math.BigDecimal;

/** A product as returned by GET /api/products. Records give equals/hashCode/toString for free. */
public record Product(int id, String name, BigDecimal price, String category) {
}
