package cz.blackdragoncz.lostdepths.util;

import java.util.Locale;

/** Shared FE display formatting. Presentation only - never use it to shrink a value before transmitting. */
public final class EnergyFormat {

    private static final String[] UNITS = {"", "k", "M", "G", "T"};

    private EnergyFormat() {}

    /** "812", "1.25k", "84.3k", "450k", "1.25M". */
    public static String compact(long value) {
        if (value < 0) return "-" + compact(-value);
        if (value < 1000) return Long.toString(value);

        int unit = 0;
        double scaled = value;
        while (scaled >= 1000 && unit < UNITS.length - 1) {
            scaled /= 1000d;
            unit++;
        }

        int decimals = scaled < 10 ? 2 : scaled < 100 ? 1 : 0;

        // Rounding carries 999,999 up to "1000k"; promote so it reads "1M".
        double factor = Math.pow(10, decimals);
        if (Math.round(scaled * factor) / factor >= 1000 && unit < UNITS.length - 1) {
            scaled /= 1000d;
            unit++;
            decimals = 2;
        }

        // Locale.ROOT or a Czech client renders "84,3k".
        String text = String.format(Locale.ROOT, "%." + decimals + "f", scaled);
        if (text.contains(".")) text = text.replaceAll("0+$", "").replaceAll("\\.$", "");
        return text + UNITS[unit];
    }

    /** "84.3k FE" */
    public static String withUnit(long value) {
        return compact(value) + " FE";
    }

    /** "84.3k / 450k FE" */
    public static String stored(long stored, long capacity) {
        return compact(stored) + " / " + compact(capacity) + " FE";
    }
}
