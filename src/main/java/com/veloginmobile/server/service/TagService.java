package com.veloginmobile.server.service;

import com.veloginmobile.server.data.dto.tag.TagPostDto;
import com.veloginmobile.server.data.dto.tag.TagPostResultDto;

import java.io.IOException;
import java.util.List;

public interface TagService {
    List<TagPostDto> getTagPosts(String tag) throws IOException;

    TagPostResultDto getTagsPost(List<String> tags) throws IOException;

    TagPostResultDto getTagsPost(String uid) throws IOException;

    void addTag(String uid, String tag);

    List<String> getTags(String userName);
}
