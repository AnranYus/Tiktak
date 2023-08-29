package com.anryus.comment.service.client;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("service-user")
public interface UserClient {
    @GetMapping("/douyin/user/")
    Rest<User> getUserInfo(@RequestParam("user_id") long uid, @RequestParam("token")@Nullable String token);
}