package com.veloginmobile.server.service.impl;

import com.veloginmobile.server.data.dto.tag.TagPostDto;
import com.veloginmobile.server.data.dto.tag.TagPostResultDto;
import com.veloginmobile.server.data.entity.Tag;
import com.veloginmobile.server.data.entity.User;
import com.veloginmobile.server.data.repository.TagRepository;
import com.veloginmobile.server.data.repository.UserRepository;
import com.veloginmobile.server.service.TagService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    TagRepository tagRepository;
    UserRepository userRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TagPostDto> getTagPosts(String tag) throws IOException {

        String userProfileURL = "https://velog.io/tags/" + tag;
        Document doc = Jsoup.connect(userProfileURL).get();

        List<TagPostDto> subscribePostDtos = new ArrayList<>();

        Elements posts = doc.select("#root > div > main > div > div").get(1).select("> div");

        for (Element post : posts) {

            TagPostDto tagPostDto = new TagPostDto();

            tagPostDto.setName(post.select(".user-info .username a").text());
            tagPostDto.setTitle(post.select("a h2").text());
            tagPostDto.setSummary(post.select("p").text());
            tagPostDto.setDate(post.select(".subinfo span").get(0).text());
            tagPostDto.setComment(Integer.parseInt(post.select(".subinfo span").get(1).text().replace("개의 댓글", "")));
            tagPostDto.setLike(Integer.parseInt(post.select(".subinfo span").get(2).text()));
            tagPostDto.setImg(post.select("a div img").attr("src"));
            tagPostDto.setUrl(post.select("> a").attr("href"));

            Elements tags = post.select(".tags-wrapper a");
            for (Element _tag : tags) {
                tagPostDto.getTag().add(_tag.text());
            }

            subscribePostDtos.add(tagPostDto);
        }

        return subscribePostDtos;
    }

    @Override
    public TagPostResultDto getTagsPost(List<String> tags) throws IOException {

        TagPostResultDto tagPostResultDto = new TagPostResultDto();

        for(String tag : tags) {
            List<TagPostDto> tagPostDtos = getTagPosts(tag);
            tagPostResultDto.getTagPostDtoList().addAll(tagPostDtos);
        }
        Collections.sort(tagPostResultDto.getTagPostDtoList(), TagPostDto.compareByDate);

        return tagPostResultDto;
    }

    @Override
    public TagPostResultDto getTagsPost(String uid) throws IOException {
        User user = userRepository.getByUid(uid);
        Tag tag = tagRepository.findByUser(user);
        List<String> tags = tag.getTags();

        return getTagsPost(tags);
    }

    @Override
    public void addTag(String uid, String tag) {

        User user = userRepository.getByUid(uid);
        Tag tags = tagRepository.findByUser(user);
        if (tags == null) {
            tags = makeTag(user);
        }

        tags.getTags().add(tag);
        tagRepository.save(tags);
    }

    @Override
    public List<String> getTags(String userName) {

        User user = userRepository.getByUid(userName);
        Tag tag = tagRepository.findByUser(user);

        return tag.getTags();
    }

    private Tag makeTag(User user) {

        Tag tag = new Tag();

        tag.setUser(user);
        return tagRepository.save(tag);
    }
}
