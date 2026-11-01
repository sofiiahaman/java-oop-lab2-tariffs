package org.example.sort;

import java.util.Comparator;

import org.example.model.Tariff;

/** A set of comparators for sorting tariffs (implements {@link Comparator}). */
public enum TariffOrder implements Comparator<Tariff> {

    /** By tariff name (case-insensitive). */
    NAME(Comparator.comparing(Tariff::name, String.CASE_INSENSITIVE_ORDER)),

    /** By subscription fee, then by name. */
    PAYROLL(Comparator.comparing(Tariff::payroll).thenComparing(Tariff::name)),

    /** By operator, then by subscription fee, then by name. */
    OPERATOR_THEN_PAYROLL(Comparator.comparing(Tariff::operatorName)
            .thenComparing(Tariff::payroll)
            .thenComparing(Tariff::name));

    private final Comparator<Tariff> delegate;

    TariffOrder(Comparator<Tariff> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int compare(Tariff a, Tariff b) {
        return delegate.compare(a, b);
    }
}