package org.example.parser;

import java.util.function.Supplier;

/** Available parser implementations. */
public enum ParserType {
    SAX(SaxTariffParser::new),
    DOM(DomTariffParser::new),
    STAX(StaxTariffParser::new);

    private final Supplier<TariffParser> factory;

    ParserType(Supplier<TariffParser> factory) {
        this.factory = factory;
    }

    public TariffParser create() {
        return factory.get();
    }
}