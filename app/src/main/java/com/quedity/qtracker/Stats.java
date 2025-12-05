package com.quedity.qtracker;

import java.util.List;

/**
 * Utilities to compute average, mean absolute deviation, and scores.
 */
public class Stats {

    public static double mean(List<Double> list, int length) {
        if (list == null || list.isEmpty()) return 0.0;
        int size = list.size();
        int start = Math.max(0, size - length);
        double sum = 0;
        int count = 0;
        for (int i = start; i < size; i++) {
            sum += list.get(i);
            count++;
        }
        return count == 0 ? 0 : sum / count;
    }

    // mean absolute deviation around mean (for the considered window)
    public static double meanAbsoluteDeviation(List<Double> list, int length) {
        if (list == null || list.isEmpty()) return 0.0;
        int size = list.size();
        int start = Math.max(0, size - length);
        double avg = mean(list, length);
        double sumAbs = 0;
        int count = 0;
        for (int i = start; i < size; i++) {
            sumAbs += Math.abs(list.get(i) - avg);
            count++;
        }
        return count == 0 ? 0 : sumAbs / count;
    }

    public static double paramScore(List<Double> list, int length) {
        if (list == null || list.isEmpty()) return 0.0;
        double avg = mean(list, length);
        double mad = meanAbsoluteDeviation(list, length);
        double last = list.get(list.size() - 1);
        if (Math.abs(mad) < 1e-9) return 0.0;
        return (last - avg) / mad;
    }
}