package com.outsera.raspberry.application.dto;

public record ProducerIntervalDTO(String producer, int interval, int previousWin, int followingWin) {
}
