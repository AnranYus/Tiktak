package com.anryus.favorite.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@TableName("user_like_video")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Favorite {
    @TableId
    Long likeId;
    long userUid;
    long videoId;
    boolean deleted;

}
