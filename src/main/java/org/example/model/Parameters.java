package org.example.model;

import java.math.BigDecimal;

/** Tariff parameters. */
public record Parameters(int favoriteNumbers, Tarification tarification, BigDecimal connectionFee) {
}