package com.anryus.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Video {
    /**
     * 视频作者信息
     */
    @TableField(exist = false)
    private User author;
    /**
     * 视频的评论总数
     */
    private long commentCount;
    /**
     * 视频封面地址
     */
    private String coverPath;
    /**
     * 视频的点赞总数
     */
    private long likeCount;
    /**
     * 视频唯一标识
     */
    @TableId("video_id")
    private Long videoId;
    /**
     * 视频播放地址
     */
    private String videoPath;
    /**
     * 视频标题
     */
    private String title;
    private long userUid;
    private String descripath;
    private boolean deleted;
}
