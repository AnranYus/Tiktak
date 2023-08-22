package com.anryus.favorite.controller;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.VideoDTO;
import com.anryus.common.utils.JwtUtils;
import com.anryus.favorite.service.FavoriteService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
@ResponseBody
public class FavoriteController {

    final
    FavoriteService favoriteService;
    final
    JwtUtils jwtUtils;

    public FavoriteController(FavoriteService favoriteService, JwtUtils jwtUtils) {
        this.favoriteService = favoriteService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/douyin/favorite/action/")
    public Rest<Object> favoriteAction(@RequestParam("token")String token, @RequestParam("video_id")long videoId, @RequestParam("action_type")int actionType){

        if (actionType == 1) {
            boolean favorite = favoriteService.isFavorite(0, token, videoId);

            if (favorite) {
                return Rest.fail("已经喜欢过了");
            }
        }

        int i = favoriteService.actionFavorite(token, videoId, actionType);
        if (i == -1){
            return Rest.fail("操作失败");
        }

        return Rest.success("操作成功");
    }

    @GetMapping("/douyin/favorite/list/")
    public Rest<List<VideoDTO>> favoriteList(@RequestParam("user_id")Long userId, @RequestParam("token") String token){
        return favoriteService.getFavoriteListByUid(userId, token);
    }

    @GetMapping("/douyin/favorite/is")
    public boolean isFavorite(@RequestParam("user_id")Long userId,@RequestParam("video_id") Long videoId,@RequestParam("token")@Nullable String token){
        return favoriteService.isFavorite(userId, token, videoId);
    }

}
