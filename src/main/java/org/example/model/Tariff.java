package org.example.model;

import java.math.BigDecimal;

/**
 * Mobile operator tariff. Corresponds to the TariffType complex type in tariffs.xsd.
 *
 * @param id           unique identifier (ID type attribute)
 * @param name         tariff name
 * @param operatorName operator name
 * @param payroll      monthly subscription fee, UAH
 * @param callPrices   call prices
 * @param smsPrice     SMS price, UAH
 * @param parameters   additional parameters
 */
public record Tariff(String id,
                     String name,
                     String operatorName,
                     BigDecimal payroll,
                     CallPrices callPrices,
                     BigDecimal smsPrice,
                     Parameters parameters) {
}