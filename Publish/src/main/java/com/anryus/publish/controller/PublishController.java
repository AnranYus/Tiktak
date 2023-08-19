package com.anryus.publish.controller;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.Video;
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
            return Rest.success("成功",null);
        }else {
            return Rest.fail("失败",null);
        }
    }

    @GetMapping("/douyin/publish/list/")
    public Rest<Object> getPublishVideoList(@RequestParam("token")@Nullable String token, @RequestParam("user_id")Long uid){
        List<Video> videoList = service.getVideoList(uid);

        return Rest.success("",videoList);
    }

    @GetMapping("/douyin/publish/video")
    public Rest<Video> getVideo(@RequestParam("video_id")Long videoId){
        Video videoById = service.getVideoById(videoId);
        if (videoById!=null){
            return Rest.success("",videoById);
        }else {
            return Rest.fail("",null);
        }

    }
}
