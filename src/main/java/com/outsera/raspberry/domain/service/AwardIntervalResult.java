package com.outsera.raspberry.domain.service;

import com.outsera.raspberry.domain.model.AwardInterval;
import java.util.List;

public record AwardIntervalResult(List<AwardInterval> min, List<AwardInterval> max) {
}
