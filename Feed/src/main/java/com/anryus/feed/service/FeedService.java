package com.anryus.feed.service;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.entity.Video;
import com.anryus.feed.mapper.FeedMapper;
import com.anryus.feed.service.client.UserClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeedService {

    final FeedMapper feedMapper;
    final UserClient userClient;

    public FeedService(FeedMapper feedMapper, UserClient userClient) {
        this.feedMapper = feedMapper;
        this.userClient = userClient;
    }

    //获取视频列表
    public List<Video> getVideoByLatestTime(Long time, String token){
        Date date = new Date(time);
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("updatetime",date).eq("deleted",false);
        List<Video> videos = feedMapper.selectList(queryWrapper);

        for (Video video : videos) {
            long uid = video.getUserUid();
            Rest<User> userInfo = userClient.getUserInfo(uid, token);
            video.setAuthor(userInfo.getAttributes());
        }

        return videos;
    }

//    public List<Video> getVideoByUid(String uid){
//        return basicMapper.findVideoByUid(uid);
//    }
}
