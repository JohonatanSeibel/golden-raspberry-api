package com.outsera.raspberry.application.dto;

import java.util.List;

public record AwardIntervalResponse(List<ProducerIntervalDTO> min, List<ProducerIntervalDTO> max) {
}
