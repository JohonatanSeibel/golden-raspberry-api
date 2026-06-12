package com.outsera.raspberry.domain.service;

import com.outsera.raspberry.domain.model.AwardInterval;
import java.util.Comparator;
import java.util.List;

public class AwardIntervalCalculator {

    public AwardIntervalResult calculate(List<AwardInterval> intervals) {
        if (intervals.isEmpty()) {
            return new AwardIntervalResult(List.of(), List.of());
        }

        int minInterval = Integer.MAX_VALUE;
        int maxInterval = Integer.MIN_VALUE;
        for (AwardInterval interval : intervals) {
            minInterval = Math.min(minInterval, interval.interval());
            maxInterval = Math.max(maxInterval, interval.interval());
        }

        return new AwardIntervalResult(
                selectByInterval(intervals, minInterval),
                selectByInterval(intervals, maxInterval));
    }

    private List<AwardInterval> selectByInterval(List<AwardInterval> intervals, int targetInterval) {
        return intervals.stream()
                .filter(interval -> interval.interval() == targetInterval)
                .sorted(Comparator.comparingInt(AwardInterval::previousWin)
                        .thenComparing(AwardInterval::producer))
                .toList();
    }
}
