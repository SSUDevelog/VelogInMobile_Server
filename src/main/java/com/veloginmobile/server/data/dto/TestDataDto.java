package com.veloginmobile.server.data.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TestDataDto {

    private String name;
    private int price;
    private int stock;
}
