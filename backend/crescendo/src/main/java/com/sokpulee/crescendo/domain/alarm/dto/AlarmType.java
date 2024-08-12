package com.sokpulee.crescendo.domain.alarm.dto;

public enum AlarmType {
    FOLLOW(1L),
    FEED(2L),
    CHALLENGE(3L),
    FANART(4L),
    GOODS(5L);


    private final Long id;

    AlarmType(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
