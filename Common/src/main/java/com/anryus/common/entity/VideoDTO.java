package com.anryus.common.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class VideoDTO extends Video {

    private final User author;

    public VideoDTO(long commentCount, String coverUrl, long likeCount, Long videoId, String videoUrl, String title, long userUid, String descripath, boolean deleted, User author) {
        super(commentCount, coverUrl, likeCount, videoId, videoUrl, title, userUid, descripath, deleted);
        this.author = author;
    }

    public static VideoDTO parseVideoDTO(Video video, User author){
        return new VideoDTO(video.getCommentCount(), video.getCoverUrl(), video.getLikeCount(), video.getVideoId(), video.getVideoUrl(), video.getTitle(), video.getUserUid(), video.getDescripath(), video.isDeleted(),author);
    }

}