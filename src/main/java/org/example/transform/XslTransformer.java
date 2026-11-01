package org.example.transform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.TariffProcessingException;

/** XML transformation using XSL. */
public class XslTransformer {

    private static final Logger LOG = LogManager.getLogger(XslTransformer.class);

    public void transform(Path xmlFile, Path xslFile, Path outputFile) throws TariffProcessingException {
        LOG.debug("Transforming {} with {} into {}", xmlFile, xslFile, outputFile);
        try {
            Files.createDirectories(outputFile.toAbsolutePath().getParent());

            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = factory.newTransformer(new StreamSource(xslFile.toFile()));
            transformer.transform(new StreamSource(xmlFile.toFile()), new StreamResult(outputFile.toFile()));
            LOG.info("Transformation result written to {}", outputFile);
        } catch (TransformerException | IOException e) {
            LOG.error("Transformation failed: {}", e.getMessage());
            throw new TariffProcessingException("Cannot transform " + xmlFile + ": " + e.getMessage(), e);
        }
    }
}