package com.veloginmobile.server.controller;

import com.veloginmobile.server.common.exception.TagException;
import com.veloginmobile.server.config.security.JwtTokenProvider;
import com.veloginmobile.server.data.dto.tag.TagPostResultDto;
import com.veloginmobile.server.service.TagService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("tag")
public class TagController {

    private final JwtTokenProvider jwtTokenProvider;
    private final TagService tagService;

    private final Logger LOGGER = LoggerFactory.getLogger(TagController.class);

    public TagController(JwtTokenProvider jwtTokenProvider, TagService tagService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tagService = tagService;
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping(value = "/gettag")
    public ResponseEntity<List<String>> getTag(@RequestHeader("X-AUTH-TOKEN") String token) throws TagException {//제네릭 안의 제네릭 -> 별도응답 객체를 만들어야함!
        List<String> tags = new ArrayList<>();
        try {
            String userName = jwtTokenProvider.getUsername(token);

            tags = tagService.getTags(userName);

        } catch (RuntimeException e) {
            LOGGER.error("불러올 태그가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tags);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @PostMapping(value = "/addtag")
    public ResponseEntity<String> addTag(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam String tag) throws TagException {
        String userName = jwtTokenProvider.getUsername(token);

        tagService.addTag(userName, tag);

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @DeleteMapping(value = "/deletetag")
    public ResponseEntity<String> deleteTag(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam String tag) throws TagException {
        String userName = jwtTokenProvider.getUsername(token);

        tagService.deleteTag(userName, tag);

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping(value = "/tagpost")//responseentity가 없어도 되는지 테스트 되는지 확인해보기//403 402같은 코드를 던질경우 쓰기
    public ResponseEntity<TagPostResultDto> getTagPost(@RequestHeader("X-AUTH-TOKEN") String token) throws IOException, TagException {
        String userName = jwtTokenProvider.getUsername(token);

        TagPostResultDto tagPostResultDto = tagService.getTagsPost(userName);

        return ResponseEntity.status(HttpStatus.OK).body(tagPostResultDto);
    }
}
