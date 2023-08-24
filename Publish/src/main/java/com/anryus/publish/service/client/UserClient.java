package com.anryus.publish.service.client;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient( value ="service-user")
@Component
public interface UserClient {
    @GetMapping("/douyin/user/")
    Rest<User> getUserInfo(@RequestParam("user_id") long uid, @RequestParam("token")@Nullable String token);

    @PostMapping("/douyin/user/work_count")
    Rest<User> updateUserInfo(@RequestHeader("user_id")Long uid);

}
