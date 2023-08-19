package com.anryus.favorite.service.client;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("service-publish")
public interface PublishClient {
    @GetMapping("/douyin/publish/video")
    Rest<Video> getVideo(@RequestParam("video_id")Long videoId);
}
