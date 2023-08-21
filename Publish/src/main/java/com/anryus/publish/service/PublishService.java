package com.anryus.publish.service;


import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.entity.Video;
import com.anryus.common.entity.VideoDTO;
import com.anryus.common.utils.JwtUtils;
import com.anryus.common.utils.SnowFlake;
import com.anryus.publish.mapper.PublishMapper;
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

            Video video = new Video(file.getOriginalFilename(),file.getOriginalFilename(),title,uid,null);
            video.setVideoId(SnowFlake.Gen(1));
            //TODO SAVE
            return publishMapper.insert(video);

        }
        return -1;
    }

    public List<VideoDTO> getVideoList(Long uid){
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid",uid).eq("deleted",false);
        List<Video> videos = publishMapper.selectList(queryWrapper);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (Video item:videos){
            Rest<User> userInfo = userClient.getUserInfo(item.getUserUid(), null);
            VideoDTO videoDTO = VideoDTO.parseVideoDTO(item,userInfo.getAttributes().get("user"));
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
