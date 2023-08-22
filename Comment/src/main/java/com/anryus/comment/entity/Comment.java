package com.anryus.comment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Comment {
    @TableId
    @JsonProperty("id")
    Long commentId;
    long userUid;
    long videoId;
    String content;
    boolean deleted;
    Date createDate;
}
