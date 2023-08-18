package com.anryus.feed.entity;

import com.anryus.common.entity.Video;
import lombok.Data;

import java.util.List;

@Data
public class FeedRest {
    /**
     * 本次返回的视频中，发布最早的时间，作为下次请求时的latest_time
     */
    private Long nextTime;
    /**
     * 状态码，0-成功，其他值-失败
     */
    private long statusCode;
    /**
     * 返回状态描述
     */
    private String statusMsg;
    /**
     * 视频列表
     */
    private List<Video> videoList;

    public FeedRest(Long nextTime, long statusCode, String statusMsg, List<Video> videoList) {
        this.nextTime = nextTime;
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.videoList = videoList;
    }

    public FeedRest(long statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public static FeedRest success(String statusMsg, long nextTime, List<Video> videoList){
        return new FeedRest(nextTime,0,statusMsg,videoList);
    }

    public static FeedRest fail(String statusMsg){
        return new FeedRest(1,statusMsg);
    }
}