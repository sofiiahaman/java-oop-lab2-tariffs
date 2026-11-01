package org.example.validation;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/** Collects all validation errors; fatal errors abort parsing. */
final class ErrorCollector implements ErrorHandler {

    private final List<String> messages = new ArrayList<>();

    @Override
    public void warning(SAXParseException e) {
        record(e);
    }

    @Override
    public void error(SAXParseException e) {
        record(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXParseException {
        record(e);
        throw e;
    }

    List<String> messages() {
        return messages;
    }

    private void record(SAXParseException e) {
        messages.add("line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e.getMessage());
    }
}