package org.example.model;

import java.math.BigDecimal;

/** Call prices (UAH per minute). */
public record CallPrices(BigDecimal insideNetwork, BigDecimal outsideNetwork, BigDecimal landline) {
}