package org.example.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.example.TariffProcessingException;
import org.example.TestFiles;
import org.example.model.Tariff;

/** Один набір тестів для всіх трьох парсерів – результат має бути однаковим. */
class TariffParsersTest {

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void parsesValidFile(ParserType type) throws Exception {
        List<Tariff> result = type.create().parse(TestFiles.resource("tariffs.xml"));

        assertEquals(TestFiles.expectedTariffs(), result);
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void failsOnMissingFile(ParserType type, @TempDir Path dir) {
        TariffParser parser = type.create();
        Path missing = dir.resolve("nope.xml");

        assertThrows(TariffProcessingException.class, () -> parser.parse(missing));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void failsOnMalformedXml(ParserType type) {
        TariffParser parser = type.create();
        Path file = TestFiles.resource("malformed.xml");

        TariffProcessingException e = assertThrows(TariffProcessingException.class, () -> parser.parse(file));
        assertTrue(e.getMessage().contains("malformed.xml"));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void failsOnInvalidNumber(ParserType type) {
        TariffParser parser = type.create();
        Path file = TestFiles.resource("bad-number.xml");

        TariffProcessingException e = assertThrows(TariffProcessingException.class, () -> parser.parse(file));
        assertInstanceOf(NumberFormatException.class, e.getCause());
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void failsOnMissingElement(ParserType type) {
        TariffParser parser = type.create();
        Path file = TestFiles.resource("missing-element.xml");

        TariffProcessingException e = assertThrows(TariffProcessingException.class, () -> parser.parse(file));
        assertInstanceOf(IllegalStateException.class, e.getCause());
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void factoryCreatesMatchingParser(ParserType type) {
        TariffParser parser = type.create();

        String expected = switch (type) {
            case SAX -> SaxTariffParser.class.getName();
            case DOM -> DomTariffParser.class.getName();
            case STAX -> StaxTariffParser.class.getName();
        };
        assertEquals(expected, parser.getClass().getName());
    }
}
