package org.example.parser;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.example.TariffProcessingException;
import org.example.model.Tariff;

/**
 * Template method: file opening and error wrapping in one place.
 * Subclasses implement only the actual stream parsing.
 */
public abstract class AbstractTariffParser implements TariffParser {

    @Override
    public final List<Tariff> parse(Path xmlFile) throws TariffProcessingException {
        String parserName = getClass().getSimpleName();
        try (InputStream in = Files.newInputStream(xmlFile)) {
            List<Tariff> tariffs = doParse(in);
            return tariffs;
        } catch (Exception e) {
            throw new TariffProcessingException(
                    "Cannot parse " + xmlFile + " with " + parserName + ": " + e.getMessage(), e);
        }
    }

    protected abstract List<Tariff> doParse(InputStream in) throws Exception;
}