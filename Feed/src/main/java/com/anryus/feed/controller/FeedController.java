package com.anryus.feed.controller;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.Video;
import com.anryus.feed.entity.FeedRest;
import com.anryus.feed.service.FeedService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@ResponseBody
public class FeedController {
    final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/douyin/feed/")
    public FeedRest getUserInfo(@RequestParam("latest_time")@Nullable Long latestTime, @RequestParam("token")@Nullable String token){
        long time;
        if (latestTime == null){
            Date date = new Date();
            time = date.getTime();
        }else {
            time = latestTime;
        }
        List<Video> videoByLatestTime = feedService.getVideoByLatestTime(time, token);

        return FeedRest.success("",time,videoByLatestTime);

    }
}
