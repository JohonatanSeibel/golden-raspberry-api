package com.outsera.raspberry.infrastructure.persistence;

import com.outsera.raspberry.domain.model.Movie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaMovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = """
            SELECT gaps.producer AS producer,
                   gaps.previous_win AS previousWin,
                   gaps.following_win AS followingWin,
                   gaps.following_win - gaps.previous_win AS intervalYears
            FROM (
                SELECT mp.producer AS producer,
                       m.release_year AS following_win,
                       LAG(m.release_year) OVER (
                           PARTITION BY mp.producer ORDER BY m.release_year
                       ) AS previous_win
                FROM movies m
                JOIN movie_producers mp ON mp.movie_id = m.id
                WHERE m.winner = TRUE
            ) gaps
            WHERE gaps.previous_win IS NOT NULL
            ORDER BY intervalYears, previousWin, producer
            """, nativeQuery = true)
    List<AwardIntervalRow> findConsecutiveAwardIntervals();
}
