package org.example.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.example.TariffProcessingException;
import org.example.TestFiles;

class XslTransformerTest {

    private final XslTransformer transformer = new XslTransformer();
    private final Path xml = TestFiles.resource("tariffs.xml");
    private final Path xsl = TestFiles.resource("tariffs-grouped.xsl");

    private static Document read(Path file) throws Exception {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file.toFile());
    }

    private static String xpath(Document doc, String expression) throws Exception {
        return (String) XPathFactory.newInstance().newXPath().evaluate(expression, doc, XPathConstants.STRING);
    }

    @Test
    void groupsTariffsByOperator(@TempDir Path dir) throws Exception {
        Path out = dir.resolve("nested/result.xml");

        transformer.transform(xml, xsl, out);

        Document doc = read(out);
        NodeList groups = doc.getDocumentElement().getChildNodes();
        int count = 0;
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < groups.getLength(); i++) {
            Node n = groups.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                count++;
                names.append(n.getNodeName()).append(',');
            }
        }
        assertEquals(3, count);
        assertEquals("Kyivstar,lifecell,Vodafone,", names.toString());
        assertEquals("2", xpath(doc, "count(/Tariffs/Kyivstar/Tariff)"));
        assertEquals("2", xpath(doc, "count(/Tariffs/Vodafone/Tariff)"));
        assertEquals("2", xpath(doc, "count(/Tariffs/lifecell/Tariff)"));
    }

    @Test
    void itemsInsideGroupAreSortedByPayroll(@TempDir Path dir) throws Exception {
        Path out = dir.resolve("result.xml");

        transformer.transform(xml, xsl, out);

        Document doc = read(out);
        assertEquals("T004", xpath(doc, "/Tariffs/Vodafone/Tariff[1]/@id")); // 90 < 300
        assertEquals("T003", xpath(doc, "/Tariffs/Vodafone/Tariff[2]/@id"));
        assertTrue(Files.size(out) > 0);
    }

    @Test
    void brokenStylesheetThrows(@TempDir Path dir) {
        Path broken = TestFiles.resource("broken.xsl");
        Path out = dir.resolve("x.xml");
        assertThrows(TariffProcessingException.class, () -> transformer.transform(xml, broken, out));
    }

    @Test
    void missingInputThrows(@TempDir Path dir) {
        Path missing = dir.resolve("missing.xml");
        Path out = dir.resolve("x.xml");
        assertThrows(TariffProcessingException.class, () -> transformer.transform(missing, xsl, out));
        assertTrue(Files.notExists(out), "result file must not be left behind (locks the file on Windows)");
    }
}