package com.anryus.comment.service.client;

import com.anryus.common.entity.Rest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("service-feed")
public interface FeedClient {

    @PostMapping("/douyin/feed/comment")
    Rest<Object> commentAction(@RequestParam("video_id")Long videoId, @RequestParam("action")int action);
}
