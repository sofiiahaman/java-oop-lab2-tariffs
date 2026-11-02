package org.example;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import org.example.model.CallPrices;
import org.example.model.Parameters;
import org.example.model.Tariff;
import org.example.model.Tarification;

/** Helper methods for tests. */
public final class TestFiles {

    private TestFiles() {
    }

    public static Path resource(String name) {
        try {
            return Path.of(TestFiles.class.getResource("/" + name).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Tariff tariff(String id, String name, String operator, String payroll,
                                 String in, String out, String landline, String sms,
                                 int fav, Tarification t, String fee) {
        return new Tariff(id, name, operator, new BigDecimal(payroll),
                new CallPrices(new BigDecimal(in), new BigDecimal(out), new BigDecimal(landline)),
                new BigDecimal(sms),
                new Parameters(fav, t, new BigDecimal(fee)));
    }

    /** Expected content of src/main/resources/tariffs.xml. */
    public static List<Tariff> expectedTariffs() {
        return List.of(
                tariff("T001", "Smart Start", "Kyivstar", "0.00", "0.80", "1.50", "2.00", "0.50", 0, Tarification.PER_MINUTE, "0.00"),
                tariff("T002", "Smart Plus", "Kyivstar", "150.00", "0.00", "1.20", "1.50", "0.40", 2, Tarification.TWELVE_SECOND, "50.00"),
                tariff("T003", "Vodafone Red", "Vodafone", "300.00", "0.00", "0.50", "0.80", "0.00", 5, Tarification.TWELVE_SECOND, "100.00"),
                tariff("T004", "Vodafone Light", "Vodafone", "90.00", "0.60", "1.80", "2.50", "0.60", 1, Tarification.PER_MINUTE, "0.00"),
                tariff("T005", "Lifecell Easy", "lifecell", "120.00", "0.00", "1.00", "1.80", "0.45", 3, Tarification.PER_MINUTE, "30.00"),
                tariff("T006", "Lifecell Max", "lifecell", "250.00", "0.00", "0.30", "0.50", "0.00", 10, Tarification.TWELVE_SECOND, "75.50"));
    }
}