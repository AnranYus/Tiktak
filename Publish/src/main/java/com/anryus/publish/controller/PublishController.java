package com.anryus.publish.controller;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.Video;
import com.anryus.common.entity.VideoDTO;
import com.anryus.publish.service.PublishService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@ResponseBody
public class PublishController {
    final PublishService service;

    public PublishController(PublishService service) {
        this.service = service;
    }
    @PostMapping("/douyin/publish/action/")
    public Rest<Object> publishVideo(@RequestParam("file") MultipartFile file, @RequestParam("token")String token, @RequestParam("title")String title){
        int i = service.pushVideo(file,token,title);
        if (i>0){
            return Rest.success("成功");
        }else {
            return Rest.fail("失败");
        }
    }

    @GetMapping("/douyin/publish/list/")
    public Rest<List<VideoDTO>> getPublishVideoList(@RequestParam("token")@Nullable String token, @RequestParam("user_id")Long uid){
        List<VideoDTO> videoList = service.getVideoList(uid);
        return Rest.success("","video_list",videoList);
    }

    @GetMapping("/douyin/publish/video")
    public Rest<Video> getVideo(@RequestParam("video_id")Long videoId){
        Video videoById = service.getVideoById(videoId);
        if (videoById!=null){
            return Rest.success("","video",videoById);
        }else {
            return Rest.fail("");
        }

    }
}
