package com.anryus.comment.entity.dto;

import com.anryus.comment.entity.Comment;
import com.anryus.common.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentDTO extends Comment {
    private User user;

    @JsonProperty("create_date")
    private String createDateStr;

    public static CommentDTO parse(Comment comment,User user){
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getCommentId());
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        dto.setCreateDateStr(sdf.format(comment.getCreateDate()));
        dto.setDeleted(comment.isDeleted());
        dto.setContent(comment.getContent());
        dto.setVideoId(comment.getVideoId());
        dto.setUser(user);
        return dto;

    }
}
