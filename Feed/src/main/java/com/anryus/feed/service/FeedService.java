package com.anryus.feed.service;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.entity.Video;
import com.anryus.common.entity.VideoDTO;
import com.anryus.feed.mapper.FeedMapper;
import com.anryus.feed.service.client.UserClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FeedService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    final FeedMapper feedMapper;
    final UserClient userClient;

    public FeedService(FeedMapper feedMapper, UserClient userClient) {
        this.feedMapper = feedMapper;
        this.userClient = userClient;
    }

    //获取视频列表
    public List<VideoDTO> getVideoByLatestTime(Long time, String token){
        Date date = new Date(time);
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("updatetime",date).eq("deleted",false);
        List<Video> videos = feedMapper.selectList(queryWrapper);
        List<VideoDTO> result = new ArrayList<>();
        for (Video video : videos) {
            long uid = video.getUserUid();
            Rest<User> userInfo = userClient.getUserInfo(uid, token);
            logger.info(userInfo.toString());
            VideoDTO dto = VideoDTO.parseVideoDTO(video,userInfo.getAttributes().get("user"));
            result.add(dto);
        }

        return result;
    }

//    public List<Video> getVideoByUid(String uid){
//        return basicMapper.findVideoByUid(uid);
//    }
}
