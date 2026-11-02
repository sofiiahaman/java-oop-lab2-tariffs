package org.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class TariffBuilderTest {

    private static TariffBuilder full() {
        return new TariffBuilder().id("T001")
                .set("Name", " Test ")
                .set("OperatorName", "Vodafone")
                .set("Payroll", "10.50")
                .set("InsideNetwork", "0.10")
                .set("OutsideNetwork", "0.20")
                .set("Landline", "0.30")
                .set("SmsPrice", "0.40")
                .set("FavoriteNumbers", "2")
                .set("Tarification", "per-minute")
                .set("ConnectionFee", "5.00");
    }

    @Test
    void buildsTariffFromAllFields() {
        Tariff t = full().build();

        assertEquals("T001", t.id());
        assertEquals("Test", t.name());
        assertEquals("Vodafone", t.operatorName());
        assertEquals(new BigDecimal("10.50"), t.payroll());
        assertEquals(new BigDecimal("0.30"), t.callPrices().landline());
        assertEquals(2, t.parameters().favoriteNumbers());
        assertEquals(Tarification.PER_MINUTE, t.parameters().tarification());
    }

    @Test
    void ignoresContainerElements() {
        Tariff t = full().set("CallPrices", "\n  ").set("Parameters", "").build();
        assertEquals("Test", t.name());
    }

    @Test
    void failsWhenElementMissing() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> new TariffBuilder().id("T001").build());
        assertTrue(e.getMessage().contains("InsideNetwork"));
    }

    @Test
    void failsWhenIdMissing() {
        TariffBuilder builder = new TariffBuilder()
                .set("Name", "n").set("OperatorName", "o").set("Payroll", "1")
                .set("InsideNetwork", "1").set("OutsideNetwork", "1").set("Landline", "1")
                .set("SmsPrice", "1").set("FavoriteNumbers", "1")
                .set("Tarification", "per-minute").set("ConnectionFee", "1");
        IllegalStateException e = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(e.getMessage().contains("id"));
    }

    @Test
    void failsOnInvalidNumber() {
        TariffBuilder builder = new TariffBuilder();
        assertThrows(NumberFormatException.class, () -> builder.set("Payroll", "abc"));
    }
}
