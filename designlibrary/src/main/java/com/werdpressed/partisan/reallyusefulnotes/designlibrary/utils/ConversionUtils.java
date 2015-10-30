package com.werdpressed.partisan.reallyusefulnotes.designlibrary.utils;

public final class ConversionUtils {

    private ConversionUtils() { throw new AssertionError(); }

    public static boolean isPossibleToCastLongToInt(long value) {
        return ((value > Integer.MIN_VALUE) && (value < Integer.MAX_VALUE));
    }

    public static void isPossibleToCastLongToIntThrowsException(long value) {
        if (!isPossibleToCastLongToInt(value)) {
            throw new IllegalArgumentException(
                    "Long value must be between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE);
        }
    }
}
