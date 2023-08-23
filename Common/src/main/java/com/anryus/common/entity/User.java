package com.anryus.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User {

    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户个人页顶部大图
     */
    private String backgroundImage;
    /**
     * 关注总数
     */
    private long followCount;
    /**
     * 粉丝总数
     */
    private long followerCount;
    /**
     * 用户id
     */
    @TableId("uid")
    @JsonProperty("id")
    private Long uid;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 个人简介
     */
    private String signature;
    /**
     * 获赞数量
     */
    private String totalFavorited;
    private String username;
    private String password;
    private boolean deleted;
    //作品数量
    private long workCount;

}
