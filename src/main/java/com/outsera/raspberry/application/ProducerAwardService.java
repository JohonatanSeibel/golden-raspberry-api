package com.outsera.raspberry.application;

import com.outsera.raspberry.application.dto.AwardIntervalResponse;
import com.outsera.raspberry.domain.model.AwardInterval;
import com.outsera.raspberry.domain.repository.MovieRepository;
import com.outsera.raspberry.domain.service.AwardIntervalCalculator;
import com.outsera.raspberry.domain.service.AwardIntervalResult;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProducerAwardService {

    private final MovieRepository movieRepository;
    private final AwardIntervalCalculator awardIntervalCalculator;
    private final AwardIntervalMapper awardIntervalMapper;

    public ProducerAwardService(MovieRepository movieRepository,
                                AwardIntervalCalculator awardIntervalCalculator,
                                AwardIntervalMapper awardIntervalMapper) {
        this.movieRepository = movieRepository;
        this.awardIntervalCalculator = awardIntervalCalculator;
        this.awardIntervalMapper = awardIntervalMapper;
    }

    public AwardIntervalResponse getAwardIntervals() {
        List<AwardInterval> intervals = movieRepository.findConsecutiveAwardIntervals();
        AwardIntervalResult result = awardIntervalCalculator.calculate(intervals);
        return awardIntervalMapper.toResponse(result);
    }
}
