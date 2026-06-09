package com.outsera.raspberry.domain.service;

public record AwardInterval(String producer, int interval, int previousWin, int followingWin) {
}
