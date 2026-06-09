package com.outsera.raspberry.application;

import com.outsera.raspberry.application.dto.AwardIntervalResponse;
import com.outsera.raspberry.application.dto.ProducerIntervalDTO;
import com.outsera.raspberry.domain.service.AwardInterval;
import com.outsera.raspberry.domain.service.AwardIntervalResult;
import java.util.List;

public class AwardIntervalMapper {

    public AwardIntervalResponse toResponse(AwardIntervalResult result) {
        return new AwardIntervalResponse(toDtoList(result.min()), toDtoList(result.max()));
    }

    private List<ProducerIntervalDTO> toDtoList(List<AwardInterval> intervals) {
        return intervals.stream().map(this::toDto).toList();
    }

    private ProducerIntervalDTO toDto(AwardInterval interval) {
        return new ProducerIntervalDTO(
                interval.producer(),
                interval.interval(),
                interval.previousWin(),
                interval.followingWin());
    }
}
