package com.anryus.feed.mapper;

import com.anryus.common.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedMapper extends BaseMapper<Video> {
//    List<Video> findVideoByLatestTime(String time);
//
//    @Insert("INSERT INTO `video` (`VIDEO_ID`, `USER_UID`, `DESCRIPATH`, `TITLE`, `COVER_PATH`, `VIDEO_PATH`, `LIKE_COUNT`, `COMMENT_COUNT`, `CREATED_TIME`, `UPDATED_TIME`, `DELETE`) " +
//            "VALUES (#{video_id}, #{user_id}, 'null', #{title}, 'null', 'test', 0, 0, now(), now(), 0)")
//    int
//    insertVideo(@Param("video_id")long id,@Param("user_id")long uid,@Param("descripath")String descripath,@Param("title")String title,@Param("cover_path")String cover,
//                    @Param("video_path")String videoPath);
//
//    @Select("select * from video where user_uid = #{uid}")
//    List<Video> findVideoByUid(String uid);
}
