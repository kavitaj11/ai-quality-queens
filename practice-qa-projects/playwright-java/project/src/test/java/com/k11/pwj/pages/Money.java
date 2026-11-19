package com.k11.pwj.pages;

import java.math.BigDecimal;

/** Money is BigDecimal, never double: in floating point, 0.1 + 0.2 != 0.3. */
public final class Money {

    private Money() {
    }

    /** Parses "$1,234.50" or "1234.50". */
    public static BigDecimal parse(String text) {
        return new BigDecimal(text.replace("$", "").replace(",", "").trim());
    }
}
