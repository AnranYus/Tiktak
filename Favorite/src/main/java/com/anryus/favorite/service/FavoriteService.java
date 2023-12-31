package com.anryus.favorite.service;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.Video;
import com.anryus.common.entity.VideoDTO;
import com.anryus.common.utils.SnowFlake;
import com.anryus.common.entity.Favorite;
import com.anryus.favorite.mapper.FavoriteMapper;
import com.anryus.favorite.service.client.FeedClient;
import com.anryus.favorite.service.client.PublishClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    final
    FavoriteMapper favoriteMapper;

    final
    PublishClient publishClient;

    final
    FeedClient feedClient;


    public FavoriteService(FavoriteMapper favoriteMapper, PublishClient publishClient, FeedClient feedClient) {
        this.favoriteMapper = favoriteMapper;
        this.publishClient = publishClient;
        this.feedClient = feedClient;
    }

    public int actionFavorite(long uid,long videoId,int actionType){
        Rest<Video> video = publishClient.getVideo(videoId);
        if (video.getStatusCode() != 0) {
            //没有该视频，非法操作
            return -1;
        }


        int result = -1;



        Favorite favorite = new Favorite();
        favorite.setUserUid(uid);
        favorite.setVideoId(videoId);

        if (actionType == 1){

            QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_uid", uid).eq("video_id",videoId).eq("deleted",true);
            Favorite exites = favoriteMapper.selectOne(queryWrapper);
            if (exites != null){
                //存在被删除的项，恢复
                exites.setDeleted(false);
                result = favoriteMapper.updateById(exites);

            }else {
                //新建喜欢
                favorite.setLikeId(SnowFlake.Gen(1));
                result = favoriteMapper.insert(favorite);
            }

        }else {

            //更新为不喜欢
            UpdateWrapper<Favorite> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_uid", uid).eq("video_id",videoId).eq("deleted",false).set("deleted",true);
            favorite.setDeleted(true);
            result = favoriteMapper.update(favorite,updateWrapper);
        }

        if (result > -1){
            feedClient.favoriteAction(videoId,actionType);
        }

        return result;
    }

    public Rest<List<VideoDTO>> getFavoriteListByUid(Long uid, long requestUid){
        long userId = uid;
        if (userId <= 0){
            userId = requestUid;
        }

        QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid",userId);
        List<Favorite> favorites = favoriteMapper.selectList(queryWrapper);
        return feedClient.favoriteVideos(favorites);
    }

    public boolean isFavorite(long uid,Long videoId){

        if (uid <=0){
            return false;
        }

        QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid", uid).eq("video_id",videoId).eq("deleted",false);
        Favorite favorite = favoriteMapper.selectOne(queryWrapper);
        return favorite != null;

    }

}
