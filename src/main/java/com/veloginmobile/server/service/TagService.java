package com.veloginmobile.server.service;

import com.veloginmobile.server.common.exception.TagException;
import com.veloginmobile.server.data.dto.tag.TagPostDto;
import com.veloginmobile.server.data.dto.tag.TagPostResultDto;

import java.io.IOException;
import java.util.List;

public interface TagService {
    List<TagPostDto> getTagPosts(String tag) throws IOException, TagException;

    TagPostResultDto getTagsPost(List<String> tags) throws IOException, TagException;

    TagPostResultDto getTagsPost(String uid) throws IOException, TagException;

    void addTag(String uid, String tag) throws TagException;

    void deleteTag(String uid, String tag) throws TagException;

    List<String> getTags(String userName) throws TagException;
}
