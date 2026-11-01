package org.example.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.example.XmlNames;
import org.example.model.Tariff;
import org.example.model.TariffBuilder;

/** DOM-based parsing. */
public class DomTariffParser extends AbstractTariffParser {

    @Override
    protected List<Tariff> doParse(InputStream in) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        Document document = factory.newDocumentBuilder().parse(in);
        NodeList nodes = document.getElementsByTagName(XmlNames.TARIFF);

        List<Tariff> tariffs = new ArrayList<>(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            tariffs.add(toTariff((Element) nodes.item(i)));
        }
        return tariffs;
    }

    private static Tariff toTariff(Element element) {
        TariffBuilder builder = new TariffBuilder().id(element.getAttribute(XmlNames.ID));
        NodeList descendants = element.getElementsByTagName("*");
        for (int i = 0; i < descendants.getLength(); i++) {
            Element child = (Element) descendants.item(i);
            builder.set(child.getTagName(), child.getTextContent());
        }
        return builder.build();
    }
}