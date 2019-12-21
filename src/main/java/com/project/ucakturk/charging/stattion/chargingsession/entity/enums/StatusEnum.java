package com.project.ucakturk.charging.stattion.chargingsession.entity.enums;

public enum StatusEnum {

    IN_PROGRESS("IN_PROGRESS"), FINISHED("FINISHED");

    private String value;

    StatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
