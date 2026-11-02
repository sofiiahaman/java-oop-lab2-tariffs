package org.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TarificationTest {

    @Test
    void parsesKnownValues() {
        assertEquals(Tarification.TWELVE_SECOND, Tarification.fromXml("12-second"));
        assertEquals(Tarification.PER_MINUTE, Tarification.fromXml("per-minute"));
    }

    @Test
    void xmlValueRoundTrips() {
        for (Tarification t : Tarification.values()) {
            assertEquals(t, Tarification.fromXml(t.xmlValue()));
        }
    }

    @Test
    void rejectsUnknownValue() {
        assertThrows(IllegalArgumentException.class, () -> Tarification.fromXml("per-second"));
    }
}