package com.veloginmobile.server.controller;

import com.veloginmobile.server.data.dto.SubscribeRequestModel;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("subscribe")
public class SubscribeController {

    private SubscribeRequestModel subscribeRequestModel;

    private int openURL(String profileURL) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(profileURL).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return responseCode;
        } catch (IOException e) {
            return 0;
        }
    }

    @GetMapping("/inputname/{name}")
    @ResponseBody
    public Object subscribeInput(@PathVariable String name) throws IOException {

        this.subscribeRequestModel = new SubscribeRequestModel();
        String userProfileURL = "https://velog.io/@" + name;
        Document document = Jsoup.connect(userProfileURL).get();

        if (openURL(userProfileURL) == 404) {
            subscribeRequestModel.setValidate(Boolean.FALSE);
            subscribeRequestModel.setUserName("");
            return subscribeRequestModel;
        } else {
            subscribeRequestModel.setUserName(name);
            subscribeRequestModel.setValidate(Boolean.TRUE);
        }

        Elements profileImageURL = document.select("#root > div.sc-efQSVx.sc-cTAqQK.hKuDqm > div.sc-hiwPVj.cFguvd.sc-dkqQuH > div.sc-jlRLRk.itanDZ.sc-dwsnSq.cXXBgc > div.sc-dUbtfd.gBxoyd > a > img");
        subscribeRequestModel.setProfilePictureURL(profileImageURL.attr("src"));
        return subscribeRequestModel;
    }
}