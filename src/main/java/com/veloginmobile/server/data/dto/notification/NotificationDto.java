package com.veloginmobile.server.data.dto.notification;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationDto {
    @ApiParam(value = "Title", required = true)
    @NotBlank
    private String title;

    @ApiParam(value = "Body", required = true)
    @NotBlank
    private String body;

    @ApiParam(value = "Link", required = true)
    @NotBlank
    private String link;
}
