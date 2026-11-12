package com.fenny.moon;

/** Mean lunar cycle approximation; intended for a home screen, not navigation. */
public final class MoonPhase {
    private static final double SYNODIC_DAYS = 29.530588853;
    // New moon: 2000-01-06 18:14 UTC.
    private static final long EPOCH = 947182440000L;
    public final double cycle;
    public final double illumination;

    public MoonPhase(long timeMillis) {
        double cycles = (timeMillis - EPOCH) / 86400000.0 / SYNODIC_DAYS;
        cycle = cycles - Math.floor(cycles);
        illumination = (1 - Math.cos(2 * Math.PI * cycle)) / 2;
    }

    public String name() {
        String[] names = {"New Moon", "Waxing Crescent", "First Quarter", "Waxing Gibbous",
                "Full Moon", "Waning Gibbous", "Last Quarter", "Waning Crescent"};
        return names[(int) Math.floor(cycle * 8 + 0.5) % 8];
    }
}
