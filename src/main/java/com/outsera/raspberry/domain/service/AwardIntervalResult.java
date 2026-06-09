package com.outsera.raspberry.domain.service;

import java.util.List;

public record AwardIntervalResult(List<AwardInterval> min, List<AwardInterval> max) {
}
