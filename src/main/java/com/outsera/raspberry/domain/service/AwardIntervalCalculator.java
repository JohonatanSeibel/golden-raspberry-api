package com.outsera.raspberry.domain.service;

import com.outsera.raspberry.domain.model.Movie;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AwardIntervalCalculator {

    public AwardIntervalResult calculate(List<Movie> winners) {
        Map<String, List<Integer>> winYearsByProducer = groupWinYearsByProducer(winners);
        List<AwardInterval> intervals = buildConsecutiveIntervals(winYearsByProducer);

        if (intervals.isEmpty()) {
            return new AwardIntervalResult(List.of(), List.of());
        }

        int minInterval = intervals.stream().mapToInt(AwardInterval::interval).min().getAsInt();
        int maxInterval = intervals.stream().mapToInt(AwardInterval::interval).max().getAsInt();

        return new AwardIntervalResult(
                selectByInterval(intervals, minInterval),
                selectByInterval(intervals, maxInterval));
    }

    private Map<String, List<Integer>> groupWinYearsByProducer(List<Movie> winners) {
        Map<String, List<Integer>> winYearsByProducer = new LinkedHashMap<>();
        for (Movie movie : winners) {
            for (String producer : movie.getProducers()) {
                winYearsByProducer
                        .computeIfAbsent(producer, key -> new ArrayList<>())
                        .add(movie.getReleaseYear());
            }
        }
        return winYearsByProducer;
    }

    private List<AwardInterval> buildConsecutiveIntervals(Map<String, List<Integer>> winYearsByProducer) {
        List<AwardInterval> intervals = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : winYearsByProducer.entrySet()) {
            List<Integer> years = new ArrayList<>(entry.getValue());
            if (years.size() < 2) {
                continue;
            }
            years.sort(Comparator.naturalOrder());
            for (int index = 1; index < years.size(); index++) {
                int previousWin = years.get(index - 1);
                int followingWin = years.get(index);
                intervals.add(new AwardInterval(
                        entry.getKey(),
                        followingWin - previousWin,
                        previousWin,
                        followingWin));
            }
        }
        return intervals;
    }

    private List<AwardInterval> selectByInterval(List<AwardInterval> intervals, int targetInterval) {
        return intervals.stream()
                .filter(interval -> interval.interval() == targetInterval)
                .sorted(Comparator.comparingInt(AwardInterval::previousWin)
                        .thenComparing(AwardInterval::producer))
                .toList();
    }
}
