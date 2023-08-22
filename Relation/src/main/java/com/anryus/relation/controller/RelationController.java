package com.anryus.relation.controller;

import com.anryus.common.entity.Rest;
import com.anryus.relation.entity.Relation;
import com.anryus.relation.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@ResponseBody
public class RelationController {

    @Autowired
    RelationService relationService;

    @PostMapping("/douyin/relation/action/")
    public Rest newFollow(@RequestHeader("user-id")Long uid, @RequestParam("to_user_id")long followUid, @RequestParam("action_type")int actionType){

        int i;
        if (actionType == 1){
            i = relationService.insertFollow(uid, followUid);
        }else {
            i = relationService.unFollow(uid, followUid);
        }

        if (i > 0){
            return Rest.success(null);
        }else {
            return Rest.fail(null);
        }

    }

    //TODO 合并
    @GetMapping("/douyin/relation/follow/list/")
    public Rest<List<Relation>> followList(@RequestParam("token")String token, @RequestParam("user_id")long uid){
        List<Relation> followList = relationService.getFollowList(token, uid);
        return Rest.success("","follow_list",followList);
    }

    @GetMapping("/douyin/relation/follower/list/")
    public Rest<List<Relation>> followerList(@RequestParam("token")String token,@RequestParam("user_id")long uid){
        List<Relation> followerList = relationService.getFollowerList(token, uid);
        return Rest.success("","follower_list",followerList);
    }

    @GetMapping("/douyin/relation/friend/list/")
    public Rest<List<Relation>> friendList(@RequestParam("token")String token,@RequestParam("user_id")long uid){
        List<Relation> followList = relationService.getFollowerList(token, uid);
        return Rest.success("","follow_list",followList);
    }


}
