package com.anryus.common.entity;

import com.anryus.common.utils.SnowFlake;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Video {

    public Video(Long videoId,String coverPath, String videoPath, String title, long userUid, String descripath) {
        this.videoId = videoId;
        this.coverUrl = coverPath;
        this.videoUrl = videoPath;
        this.title = title;
        this.userUid = userUid;
        this.descripath = descripath;

    }

    public Video(long commentCount, String coverUrl, long likeCount, Long videoId, String videoUrl, String title, long userUid, String descripath, boolean deleted) {
        this.commentCount = commentCount;
        this.coverUrl = coverUrl;
        this.likeCount = likeCount;
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.title = title;
        this.userUid = userUid;
        this.descripath = descripath;
        this.deleted = deleted;
    }

    public Video() {
    }

    /**
     * 视频的评论总数
     */
    private long commentCount;
    /**
     * 视频封面地址
     */
    private String coverUrl;
    /**
     * 视频的点赞总数
     */
    @JsonProperty("favorite_count")
    private long likeCount;
    /**
     * 视频唯一标识
     */
    @TableId("video_id")
    @JsonProperty("id")
    private Long videoId;
    /**
     * 视频播放地址
     */
    @JsonProperty("play_url")
    private String videoUrl;
    /**
     * 视频标题
     */
    private String title;
    private long userUid;
    private String descripath;
    private boolean deleted;
    private Date updatetime = new Date();
}
