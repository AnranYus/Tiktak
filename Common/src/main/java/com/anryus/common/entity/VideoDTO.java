package com.anryus.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class VideoDTO extends Video {

    private User author;

    @JsonProperty("is_favorite")
    private boolean favorite;

    public VideoDTO(long commentCount, String coverUrl, long likeCount, Long videoId, String videoUrl, String title, long userUid, String descripath, boolean deleted, User author,boolean favorite) {
        super(commentCount, coverUrl, likeCount, videoId, videoUrl, title, userUid, descripath, deleted);
        this.author = author;
        this.favorite = favorite;
    }

    public static VideoDTO parseVideoDTO(Video video, User author,boolean favorite){
        return new VideoDTO(video.getCommentCount(), video.getCoverUrl(), video.getLikeCount(), video.getVideoId(), video.getVideoUrl(), video.getTitle(), video.getUserUid(), video.getDescripath(), video.isDeleted(),author,favorite);
    }

    public VideoDTO() {
    }
}