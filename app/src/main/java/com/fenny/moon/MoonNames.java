package com.fenny.moon;

import java.util.Calendar;
import java.util.TimeZone;

/** Friendly mood labels and northern-hemisphere traditional full moon names. */
public final class MoonNames {
    private MoonNames() {}

    public static String label(MoonPhase phase, long millis) {
        String[] moods = {"Silent", "Boisterous", "Curious", "Waxing Gibbous",
                "", "Pensive", "Fatigued", "Shy"};
        int index = (int) Math.floor(phase.cycle * 8 + 0.5) % 8;
        return index == 4 ? fullMoonName(millis) : moods[index];
    }

    private static String fullMoonName(long millis) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(millis);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        String[] names = {"Wolf Moon", "Snow Moon", "Worm Moon", "Pink Moon",
                "Flower Moon", "Strawberry Moon", "Buck Moon", "Sturgeon Moon",
                "Corn Moon", "Hunter's Moon", "Beaver Moon", "Cold Moon"};

        // Harvest Moon is the full moon closest to the September equinox.
        // The next full moon is the Hunter's Moon, regardless of calendar month.
        long equinox = utcDate(year, Calendar.SEPTEMBER, 22);
        long harvest = nearestFullMoon(equinox);
        long hunters = nearestFullMoon(harvest + 30L * 86400000L);
        long thisFull = nearestFullMoon(millis);
        long tolerance = 2L * 86400000L;
        if (Math.abs(thisFull - harvest) < tolerance) return "Harvest Moon";
        if (Math.abs(thisFull - hunters) < tolerance) return "Hunter's Moon";

        // The second full moon in a month can optionally be called Blue Moon,
        // but seasonal names take precedence.
        long monthStart = utcDate(year, month, 1);
        long firstFull = nextFullMoon(monthStart);
        if (thisFull > firstFull + 20L * 86400000L &&
                thisFull < utcDate(year, month + 1, 1)) return "Blue Moon";
        return names[month];
    }

    private static long utcDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.clear();
        c.set(year, month, day, 12, 0, 0);
        return c.getTimeInMillis();
    }

    private static long nearestFullMoon(long millis) {
        double period = 29.530588853 * 86400000.0;
        double epoch = 947182440000.0;
        double index = Math.rint((millis - epoch) / period - 0.5);
        return Math.round(epoch + (index + 0.5) * period);
    }

    private static long nextFullMoon(long millis) {
        double period = 29.530588853 * 86400000.0;
        double epoch = 947182440000.0;
        double index = Math.ceil((millis - epoch) / period - 0.5);
        return Math.round(epoch + (index + 0.5) * period);
    }
}
