package com.anryus.favorite.service;

import com.anryus.common.utils.JwtUtils;
import com.anryus.common.utils.SnowFlake;
import com.anryus.favorite.entity.Favorite;
import com.anryus.favorite.mapper.FavoriteMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    final
    FavoriteMapper favoriteMapper;
    final JwtUtils jwtUtils;

    public FavoriteService(FavoriteMapper favoriteMapper,JwtUtils jwtUtils) {
        this.favoriteMapper = favoriteMapper;
        this.jwtUtils = jwtUtils;
    }

    public int actionFavorite(String token,long videoId,int actionType){
        //TODO 检查video_id是否存在数据库中
        //TODO 重复操作检查

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
            //新建喜欢
            favorite.setLikeId(SnowFlake.Gen(1));
            result = favoriteMapper.insert(favorite);
        }else {
            //更新为不喜欢
            UpdateWrapper<Favorite> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_uid",UID).eq("video_id",videoId);
            favorite.setDeleted(true);
            result = favoriteMapper.update(favorite, updateWrapper);
        }
        return result;
    }

    public List<Favorite> getFavoriteListByUid(String uid,String token){
        QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid",uid);
        return favoriteMapper.selectList(queryWrapper);
    }

}
