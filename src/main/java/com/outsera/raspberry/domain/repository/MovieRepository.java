package com.outsera.raspberry.domain.repository;

import com.outsera.raspberry.domain.model.AwardInterval;
import java.util.List;

public interface MovieRepository {

    List<AwardInterval> findConsecutiveAwardIntervals();
}
