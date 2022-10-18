package com.veloginmobile.server.data.dto;

public class ChangeTestDataNameDto {

    private Long number;

    private String name;

    public ChangeTestDataNameDto(Long number, String name) {
        this.number = number;
        this.name = name;
    }

    public ChangeTestDataNameDto() {}

    public Long getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }
}
