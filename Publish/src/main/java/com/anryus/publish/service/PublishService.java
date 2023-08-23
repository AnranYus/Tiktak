package com.anryus.publish.service;


import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.entity.Video;
import com.anryus.common.entity.VideoDTO;
import com.anryus.common.utils.JwtUtils;
import com.anryus.common.utils.SnowFlake;
import com.anryus.publish.mapper.PublishMapper;
import com.anryus.publish.service.client.FavoriteClient;
import com.anryus.publish.service.client.UserClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    final
    FavoriteClient favoriteClient;

    public PublishService(UserClient userClient, PublishMapper publishMapper, StringRedisTemplate template, JwtUtils jwtUtils, FavoriteClient favoriteClient) {
        this.userClient = userClient;
        this.publishMapper = publishMapper;
        this.template = template;
        this.jwtUtils = jwtUtils;
        this.favoriteClient = favoriteClient;
    }

    public int pushVideo( MultipartFile file,Long uid, String title){
        //TODO 储存视频

            Video video = new Video(file.getOriginalFilename(),file.getOriginalFilename(),title,uid,null);
            video.setVideoId(SnowFlake.Gen(1));
            //TODO SAVE
            int insert = publishMapper.insert(video);
            if (insert > 0){
                userClient.updateUserInfo(uid);
            }
            return insert;

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

    //TODO 保存上传视频
    public String save(MultipartFile file){
        return file.getOriginalFilename();
    }

    public Video getVideoById(Long videoId){
        Video video = publishMapper.selectById(videoId);
        if (video!= null && !video.isDeleted()){
            return video;
        }
        return null;
    }
}
