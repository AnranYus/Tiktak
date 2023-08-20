package com.anryus.userservice.controller;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.utils.JwtUtils;
import com.anryus.userservice.entity.UserRest;
import com.anryus.userservice.service.UserService;
import com.anryus.userservice.utils.Jwt;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
public class UserController {

    final
    UserService userService;
    final
    Jwt jwt;

    final JwtUtils jwtUtils;

    public UserController(UserService userService, Jwt jwt,JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwt = jwt;
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
    public UserRest registerUser(@RequestParam("username")String username, @RequestParam("password")String password){
        User user = userService.register(username, password);

        if (user == null){
            return UserRest.fail("已经存在改用户");
        }

        String token = jwt.createToken(user.getUid(),"user");
        return UserRest.success("",user.getUid(),token);
    }

    @PostMapping("/douyin/user/login/")
    public UserRest loginUser(@RequestParam("username")String username, @RequestParam("password")String password){
        User user = userService.login(username, password);

        if (user!= null){
            String token = jwt.createToken(user.getUid(), "user");
            return UserRest.success("",user.getUid(),token);
        }

        return UserRest.fail("账户或密码错误");
    }

}
