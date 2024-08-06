package com.sokpulee.crescendo.domain.alarm.dto;

public enum AlarmType {
    FOLLOW(1L);

    private final Long id;

    AlarmType(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
