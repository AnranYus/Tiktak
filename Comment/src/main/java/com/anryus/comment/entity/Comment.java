package com.anryus.comment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Comment {
    @TableId
    Long commentId;
    long userUid;
    long videoId;
    String content;
    boolean deleted;
}
