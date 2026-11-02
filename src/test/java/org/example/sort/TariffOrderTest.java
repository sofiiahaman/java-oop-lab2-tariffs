package org.example.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.example.TestFiles;
import org.example.model.Tariff;

class TariffOrderTest {

    private static List<String> ids(TariffOrder order) {
        List<Tariff> tariffs = new ArrayList<>(TestFiles.expectedTariffs());
        tariffs.sort(order);
        return tariffs.stream().map(Tariff::id).toList();
    }

    @Test
    void sortsByName() {
        // Lifecell Easy, Lifecell Max, Smart Plus, Smart Start, Vodafone Light, Vodafone Red
        assertEquals(List.of("T005", "T006", "T002", "T001", "T004", "T003"), ids(TariffOrder.NAME));
    }

    @Test
    void sortsByPayroll() {
        assertEquals(List.of("T001", "T004", "T005", "T002", "T006", "T003"), ids(TariffOrder.PAYROLL));
    }

    @Test
    void sortsByOperatorThenPayroll() {
        // Kyivstar < Vodafone < lifecell (природний порядок String, регістр враховується)
        assertEquals(List.of("T001", "T002", "T004", "T003", "T005", "T006"), ids(TariffOrder.OPERATOR_THEN_PAYROLL));
    }

    @Test
    void payrollTieIsBrokenByName() {
        Tariff a = TestFiles.expectedTariffs().get(0);
        Tariff b = new Tariff("T999", "A first", a.operatorName(), a.payroll(),
                a.callPrices(), a.smsPrice(), a.parameters());

        assertEquals(true, TariffOrder.PAYROLL.compare(b, a) < 0);
    }
}
