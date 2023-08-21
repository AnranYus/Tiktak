package com.anryus.publish.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient( value ="service-favorite")
@Component
public interface FavoriteClient {

    @GetMapping("/douyin/favorite/is")
    boolean isFavorite(@RequestParam("user_id")Long userId,@RequestParam("video_id") Long videoId,@RequestParam("token")@Nullable String token);
}
