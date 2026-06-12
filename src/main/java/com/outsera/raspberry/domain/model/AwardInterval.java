package com.outsera.raspberry.domain.model;

public record AwardInterval(String producer, int interval, int previousWin, int followingWin) {
}
