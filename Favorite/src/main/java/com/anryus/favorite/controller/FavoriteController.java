package com.anryus.favorite.controller;

import com.anryus.common.entity.Rest;
import com.anryus.favorite.entity.Favorite;
import com.anryus.favorite.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
@ResponseBody
public class FavoriteController {

    @Autowired
    FavoriteService favoriteService;

    @PostMapping("/do/favorite/action/")
    public Rest<Object> favoriteAction(@RequestParam("token")String token, @RequestParam("video_id")long videoId, @RequestParam("action_type")int actionType){
        int i = favoriteService.actionFavorite(token, videoId, actionType);
        if (i == -1){
            return Rest.fail("操作失败",null);
        }

        return Rest.success("操作成功",null);
    }

    @GetMapping("/douyin/favorite/list/")
    public Rest<List<Favorite>> favoriteList(@RequestParam("user_id")String userId, @RequestParam("token") String token){
        List<Favorite> favoriteListByUid = favoriteService.getFavoriteListByUid(userId, token);
        return Rest.success(null,favoriteListByUid);
    }

}
