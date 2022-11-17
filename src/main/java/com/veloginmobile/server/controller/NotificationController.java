package com.veloginmobile.server.controller;

import com.veloginmobile.server.data.dto.notification.NotificationDto;
import com.veloginmobile.server.service.NotificationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notification")//나중에 관리자 권한으로만 실행되게끔 설정!
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //방송
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @PostMapping(value = "/broadcast")
    public ResponseEntity<String> broadcast(@Validated @RequestBody NotificationDto notificationDto) {

        notificationService.sendByGroupName("AllGroup", notificationDto);

        return ResponseEntity.status(HttpStatus.OK).body("temp");//임시
    }
}
