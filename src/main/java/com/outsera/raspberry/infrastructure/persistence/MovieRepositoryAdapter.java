package com.outsera.raspberry.infrastructure.persistence;

import com.outsera.raspberry.domain.model.AwardInterval;
import com.outsera.raspberry.domain.repository.MovieRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MovieRepositoryAdapter implements MovieRepository {

    private final JpaMovieRepository jpaMovieRepository;

    public MovieRepositoryAdapter(JpaMovieRepository jpaMovieRepository) {
        this.jpaMovieRepository = jpaMovieRepository;
    }

    @Override
    public List<AwardInterval> findConsecutiveAwardIntervals() {
        return jpaMovieRepository.findConsecutiveAwardIntervals().stream()
                .map(row -> new AwardInterval(
                        row.getProducer(),
                        row.getIntervalYears(),
                        row.getPreviousWin(),
                        row.getFollowingWin()))
                .toList();
    }
}
