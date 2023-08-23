package com.anryus.publish.service;


import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.entity.Video;
import com.anryus.common.entity.VideoDTO;
import com.anryus.common.utils.JwtUtils;
import com.anryus.common.utils.SnowFlake;
import com.anryus.publish.client.AwsClient;
import com.anryus.publish.mapper.PublishMapper;
import com.anryus.publish.service.client.FavoriteClient;
import com.anryus.publish.service.client.UserClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    final
    FavoriteClient favoriteClient;

    @Autowired
    AwsClient awsClient;

    public PublishService(UserClient userClient, PublishMapper publishMapper, StringRedisTemplate template, JwtUtils jwtUtils, FavoriteClient favoriteClient) {
        this.userClient = userClient;
        this.publishMapper = publishMapper;
        this.template = template;
        this.jwtUtils = jwtUtils;
        this.favoriteClient = favoriteClient;
    }

    public int pushVideo( MultipartFile file,Long uid, String title){

        int result = -1;
        Long id = SnowFlake.Gen(1);
        String url = "";
        try {
             url = awsClient.upload(String.valueOf(id), file, file.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Video video = new Video(id,"null",url,title,uid,null);

        video.setVideoId(id);

        result = publishMapper.insert(video);
        if (result > 0){
            userClient.updateUserInfo(uid);
        }
        return result;

    }

    public List<VideoDTO> getVideoList(Long uid){
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid",uid).eq("deleted",false);
        List<Video> videos = publishMapper.selectList(queryWrapper);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (Video item:videos){
            Rest<User> userInfo = userClient.getUserInfo(item.getUserUid(), null);
            VideoDTO videoDTO = VideoDTO.parseVideoDTO(item,userInfo.getAttributes().get("user"),
                    favoriteClient.isFavorite(uid,item.getVideoId(),null));
            videoDTOS.add(videoDTO);
        }

        return videoDTOS;
    }

    public Video getVideoById(Long videoId){
        Video video = publishMapper.selectById(videoId);
        if (video!= null && !video.isDeleted()){
            return video;
        }
        return null;
    }
}
