package com.outsera.raspberry.infrastructure.csv;

import com.outsera.raspberry.domain.model.Movie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class MovieCsvParser {

    private static final String COLUMN_DELIMITER = ";";
    private static final Pattern PRODUCER_DELIMITER = Pattern.compile("\\s*,\\s*|\\s+and\\s+");
    private static final String WINNER_FLAG = "yes";
    private static final int YEAR_INDEX = 0;
    private static final int TITLE_INDEX = 1;
    private static final int STUDIOS_INDEX = 2;
    private static final int PRODUCERS_INDEX = 3;
    private static final int WINNER_INDEX = 4;
    private static final int MINIMUM_COLUMNS = 4;

    public Optional<Movie> parse(String line) {
        if (line == null || line.isBlank()) {
            return Optional.empty();
        }
        String[] columns = line.split(COLUMN_DELIMITER, -1);
        if (columns.length < MINIMUM_COLUMNS) {
            return Optional.empty();
        }
        Optional<Integer> releaseYear = parseYear(columns[YEAR_INDEX]);
        if (releaseYear.isEmpty()) {
            return Optional.empty();
        }
        Movie movie = new Movie(
                releaseYear.get(),
                columns[TITLE_INDEX].trim(),
                columns[STUDIOS_INDEX].trim(),
                isWinner(columns),
                parseProducers(columns[PRODUCERS_INDEX]));
        return Optional.of(movie);
    }

    private Optional<Integer> parseYear(String value) {
        try {
            return Optional.of(Integer.parseInt(value.trim()));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private boolean isWinner(String[] columns) {
        if (columns.length <= WINNER_INDEX) {
            return false;
        }
        return WINNER_FLAG.equalsIgnoreCase(columns[WINNER_INDEX].trim());
    }

    private List<String> parseProducers(String value) {
        List<String> producers = new ArrayList<>();
        for (String candidate : PRODUCER_DELIMITER.split(value)) {
            String producer = candidate.trim();
            if (!producer.isEmpty()) {
                producers.add(producer);
            }
        }
        return producers;
    }
}
