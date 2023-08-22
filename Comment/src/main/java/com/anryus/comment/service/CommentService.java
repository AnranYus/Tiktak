package com.anryus.comment.service;

import com.anryus.comment.entity.Comment;
import com.anryus.comment.entity.dto.CommentDTO;
import com.anryus.comment.mapper.FavoriteMapper;
import com.anryus.comment.service.client.FeedClient;
import com.anryus.comment.service.client.UserClient;
import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.utils.JwtUtils;
import com.anryus.common.utils.SnowFlake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired
    FavoriteMapper favoriteMapper;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserClient userClient;
    @Autowired
    FeedClient feedClient;

    /**
     *
     * @param videoId
     * @param content
     * @return 操作数量 小于1则操作失败
     */
    public CommentDTO insertComment(Long uid, long videoId, String content){
        //TODO 检查外键是否存在



        Comment comment = new Comment();
        comment.setCommentId(SnowFlake.Gen(1));
        comment.setContent(content);
        comment.setVideoId(videoId);
        comment.setUserUid(uid);
        Date date = new Date();
        comment.setCreateDate(date);
        int insert = favoriteMapper.insert(comment);

        Rest<User> userInfo = userClient.getUserInfo(uid, null);
        User user = userInfo.getAttributes().get("user");

        if (user != null && insert > 0){
            feedClient.commentAction(videoId,1);
            return CommentDTO.parse(comment,user);
        }

        //TODO 异常处理
        return null;
    }

    public int deleteComment(Long uid,long commentId){


        Comment comment = favoriteMapper.selectById(commentId);
        if (comment.getUserUid() == uid){
            comment.setDeleted(true);
            feedClient.commentAction(comment.getVideoId(),2);
            return favoriteMapper.updateById(comment);
        }
        return -1;

    }

    public List<CommentDTO> getCommentList(String token,long videoId){
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId).eq("deleted",false);
        List<Comment> comments = favoriteMapper.selectList(queryWrapper);
        List<CommentDTO> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            Rest<User> userInfo = userClient.getUserInfo(comment.getUserUid(), null);
            User user = userInfo.getAttributes().get("user");
            if (user!=null){
                CommentDTO parse = CommentDTO.parse(comment, user);
                dtos.add(parse);
            }
        }

        return dtos;
    }
}
