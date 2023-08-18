package com.anryus.userservice.controller;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.userservice.entity.UserRest;
import com.anryus.userservice.service.UserService;
import com.anryus.userservice.utils.Jwt;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@ResponseBody
public class UserController {

    final
    UserService userService;
    final
    Jwt jwt;

    public UserController(UserService userService, Jwt jwt) {
        this.userService = userService;
        this.jwt = jwt;
    }


    @GetMapping("/douyin/user/")
    public Rest<User> getUserInfo(@RequestParam("user_id") long uid, @RequestParam("token")@Nullable String token){

        User userByUid = userService.getUserByUid(uid);
        if (userByUid != null){
            return Rest.success("成功",userByUid);
        }else {
            return Rest.fail("获取用户信息失败",null);
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
            String token = jwt.getToken(user.getUid());
            if (token != null){
                return UserRest.success("", user.getUid(), token);
            }else {
                String token1 = jwt.createToken(user.getUid(), "user");
                return UserRest.success("",user.getUid(),token1);
            }
        }

        return UserRest.fail("账户或密码错误");
    }

}
