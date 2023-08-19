package com.anryus.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {

    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户个人页顶部大图
     */
    private String backgroundImg;
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
    private Long uid;
    /**
     * 用户名称
     */
//    private String name;
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

}
