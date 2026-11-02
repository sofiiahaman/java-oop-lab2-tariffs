package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AppTest {

    private final Path xsd = TestFiles.resource("tariffs.xsd");
    private final Path xsl = TestFiles.resource("tariffs-grouped.xsl");

    @Test
    void runSucceedsOnValidDocument(@TempDir Path out) {
        int code = App.run(TestFiles.resource("tariffs.xml"), xsd, xsl, out);

        assertEquals(App.OK, code);
        assertTrue(Files.exists(out.resolve("tariffs-grouped.xml")));
    }

    @Test
    void runReportsInvalidDocument(@TempDir Path out) {
        int code = App.run(TestFiles.resource("invalid-tariffs.xml"), xsd, xsl, out);

        assertEquals(App.INVALID_XML, code);
        assertTrue(Files.notExists(out.resolve("tariffs-grouped.xml")));
    }

    @Test
    void runReportsFailure(@TempDir Path out) {
        int code = App.run(out.resolve("missing.xml"), xsd, xsl, out);

        assertEquals(App.FAILURE, code);
    }

    @Test
    void mainWithExplicitArguments(@TempDir Path out) {
        App.main(new String[] {
                TestFiles.resource("tariffs.xml").toString(),
                xsd.toString(),
                xsl.toString(),
                out.toString()});

        assertTrue(Files.exists(out.resolve("tariffs-grouped.xml")));
    }

    @Test
    void mainWithDefaultArguments() {
        // Works from the project directory: src/main/resources/..., result goes to output/
        App.main(new String[0]);

        assertTrue(Files.exists(Path.of("output", "tariffs-grouped.xml")));
    }
}