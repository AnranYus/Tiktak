package com.anryus.publish.service;


import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.entity.Video;
import com.anryus.common.utils.JwtUtils;
import com.anryus.common.utils.SnowFlake;
import com.anryus.publish.mapper.PublishMapper;
import com.anryus.publish.service.client.UserClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class PublishService {

    final
    UserClient userClient;
    final
    PublishMapper publishMapper;
    final
    StringRedisTemplate template;
    final
    JwtUtils jwtUtils;

    public PublishService(UserClient userClient, PublishMapper publishMapper, StringRedisTemplate template, JwtUtils jwtUtils) {
        this.userClient = userClient;
        this.publishMapper = publishMapper;
        this.template = template;
        this.jwtUtils = jwtUtils;
    }

    public int pushVideo( MultipartFile file,String token, String title){
        //TODO 储存视频

        Map<String, String> map = jwtUtils.verify(token);
        if (map!=null){
            long uid = Long.parseLong(map.get("uid"));

            Rest<User> userRest = userClient.getUserInfo(uid,token);

            Video video = new Video();
            video.setVideoId(SnowFlake.Gen(1));
            video.setVideoPath(file.getOriginalFilename());//TODO
            video.setTitle(title);
            video.setCoverPath(file.getOriginalFilename());//TODO
            video.setUserUid(uid);
            video.setDescripath("Test");
            //TODO SAVE
            return publishMapper.insert(video);

        }
        return -1;
    }

    public List<Video> getVideoList(Long uid){
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid",uid);
        return publishMapper.selectList(queryWrapper);
    }

    //TODO 保存上传视频
    public String save(MultipartFile file){
        return file.getOriginalFilename();
    }

    public Video getVideoById(Long videoId){
        return publishMapper.selectById(videoId);
    }
}
