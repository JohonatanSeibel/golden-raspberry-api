package com.outsera.raspberry.infrastructure.persistence;

import com.outsera.raspberry.domain.model.Movie;
import com.outsera.raspberry.domain.repository.MovieRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaMovieRepository extends JpaRepository<Movie, Long>, MovieRepository {

    @Override
    @Query("select distinct m from Movie m left join fetch m.producers where m.winner = true")
    List<Movie> findByWinnerTrue();
}
