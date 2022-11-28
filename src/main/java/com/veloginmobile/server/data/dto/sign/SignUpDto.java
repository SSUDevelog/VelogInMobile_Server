package com.veloginmobile.server.data.dto.sign;


import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignUpDto {

    @ApiParam(value = "ID", required = true)
    @NotBlank
    private String id;

    @ApiParam(value = "비밀번호", required = true)
    @NotBlank
    private String password;

    @ApiParam(value = "이름", required = true)
    @NotBlank
    private String name;

    @ApiParam(value = "권한", required = false)
    private String role;
}
