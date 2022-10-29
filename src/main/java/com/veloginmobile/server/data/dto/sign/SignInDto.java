package com.veloginmobile.server.data.dto.sign;

import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignInDto {

    @ApiParam(value = "ID", required = true)
    @NotBlank
    private String id;

    @ApiParam(value = "Password", required = true)
    @NotBlank
    private String password;
}