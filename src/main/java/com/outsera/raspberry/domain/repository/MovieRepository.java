package com.outsera.raspberry.domain.repository;

import com.outsera.raspberry.domain.model.Movie;
import java.util.List;

public interface MovieRepository {

    List<Movie> findByWinnerTrue();

    long count();
}
