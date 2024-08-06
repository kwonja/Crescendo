package com.sokpulee.crescendo.domain.alarm.dto;

public enum AlarmType {
    FOLLOW(1L, "FOLLOW");

    private final Long id;
    private final String name;


    AlarmType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
