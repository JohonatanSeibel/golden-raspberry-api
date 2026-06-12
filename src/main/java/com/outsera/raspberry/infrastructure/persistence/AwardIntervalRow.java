package com.outsera.raspberry.infrastructure.persistence;

public interface AwardIntervalRow {

    String getProducer();

    int getPreviousWin();

    int getFollowingWin();

    int getIntervalYears();
}
