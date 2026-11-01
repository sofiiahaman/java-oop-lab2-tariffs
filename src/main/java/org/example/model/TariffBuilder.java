package org.example.model;

import java.math.BigDecimal;

import org.example.XmlNames;

/**
 * Builds a {@link Tariff} from "element name - text" pairs. Shared between SAX, DOM, and StAX
 * to avoid duplicating text-to-type conversion logic.
 */
public final class TariffBuilder {

    private String id;
    private String name;
    private String operatorName;
    private BigDecimal payroll;
    private BigDecimal insideNetwork;
    private BigDecimal outsideNetwork;
    private BigDecimal landline;
    private BigDecimal smsPrice;
    private Integer favoriteNumbers;
    private Tarification tarification;
    private BigDecimal connectionFee;

    public TariffBuilder id(String value) {
        this.id = value;
        return this;
    }

    /** Accepts text of a leaf element; container elements (CallPrices, etc.) are ignored. */
    public TariffBuilder set(String element, String text) {
        String value = text.trim();
        switch (element) {
            case XmlNames.NAME -> name = value;
            case XmlNames.OPERATOR_NAME -> operatorName = value;
            case XmlNames.PAYROLL -> payroll = new BigDecimal(value);
            case XmlNames.INSIDE_NETWORK -> insideNetwork = new BigDecimal(value);
            case XmlNames.OUTSIDE_NETWORK -> outsideNetwork = new BigDecimal(value);
            case XmlNames.LANDLINE -> landline = new BigDecimal(value);
            case XmlNames.SMS_PRICE -> smsPrice = new BigDecimal(value);
            case XmlNames.FAVORITE_NUMBERS -> favoriteNumbers = Integer.valueOf(value);
            case XmlNames.TARIFICATION -> tarification = Tarification.fromXml(value);
            case XmlNames.CONNECTION_FEE -> connectionFee = new BigDecimal(value);
            default -> {
                // container element – do nothing
            }
        }
        return this;
    }

    public Tariff build() {
        CallPrices callPrices = new CallPrices(
                required(insideNetwork, XmlNames.INSIDE_NETWORK),
                required(outsideNetwork, XmlNames.OUTSIDE_NETWORK),
                required(landline, XmlNames.LANDLINE));
        Parameters parameters = new Parameters(
                required(favoriteNumbers, XmlNames.FAVORITE_NUMBERS),
                required(tarification, XmlNames.TARIFICATION),
                required(connectionFee, XmlNames.CONNECTION_FEE));
        return new Tariff(
                required(id, XmlNames.ID),
                required(name, XmlNames.NAME),
                required(operatorName, XmlNames.OPERATOR_NAME),
                required(payroll, XmlNames.PAYROLL),
                callPrices,
                required(smsPrice, XmlNames.SMS_PRICE),
                parameters);
    }

    private static <T> T required(T value, String field) {
        if (value == null) {
            throw new IllegalStateException("Missing required element/attribute: " + field);
        }
        return value;
    }
}