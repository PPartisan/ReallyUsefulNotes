package com.werdpressed.partisan.reallyusefulnotes.designlibrary.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public final class ConversionUtils {

    public static final int FAB_SIZE_DEFAULT_DP = 56;

    private ConversionUtils() { throw new AssertionError(); }

    public static boolean isPossibleToCastLongToInt(long value) {
        return ((value > Integer.MIN_VALUE) && (value < Integer.MAX_VALUE));
    }

    public static boolean isPossibleToCastLongToIntThrowsException(long value) {
        if (!isPossibleToCastLongToInt(value)) {
            throw new IllegalArgumentException(
                    "Long value must be between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE);
        }
        return true;
    }

    public static float convertDpToPixels(Resources resources, float dp) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }
}
