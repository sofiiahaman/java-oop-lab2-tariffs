package org.example.parser;

import java.io.InputStream;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.example.XmlNames;
import org.example.model.Tariff;

/** StAX-based parsing (cursor API). */
public class StaxTariffParser extends AbstractTariffParser {

    @Override
    protected List<Tariff> doParse(InputStream in) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

        TariffCollector collector = new TariffCollector();
        XMLStreamReader reader = factory.createXMLStreamReader(in);
        try {
            while (reader.hasNext()) {
                switch (reader.next()) {
                    case XMLStreamConstants.START_ELEMENT ->
                            collector.startElement(reader.getLocalName(), reader.getAttributeValue(null, XmlNames.ID));
                    case XMLStreamConstants.CHARACTERS -> collector.characters(reader.getText());
                    case XMLStreamConstants.END_ELEMENT -> collector.endElement(reader.getLocalName());
                    default -> {
                        // comments, PIs, etc. are not needed
                    }
                }
            }
        } finally {
            reader.close();
        }
        return collector.tariffs();
    }
}