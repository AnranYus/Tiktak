package com.anryus.userservice.service;


import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.utils.SnowFlake;
import com.anryus.userservice.mapper.UserMapper;
import com.anryus.userservice.utils.Jwt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Nonnull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    final
    UserMapper userMapper;

    final Jwt jwt;

    public UserService(UserMapper userMapper,Jwt jwt) {
        this.userMapper = userMapper;
        this.jwt = jwt;
    }

    public User getUserByUid(long uid){
        User user = userMapper.selectById(uid);
        if (user != null){
            user.setPassword(null);
        }
        return user;

    }

    private User findUserByUsername(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username).eq("deleted",false);
        return userMapper.selectOne(queryWrapper);
    }

    public User register(String username,String password){
        //TODO 验证用户名是否已经存在

        User userByUsername = findUserByUsername(username);
        if (userByUsername!=null){
            return null;
        }


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(password);
        User user = new User();
        user.setUid(SnowFlake.Gen(1));
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setName(username);
        userMapper.insert(user);
        return user;
    }

    public User login(String username, String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username",username).eq("deleted",false);

        User user = userMapper.selectOne(queryWrapper);
        boolean matches = encoder.matches(password, user.getPassword());
        if (matches){
            return user;
        }else {
            return null;
        }

    }

    @Nonnull
    public Rest<String> createToken(Long uid, String rule) {
        String token = jwt.createToken(uid, rule);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",uid   .toString());
        map.put("token",token);
        return Rest.success("",map);
    }
}
