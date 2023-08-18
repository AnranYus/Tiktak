package com.anryus.comment.controller;


import com.anryus.comment.entity.Comment;
import com.anryus.comment.service.CommentService;
import com.anryus.common.entity.Rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseBody
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/douyin/comment/action/")
    public Rest postComment(@RequestParam("token")String token, @RequestParam("video_id")long videoId, @RequestParam("action_type")int actionType, @RequestParam("comment_content")@Nullable String content, @RequestParam("comment_id")@Nullable Long commentId){
        int result = -1;
        if (actionType == 1){
            if (content==null){
                return Rest.fail("非法操作",null);
            }

            result = commentService.insertComment(token,videoId,content);
            if (result>0){
                return Rest.success("评论成功",null);
            }else {
                return Rest.fail("评论失败",null);

            }
        }else {
            if (commentId==null){
                return Rest.fail("非法操作",null);
            }

            result = commentService.deleteComment(token,commentId);
            if (result>0){
                return Rest.success("删除成功",null);
            }else {
                return Rest.fail("删除失败",null);
            }
        }



    }

    @GetMapping("/douyin/comment/list/")
    public Rest<List<Comment>> commentList(@RequestParam("token")String token, @RequestParam("video_id")long videoId ){
        List<Comment> commentList = commentService.getCommentList(token, videoId);
        return Rest.success(null,commentList);
    }
}
