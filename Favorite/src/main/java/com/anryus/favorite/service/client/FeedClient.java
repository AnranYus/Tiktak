package com.anryus.favorite.service.client;

import com.anryus.common.entity.Favorite;
import com.anryus.common.entity.Rest;
import com.anryus.common.entity.VideoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient("service-feed")
public interface FeedClient {
    @PostMapping("/douyin/feed/favorite")
    Rest<Object> favoriteAction(@RequestParam("video_id")Long videoId, @RequestParam("action")int action);

    @PostMapping("/douyin/feed/videos")
    Rest<List<VideoDTO>> favoriteVideos(@RequestBody List<Favorite> favorites);
}
