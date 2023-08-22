package com.anryus.userservice.controller;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.utils.JwtUtils;
import com.anryus.userservice.service.UserService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
public class UserController {

    final
    UserService userService;

    final JwtUtils jwtUtils;

    public UserController(UserService userService,JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }


    @GetMapping("/douyin/user/")
    public Rest<User> getUserInfo(@RequestParam("user_id") long uid, @RequestParam("token")@Nullable String token){
        if (uid == 0){
            String uidStr = jwtUtils.verify(token).get("uid");
            if (uidStr!=null){
                uid = Long.parseLong(uidStr);
            }else {
                return Rest.fail("不存在该用户");
            }
        }

        User userByUid = userService.getUserByUid(uid);
        if (userByUid != null){
            return Rest.success("成功","user" ,userByUid);
        }else {
            return Rest.fail("获取用户信息失败");
        }
    }

    @PostMapping("/douyin/user/register/")
    public Rest<String> registerUser(@RequestParam("username")String username, @RequestParam("password")String password){
        User user = userService.register(username, password);

        if (user == null){
            return Rest.fail("已经存在改用户");
        }

        return userService.createToken(user.getUid(),"user");
    }

    @PostMapping("/douyin/user/login/")
    public Rest<String> loginUser(@RequestParam("username")String username, @RequestParam("password")String password){
        User user = userService.login(username, password);

        if (user!= null){
            return userService.createToken(user.getUid(),"user");
        }

        return Rest.fail("账户或密码错误");
    }

    @PostMapping("/douyin/user/follow/")
    public Rest<String> followAction(@RequestParam("user_id")long uid,@RequestParam("action")int action){
        User user = userService.followAction(uid, action);
        if (user != null){
            return Rest.success("");
        }else {
            return Rest.fail("");
        }
    }

    @PostMapping("/douyin/user/unfollow/")
    public Rest<String> unfollowAction(@RequestParam("user_id")long uid,@RequestParam("action")int action){
        User user = userService.unfollowAction(uid, action);
        if (user != null){
            return Rest.success("");
        }else {
            return Rest.fail("");
        }
    }



}
