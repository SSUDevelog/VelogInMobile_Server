package com.veloginmobile.server.controller;

import com.veloginmobile.server.data.dto.sign.SignInDto;
import com.veloginmobile.server.data.dto.sign.SignInResultDto;
import com.veloginmobile.server.data.dto.sign.SignUpDto;
import com.veloginmobile.server.data.dto.sign.SignUpResultDto;
import com.veloginmobile.server.service.NotificationService;
import com.veloginmobile.server.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sign-api")
public class SignController {

    private final Logger LOGGER = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;
    private final NotificationService notificationService;

    public SignController(SignService signService, NotificationService notificationService) {
        this.signService = signService;
        this.notificationService = notificationService;
    }

    @PostMapping(value = "/sign-in")
    public SignInResultDto signIn(
            @Validated @RequestBody SignInDto signInDto) throws RuntimeException {//signInDto에 토큰도 묶어놔도 되는지 질문!
        LOGGER.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", signInDto.getId());
        SignInResultDto signInResultDto = signService.signIn(signInDto);

        if(signInResultDto.getCode() == 0) {
            LOGGER.info("[signIn] 정상적으로 로그인 되었습니다. id : {}, token : {}", signInDto.getId(), signInResultDto.getToken());
            notificationService.joinGroup("AllGroup", signInDto.getFcmToken());//로그아웃할때는 제거하는 기능 추가하기
        }
        return signInResultDto;
    }

    @PostMapping(value = "/sign-up")
    public SignUpResultDto signUp(
            @Validated @RequestBody SignUpDto signUpDto) {
        LOGGER.info("[signUp] 회원가입을 수행합니다. id : {}, pw : ****, name : {}, role : {}", signUpDto.getId(), signUpDto.getName(), signUpDto.getRole());
        SignUpResultDto signUpResultDto = signService.signUp(signUpDto);

        LOGGER.info("[signUp] 회원가입을 완료했습니다. id : {}", signUpDto.getId());
        return signUpResultDto;
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOGGER.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        Map<String,String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }
}
