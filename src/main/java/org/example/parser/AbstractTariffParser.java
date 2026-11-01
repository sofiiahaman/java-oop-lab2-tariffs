package org.example.parser;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.TariffProcessingException;
import org.example.model.Tariff;

/**
 * Template method: file opening, logging, and error wrapping in one place.
 * Subclasses implement only the actual stream parsing.
 */
public abstract class AbstractTariffParser implements TariffParser {

    private static final Logger LOG = LogManager.getLogger(AbstractTariffParser.class);

    @Override
    public final List<Tariff> parse(Path xmlFile) throws TariffProcessingException {
        String parserName = getClass().getSimpleName();
        LOG.debug("{}: parsing {}", parserName, xmlFile);
        try (InputStream in = Files.newInputStream(xmlFile)) {
            List<Tariff> tariffs = doParse(in);
            LOG.info("{}: parsed {} tariff(s) from {}", parserName, tariffs.size(), xmlFile);
            return tariffs;
        } catch (Exception e) {
            LOG.error("{}: failed to parse {}", parserName, xmlFile, e);
            throw new TariffProcessingException(
                    "Cannot parse " + xmlFile + " with " + parserName + ": " + e.getMessage(), e);
        }
    }

    protected abstract List<Tariff> doParse(InputStream in) throws Exception;
}