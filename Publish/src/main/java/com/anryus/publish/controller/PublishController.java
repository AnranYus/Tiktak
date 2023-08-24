package com.anryus.publish.controller;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.Video;
import com.anryus.common.entity.VideoDTO;
import com.anryus.common.utils.JwtUtils;
import com.anryus.publish.service.PublishService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@ResponseBody
public class PublishController {
    final PublishService service;
    final JwtUtils jwtUtils;

    public PublishController(PublishService service, JwtUtils jwtUtils) {
        this.service = service;
        this.jwtUtils = jwtUtils;
    }
    @PostMapping("/douyin/publish/action/")
    public Rest<Object> publishVideo(MultipartFile data, String token, String title){

        Map<String, String> verify = jwtUtils.verify(token);
        String s = verify.get("uid");
        if (s != null){

            long uid = Long.parseLong(s);

            int i = service.pushVideo(data,uid,title);
            if (i>0){
                return Rest.success("成功");
            }else {
                return Rest.fail("失败");
            }
        }
        return Rest.fail("拒绝访问");

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
