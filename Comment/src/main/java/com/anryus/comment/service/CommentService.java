package com.anryus.comment.service;

import com.anryus.comment.entity.Comment;
import com.anryus.comment.mapper.FavoriteMapper;
import com.anryus.common.utils.JwtUtils;
import com.anryus.common.utils.SnowFlake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired
    FavoriteMapper favoriteMapper;
    @Autowired
    JwtUtils jwtUtils;

    /**
     *
     * @param token
     * @param videoId
     * @param content
     * @return 操作数量 小于1则操作失败
     */
    public int insertComment(String token, long videoId, String content){
        //TODO 检查外键是否存在

        String s = jwtUtils.verify(token).get("uid");
        long UID = -1;
        if (s != null){
            UID = Long.parseLong(s);
        }

        Comment comment = new Comment();
        comment.setCommentId(SnowFlake.Gen(1));
        comment.setContent(content);
        comment.setVideoId(videoId);
        comment.setUserUid(UID);

        return favoriteMapper.insert(comment);
    }

    public int deleteComment(String token,long commentId){
        String s = jwtUtils.verify(token).get("uid");
        long UID = -1;
        if (s != null){
            UID = Long.parseLong(s);
        }

        Comment comment = favoriteMapper.selectById(commentId);
        if (comment.getUserUid() == UID){
            comment.setDeleted(true);
            return favoriteMapper.updateById(comment);
        }
        return -1;

    }

    public List<Comment> getCommentList(String token,long videoId){
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId).eq("deleted",false);

        return favoriteMapper.selectList(queryWrapper);
    }
}
