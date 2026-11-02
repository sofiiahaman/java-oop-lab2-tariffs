package org.example.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

class ErrorCollectorTest {

    private static SAXParseException exception(String message) {
        return new SAXParseException(message, null, null, 3, 7);
    }

    @Test
    void collectsWarningsAndErrors() {
        ErrorCollector collector = new ErrorCollector();

        collector.warning(exception("w"));
        collector.error(exception("e"));

        assertEquals(2, collector.messages().size());
        assertTrue(collector.messages().get(0).contains("line 3, column 7: w"));
    }

    @Test
    void fatalErrorIsRecordedAndRethrown() {
        ErrorCollector collector = new ErrorCollector();
        SAXParseException fatal = exception("boom");

        assertSame(fatal, assertThrows(SAXParseException.class, () -> collector.fatalError(fatal)));
        assertEquals(1, collector.messages().size());
    }
}
