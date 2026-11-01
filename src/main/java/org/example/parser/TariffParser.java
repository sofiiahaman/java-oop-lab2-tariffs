package org.example.parser;

import java.nio.file.Path;
import java.util.List;

import org.example.TariffProcessingException;
import org.example.model.Tariff;

/** Parses an XML file into a collection of tariffs. */
public interface TariffParser {

    List<Tariff> parse(Path xmlFile) throws TariffProcessingException;
}