package org.example.model;

/** Tarification: corresponds to the TarificationType enumeration in XSD. */
public enum Tarification {
    TWELVE_SECOND("12-second"),
    PER_MINUTE("per-minute");

    private final String xmlValue;

    Tarification(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    public String xmlValue() {
        return xmlValue;
    }

    public static Tarification fromXml(String value) {
        for (Tarification t : values()) {
            if (t.xmlValue.equals(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown tarification: " + value);
    }
}