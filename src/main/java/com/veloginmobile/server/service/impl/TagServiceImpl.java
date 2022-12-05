package com.veloginmobile.server.service.impl;

import com.veloginmobile.server.common.exception.TagException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public List<TagPostDto> getTagPosts(String tag) throws IOException, TagException {

        String tagUrl = "https://velog.io/tags/" + tag;
        Document doc = Jsoup.connect(tagUrl).get();

        List<TagPostDto> subscribePostDtos = new ArrayList<>();

        Elements posts = doc.select("#root > div > main > div > div").get(1).select("> div");

        for (Element post : posts) {
            try {
                TagPostDto tagPostDto = tagPostFactory(post);

                Elements tags = post.select(".tags-wrapper a");
                for (Element _tag : tags) {
                    tagPostDto.getTag().add(_tag.text());
                }

                subscribePostDtos.add(tagPostDto);
            } catch (RuntimeException e){
                throw new TagException(HttpStatus.ACCEPTED, "불러올 포스트가 없거나 문서 구조가 변경되었습니다.");
            }
<<<<<<< HEAD
=======

            subscribePostDtos.add(tagPostDto);
        }
        if (subscribePostDtos.isEmpty()) {
            throw new TagException(HttpStatus.ACCEPTED, "불러올 포스트가 없습니다.");
>>>>>>> fff13f743cac4608d5420cb0011c594ce106969d
        }

        return subscribePostDtos;
    }

    private static TagPostDto tagPostFactory(Element post) {
        TagPostDto tagPostDto = new TagPostDto();

        tagPostDto.setName(post.select(".user-info .username a").text());
        tagPostDto.setTitle(post.select("a h2").text());
        tagPostDto.setSummary(post.select("p").text());
        tagPostDto.setDate(post.select(".subinfo span").get(0).text());
        tagPostDto.setComment(Integer.parseInt(post.select(".subinfo span").get(1).text().replace("개의 댓글", "")));
        tagPostDto.setLike(Integer.parseInt(post.select(".subinfo span").get(2).text()));
        tagPostDto.setImg(post.select("a div img").attr("src"));
        tagPostDto.setUrl(post.select("> a").attr("href"));
        return tagPostDto;
    }

    @Override
    public TagPostResultDto getTagsPost(List<String> tags) throws IOException, TagException {

        TagPostResultDto tagPostResultDto = new TagPostResultDto();
        for (String tag : tags) {
            try{
            List<TagPostDto> tagPostDtos = getTagPosts(tag);
            tagPostResultDto.getTagPostDtoList().addAll(tagPostDtos);
            } catch (TagException e) { }
        }
        if(tagPostResultDto.getTagPostDtoList().isEmpty()){
            throw new TagException(HttpStatus.ACCEPTED, "불러올 포스트가 없거나 문서 구조가 변경되었습니다.");
        }

        Collections.sort(tagPostResultDto.getTagPostDtoList(), TagPostDto.compareByDate);
        return tagPostResultDto;
    }

    @Override
    public TagPostResultDto getTagsPost(String uid) throws IOException, TagException {
        User user = userRepository.getByUid(uid);
        Tag tag = tagRepository.findByUser(user);
        List<String> tags = tag.getTags();

        return getTagsPost(tags);
    }

    @Override
    public void addTag(String uid, String tag) throws TagException {

        User user = userRepository.getByUid(uid);
        Tag tags = tagRepository.findByUser(user);
        if (tags == null) {
            tags = makeTag(user);
        }
        if(tags.getTags().contains(tag)){
            throw new TagException(HttpStatus.BAD_REQUEST, "이미 추가한 관심태그입니다.");
        }

        tags.getTags().add(tag);
        tagRepository.save(tags);
    }

    @Override
    public void deleteTag(String uid, String tag) throws TagException {

        User user = userRepository.getByUid(uid);
        Tag tags = tagRepository.findByUser(user);
        if (tags == null) {
            tags = makeTag(user);
        }
        if(!tags.getTags().contains(tag)){
            throw new TagException(HttpStatus.BAD_REQUEST, "목록에 해당 태그가 없습니다.");
        }

        tags.getTags().remove(tag);
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
