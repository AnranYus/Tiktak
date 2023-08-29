package com.anryus.comment.controller;


import com.anryus.comment.entity.dto.CommentDTO;
import com.anryus.comment.service.CommentService;
import com.anryus.common.entity.Rest;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseBody
public class CommentController {

    final
    CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/douyin/comment/action/")
    public Rest<CommentDTO> postComment(@RequestHeader("user-id")Long uid , @RequestParam("video_id")long videoId, @RequestParam("action_type")int actionType, @RequestParam("comment_text")@Nullable String content, @RequestParam("comment_id")@Nullable Long commentId){
        int result = -1;
        if (actionType == 1){
            if (content==null){
                return Rest.fail("非法操作");
            }

            CommentDTO dto = commentService.insertComment(uid, videoId, content);
            if (dto != null){
                return Rest.success("评论成功","comment",dto);
            }else {
                return Rest.fail("评论失败");

            }
        }else {
            if (commentId==null){
                return Rest.fail("非法操作");
            }

            result = commentService.deleteComment(uid,commentId);
            if (result>0){
                return Rest.success("删除成功");
            }else {
                return Rest.fail("删除失败");
            }
        }



    }

    @GetMapping("/douyin/comment/list/")
    public Rest<List<CommentDTO>> commentList(@RequestParam("video_id")long videoId ){
        List<CommentDTO> commentList = commentService.getCommentList(videoId);

        return Rest.success(null,"comment_list",commentList);
    }
}
