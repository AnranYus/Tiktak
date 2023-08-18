package com.anryus.favorite.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_like_video")
public class Favorite {
    @TableId
    Long likeId;
    long userUid;
    long videoId;
    boolean deleted;

}
