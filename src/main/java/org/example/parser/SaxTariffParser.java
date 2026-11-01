package org.example.parser;

import java.io.InputStream;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import org.example.XmlNames;
import org.example.model.Tariff;

/** SAX-based parsing. */
public class SaxTariffParser extends AbstractTariffParser {

    @Override
    protected List<Tariff> doParse(InputStream in) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        Handler handler = new Handler();
        factory.newSAXParser().parse(in, handler);
        return handler.collector.tariffs();
    }

    private static final class Handler extends DefaultHandler {

        private final TariffCollector collector = new TariffCollector();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            collector.startElement(localName, attributes.getValue(XmlNames.ID));
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            collector.characters(new String(ch, start, length));
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            collector.endElement(localName);
        }
    }
}