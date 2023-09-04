package com.anryus.favorite.controller;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.VideoDTO;
import com.anryus.favorite.service.FavoriteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
@ResponseBody
public class FavoriteController {

    final
    FavoriteService favoriteService;


    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/douyin/favorite/action/")
    public Rest<Object> favoriteAction(@RequestHeader("user-id")long requestUid, @RequestParam("video_id")long videoId, @RequestParam("action_type")int actionType){

        if (actionType == 1) {
            boolean favorite = favoriteService.isFavorite(requestUid, videoId);

            if (favorite) {
                return Rest.fail("已经喜欢过了");
            }
        }

        int i = favoriteService.actionFavorite(requestUid, videoId, actionType);
        if (i == -1){
            return Rest.fail("操作失败");
        }

        return Rest.success("操作成功");
    }

    @GetMapping("/douyin/favorite/list/")
    public Rest<List<VideoDTO>> favoriteList(@RequestParam("user_id")Long userId, @RequestHeader("user-id")long requestUid){
        return favoriteService.getFavoriteListByUid(userId, requestUid);
    }

    @GetMapping("/douyin/favorite/is")
    public boolean isFavorite(@RequestParam("user_id")Long userId,@RequestParam("video_id") Long videoId){
        return favoriteService.isFavorite(userId, videoId);
    }

}
