package com.k11.shop.pages;

import java.math.BigDecimal;

/** Money is compared as BigDecimal, never double: 0.1 + 0.2 != 0.3 in floating point. */
public final class Money {

    private Money() {
    }

    public static BigDecimal parse(String displayed) {
        return new BigDecimal(displayed.replace("$", "").replace(",", "").trim());
    }
}
