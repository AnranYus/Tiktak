package com.anryus.feed.service;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.entity.Video;
import com.anryus.common.entity.VideoDTO;
import com.anryus.feed.mapper.FeedMapper;
import com.anryus.feed.service.client.FavoriteClient;
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

    final
    FavoriteClient favoriteClient;

    public FeedService(FeedMapper feedMapper, UserClient userClient, FavoriteClient favoriteClient) {
        this.feedMapper = feedMapper;
        this.userClient = userClient;
        this.favoriteClient = favoriteClient;
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
            VideoDTO dto = VideoDTO.parseVideoDTO(video,userInfo.getAttributes().get("user"), favoriteClient.isFavorite(0L,video.getVideoId(),token));
            result.add(dto);
        }

        return result;
    }

    public Video favoriteAction(Long videoId,int action){
        Video video = feedMapper.selectById(videoId);
        if (video == null){
            return null;
        }
        if (action == 1){
            video.setLikeCount(video.getLikeCount() + 1);
        }else {
            video.setLikeCount(video.getLikeCount() - 1);

        }
        feedMapper.updateById(video);
        return video;
    }

    public Video commentAction(Long videoId,int action){
        Video video = feedMapper.selectById(videoId);
        if (video == null){
            return null;
        }
        if (action == 1){
            video.setCommentCount(video.getCommentCount() + 1);
        }else {
            video.setCommentCount(video.getCommentCount() - 1);

        }
        feedMapper.updateById(video);
        return video;
    }
}
