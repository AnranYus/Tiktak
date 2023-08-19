package com.anryus.userservice.service;


import com.anryus.common.entity.User;
import com.anryus.common.utils.SnowFlake;
import com.anryus.userservice.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final
    UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
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
        userMapper.insert(user);
        return user;
    }

    public User login(String username, String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(password);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username).eq("password",encryptedPassword).eq("deleted",false);

        return userMapper.selectOne(queryWrapper);
    }
}
