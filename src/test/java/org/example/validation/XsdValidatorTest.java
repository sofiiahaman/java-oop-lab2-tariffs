package org.example.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.example.TariffProcessingException;
import org.example.TestFiles;

class XsdValidatorTest {

    private final XsdValidator validator = new XsdValidator();
    private final Path xsd = TestFiles.resource("tariffs.xsd");

    @Test
    void validDocumentHasNoErrors() throws Exception {
        assertTrue(validator.validate(TestFiles.resource("tariffs.xml"), xsd).isEmpty());
    }

    @Test
    void invalidDocumentReportsAllErrors() throws Exception {
        List<String> errors = validator.validate(TestFiles.resource("invalid-tariffs.xml"), xsd);

        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(e -> e.contains("bad-id")), "id pattern: " + errors);
        assertTrue(errors.size() >= 4, "expected several errors, got: " + errors);
    }

    @Test
    void malformedDocumentThrows() {
        Path file = TestFiles.resource("malformed.xml");
        assertThrows(TariffProcessingException.class, () -> validator.validate(file, xsd));
    }

    @Test
    void missingDocumentThrows(@TempDir Path dir) {
        Path missing = dir.resolve("missing.xml");
        assertThrows(TariffProcessingException.class, () -> validator.validate(missing, xsd));
    }

    @Test
    void brokenSchemaThrows() {
        Path file = TestFiles.resource("tariffs.xml");
        Path brokenXsd = TestFiles.resource("broken.xsd");
        assertThrows(TariffProcessingException.class, () -> validator.validate(file, brokenXsd));
    }
}