package com.outsera.raspberry.api;

import com.outsera.raspberry.application.ProducerAwardService;
import com.outsera.raspberry.application.dto.AwardIntervalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/producers")
@Tag(name = "Producers")
public class ProducerController {

    private final ProducerAwardService producerAwardService;

    public ProducerController(ProducerAwardService producerAwardService) {
        this.producerAwardService = producerAwardService;
    }

    @GetMapping("/award-intervals")
    @Operation(summary = "Produtores com menor e maior intervalo entre prêmios consecutivos")
    public AwardIntervalResponse getAwardIntervals() {
        return producerAwardService.getAwardIntervals();
    }
}
