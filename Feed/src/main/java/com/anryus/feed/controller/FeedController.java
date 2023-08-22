package com.anryus.feed.controller;

import com.anryus.common.entity.Favorite;
import com.anryus.common.entity.Rest;
import com.anryus.common.entity.Video;
import com.anryus.common.entity.VideoDTO;
import com.anryus.feed.service.FeedService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
public class FeedController {
    final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/douyin/feed")
    public Rest<Object> getUserInfo(@RequestParam("latest_time")@Nullable Long latestTime, @RequestParam("token")@Nullable String token){
        long time;
        if (latestTime == null || latestTime == 0){
            Date date = new Date();
            time = date.getTime();
        }else {
            time = latestTime;
        }
        List<VideoDTO> videoByLatestTime = feedService.getVideoByLatestTime(time, token);
        Map<String,Object> map = new HashMap<>();
        map.put("next_time",time);
        map.put("video_list",videoByLatestTime);

        return Rest.success("",map);

    }


    @PostMapping("/douyin/feed/favorite")
    public Rest<Object> favoriteAction(@RequestParam("video_id")Long videoId,@RequestParam("action")int action){
        Video video = feedService.favoriteAction(videoId, action);
        if (video != null){
            return Rest.success("");
        }else {
            return Rest.fail("");
        }
    }

    @PostMapping("/douyin/feed/comment")
    public Rest<Object> commentAction(@RequestParam("video_id")Long videoId,@RequestParam("action")int action){
        Video video = feedService.commentAction(videoId, action);
        if (video != null){
            return Rest.success("");
        }else {
            return Rest.fail("");
        }
    }

    @PostMapping("/douyin/feed/videos")
    public Rest<List<VideoDTO>> favoriteVideos(@RequestBody List<Favorite> favorites){
        List<VideoDTO> favoriteVideo = feedService.getFavoriteVideo(favorites);
        return Rest.success("","video_list",favoriteVideo);
    }
}
