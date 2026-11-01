package org.example.parser;

import java.util.ArrayList;
import java.util.List;

import org.example.XmlNames;
import org.example.model.Tariff;
import org.example.model.TariffBuilder;

/** Shared logic for event-driven parsers (SAX and StAX): start/text/end of element. */
final class TariffCollector {

    private final List<Tariff> tariffs = new ArrayList<>();
    private final StringBuilder text = new StringBuilder();
    private TariffBuilder current;

    void startElement(String name, String idAttribute) {
        text.setLength(0);
        if (XmlNames.TARIFF.equals(name)) {
            current = new TariffBuilder().id(idAttribute);
        }
    }

    void characters(String chars) {
        text.append(chars);
    }

    void endElement(String name) {
        if (XmlNames.TARIFF.equals(name)) {
            tariffs.add(current.build());
            current = null;
        } else if (current != null) {
            current.set(name, text.toString());
        }
    }

    List<Tariff> tariffs() {
        return tariffs;
    }
}