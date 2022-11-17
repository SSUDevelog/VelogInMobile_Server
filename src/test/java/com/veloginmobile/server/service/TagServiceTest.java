package com.veloginmobile.server.service;

import com.veloginmobile.server.data.dto.tag.TagPostDto;
import com.veloginmobile.server.data.dto.tag.TagPostResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TagServiceTest {
    @Autowired
    private TagService tagService;

    @Test
    void getSubscriberPostsTest() throws IOException {
        List<TagPostDto> tagPostDto = tagService.getTagPosts("코딩테스트");

        System.out.println(tagPostDto);
    }

    @Test
    void sortSubscribersPostTest() throws IOException {
        List<String> tags = new ArrayList<>();
        tags.add("게임");
        tags.add("코딩");
        tags.add("알고리즘");

        TagPostResultDto tagPostResultDto = tagService.getTagsPost(tags);
        System.out.println(tagPostResultDto);
    }
}
