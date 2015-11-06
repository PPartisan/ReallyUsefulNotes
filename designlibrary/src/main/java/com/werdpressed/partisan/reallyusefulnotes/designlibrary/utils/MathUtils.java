package com.werdpressed.partisan.reallyusefulnotes.designlibrary.utils;

public final class MathUtils {

    private MathUtils() { throw new AssertionError(); }

    public static double findHypoteneuse(double adj, double opp) {
        return Math.sqrt((adj*adj) + (opp*opp));
    }

}
