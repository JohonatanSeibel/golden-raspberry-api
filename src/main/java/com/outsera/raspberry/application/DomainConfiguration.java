package com.outsera.raspberry.application;

import com.outsera.raspberry.domain.service.AwardIntervalCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public AwardIntervalCalculator awardIntervalCalculator() {
        return new AwardIntervalCalculator();
    }

    @Bean
    public AwardIntervalMapper awardIntervalMapper() {
        return new AwardIntervalMapper();
    }
}
