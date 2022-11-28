package com.veloginmobile.server.service.impl;

import com.veloginmobile.server.common.CommonResponse;
import com.veloginmobile.server.common.exception.SignException;
import com.veloginmobile.server.config.security.JwtTokenProvider;
import com.veloginmobile.server.data.dto.sign.SignInDto;
import com.veloginmobile.server.data.dto.sign.SignInResultDto;
import com.veloginmobile.server.data.dto.sign.SignUpDto;
import com.veloginmobile.server.data.dto.sign.SignUpResultDto;
import com.veloginmobile.server.data.entity.User;
import com.veloginmobile.server.data.repository.UserRepository;
import com.veloginmobile.server.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SignServiceImpl implements SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;

    public SignServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SignUpResultDto signUp(SignUpDto signUpDto) throws SignException{
        LOGGER.info("[getSignUpResult] 회원 가입 정보 전달");
        User user;
        if(signUpDto.getRole().equalsIgnoreCase("admin")) {
            user = User.builder()
                    .uid(signUpDto.getId())
                    .name(signUpDto.getName())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .build();
        } else {
            user = User.builder()
                    .uid(signUpDto.getId())
                    .name(signUpDto.getName())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }

        User savedUser = userRepository.save(user);
        SignUpResultDto signUpResultDto = new SignInResultDto();

        LOGGER.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");
        if(!savedUser.getName().isEmpty()) {
            LOGGER.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signUpResultDto);
        } else {
            LOGGER.info("[getSignUpResult] 실패 처리 완료");
            setFailResult(signUpResultDto);
            throw new SignException(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
        }

        return signUpResultDto;
    }

    @Override
    public SignInResultDto signIn(SignInDto signInDto) throws SignException {
        LOGGER.info("[getSignInResult] signDataHandler로 회원 정보 요청");
        User user = userRepository.getByUid(signInDto.getId());
        LOGGER.info("[getSignInResult] Id : {}", signInDto.getId());

        LOGGER.info("[getSignInResult] 패스워드 비교 수행");
        if(!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {//각 익셉션에 대한 클래스 정리, 분류 필요
            throw new SignException(HttpStatus.BAD_REQUEST, "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.");
        }
        LOGGER.info("[getSignInResult] 패스워드 일치");

        LOGGER.info("[getSignInResult] SignInResult 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(user.getUid()), user.getRoles()))
                .build();

        LOGGER.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSuccessResult(signInResultDto);

        return signInResultDto;
    }

    private void setSuccessResult(SignUpResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(SignUpResultDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }
}
