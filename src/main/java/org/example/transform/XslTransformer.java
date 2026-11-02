package org.example.transform;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        // Open streams explicitly and close them in try-with-resources: otherwise on Windows
        // the result file remains locked if the transformation fails with an error.
        // Open the input file first so that an empty result is not created if it is missing.
        try (InputStream xml = Files.newInputStream(xmlFile);
             InputStream xsl = Files.newInputStream(xslFile)) {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = factory.newTransformer(new StreamSource(xsl));

            Files.createDirectories(outputFile.toAbsolutePath().getParent());
            try (OutputStream out = Files.newOutputStream(outputFile)) {
                transformer.transform(new StreamSource(xml), new StreamResult(out));
            }
            LOG.info("Transformation result written to {}", outputFile);
        } catch (TransformerException | IOException e) {
            LOG.error("Transformation failed: {}", e.getMessage());
            throw new TariffProcessingException("Cannot transform " + xmlFile + ": " + e.getMessage(), e);
        }
    }
}