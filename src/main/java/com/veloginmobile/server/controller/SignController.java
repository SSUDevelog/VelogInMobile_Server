package com.veloginmobile.server.controller;

import com.veloginmobile.server.common.exception.SignException;
import com.veloginmobile.server.data.dto.sign.*;
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
            @Validated @RequestBody SignInDto signInDto) throws SignException {//signInDto에 토큰도 묶어놔도 되는지 질문!
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
            @Validated @RequestBody SignUpDto signUpDto) throws SignException{
        //user고정
        signUpDto.setRole("");
        LOGGER.info("[signUp] 회원가입을 수행합니다. id : {}, pw : ****, name : {}, role : {}", signUpDto.getId(), signUpDto.getName(), signUpDto.getRole());
        SignUpResultDto signUpResultDto = signService.signUp(signUpDto);

        LOGGER.info("[signUp] 회원가입을 완료했습니다. id : {}", signUpDto.getId());
        return signUpResultDto;
    }

    @PostMapping(value = "/sign-out")
    public SignOutResultDto signOut(
            @Validated @RequestBody SignOutDto signOutDto) throws SignException {
        LOGGER.info("[signIn] 회원탈퇴를 시도하고 있습니다. id : {}, pw : ****", signOutDto.getId());
        SignOutResultDto signOutResultDto = signService.signOut(signOutDto);

        if(signOutResultDto.getCode() == 0) {
            LOGGER.info("[signIn] 정상적으로 회원 탈퇴 되었습니다. id : {}, token : {}", signOutDto.getId());
            notificationService.outGroup("AllGroup", signOutDto.getFcmToken());
        }
        return signOutResultDto;
    }
}
