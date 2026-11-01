package org.example;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.model.Tariff;
import org.example.parser.ParserType;
import org.example.sort.TariffOrder;
import org.example.transform.XslTransformer;
import org.example.validation.XsdValidator;

/**
 * Entry point. Workflow:
 * 1) XML validation against XSD; 2) SAX/DOM/StAX parsing + sorting; 3) XSL transformation.
 * <p>
 * Arguments (optional): xml xsd xsl outputDir.
 */
public final class App {

    static final int OK = 0;
    static final int INVALID_XML = 1;
    static final int FAILURE = 2;

    private static final Logger LOG = LogManager.getLogger(App.class);

    private App() {
    }

    public static void main(String[] args) {
        Path xml = Path.of(arg(args, 0, "src/main/resources/tariffs.xml"));
        Path xsd = Path.of(arg(args, 1, "src/main/resources/tariffs.xsd"));
        Path xsl = Path.of(arg(args, 2, "src/main/resources/tariffs-grouped.xsl"));
        Path outDir = Path.of(arg(args, 3, "output"));

        int code = run(xml, xsd, xsl, outDir);
        if (code != OK) {
            System.exit(code);
        }
    }

    static int run(Path xml, Path xsd, Path xsl, Path outDir) {
        try {
            List<String> errors = new XsdValidator().validate(xml, xsd);
            if (!errors.isEmpty()) {
                errors.forEach(error -> LOG.error("XSD: {}", error));
                return INVALID_XML;
            }

            for (ParserType type : ParserType.values()) {
                List<Tariff> tariffs = new ArrayList<>(type.create().parse(xml));
                tariffs.sort(TariffOrder.PAYROLL);
                LOG.info("[{}] tariffs sorted by payroll:", type);
                tariffs.forEach(t -> LOG.info("  {} | {} | {} UAH", t.id(), t.name(), t.payroll()));
            }

            new XslTransformer().transform(xml, xsl, outDir.resolve("tariffs-grouped.xml"));
            return OK;
        } catch (TariffProcessingException e) {
            LOG.error("Processing failed: {}", e.getMessage());
            return FAILURE;
        }
    }

    private static String arg(String[] args, int index, String defaultValue) {
        return args.length > index ? args[index] : defaultValue;
    }
}
