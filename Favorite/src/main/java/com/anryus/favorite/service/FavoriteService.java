package com.anryus.favorite.service;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.Video;
import com.anryus.common.utils.JwtUtils;
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
    final JwtUtils jwtUtils;

    final
    PublishClient publishClient;

    final
    FeedClient feedClient;


    public FavoriteService(FavoriteMapper favoriteMapper, JwtUtils jwtUtils, PublishClient publishClient, FeedClient feedClient) {
        this.favoriteMapper = favoriteMapper;
        this.jwtUtils = jwtUtils;
        this.publishClient = publishClient;
        this.feedClient = feedClient;
    }

    public int actionFavorite(String token,long videoId,int actionType){
        Rest<Video> video = publishClient.getVideo(videoId);
        if (video.getStatusCode() != 0) {
            //没有该视频，非法操作
            return -1;
        }

        String s = jwtUtils.verify(token).get("uid");
        long UID = -1;
        if (s != null){
            UID = Long.parseLong(s);
        }

        int result = -1;



        Favorite favorite = new Favorite();
        favorite.setUserUid(UID);
        favorite.setVideoId(videoId);

        if (actionType == 1){

            QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_uid",UID).eq("video_id",videoId).eq("deleted",true);
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
            updateWrapper.eq("user_uid",UID).eq("video_id",videoId).eq("deleted",false).set("deleted",true);
            favorite.setDeleted(true);
            result = favoriteMapper.update(favorite,updateWrapper);
        }

        if (result > -1){
            feedClient.favoriteAction(videoId,actionType);
        }

        return result;
    }

    public List<Favorite> getFavoriteListByUid(Long uid,String token){
        long userId = uid;
        if (userId <= 0){
            userId = Long.parseLong(jwtUtils.verify(token).get("uid"));
        }

        QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid",userId);
        return favoriteMapper.selectList(queryWrapper);
    }

    public boolean isFavorite(long userId,String token,Long videoId){
        long uid = userId;
        if (userId <=0){
            String str = jwtUtils.verify(token).get("uid");
            if (str != null){
                uid = Long.parseLong(str);
            }
        }

        QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid",uid).eq("video_id",videoId).eq("deleted",false);
        Favorite favorite = favoriteMapper.selectOne(queryWrapper);
        return favorite != null;

    }

}
