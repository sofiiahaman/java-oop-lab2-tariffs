package org.example.validation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import org.example.TariffProcessingException;

/** Validates an XML document against an XSD schema. */
public class XsdValidator {

    private static final Logger LOG = LogManager.getLogger(XsdValidator.class);

    /**
     * @return list of error messages; empty if the document is valid
     * @throws TariffProcessingException if the schema or document cannot be read or the document is not well-formed
     */
    public List<String> validate(Path xmlFile, Path xsdFile) throws TariffProcessingException {
        LOG.debug("Validating {} against {}", xmlFile, xsdFile);
        ErrorCollector collector = new ErrorCollector();
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xsdFile.toFile());
            Validator validator = schema.newValidator();
            validator.setErrorHandler(collector);
            validator.validate(new StreamSource(xmlFile.toFile()));
        } catch (SAXException | IOException e) {
            LOG.error("Validation of {} aborted: {}", xmlFile, e.getMessage());
            throw new TariffProcessingException("Cannot validate " + xmlFile + ": " + e.getMessage(), e);
        }
        List<String> errors = collector.messages();
        if (errors.isEmpty()) {
            LOG.info("{} is valid against {}", xmlFile, xsdFile);
        } else {
            LOG.warn("{} is NOT valid: {} error(s)", xmlFile, errors.size());
        }
        return errors;
    }
}
