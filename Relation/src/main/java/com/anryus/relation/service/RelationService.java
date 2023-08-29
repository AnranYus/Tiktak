package com.anryus.relation.service;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.entity.UserDTO;
import com.anryus.common.utils.SnowFlake;
import com.anryus.relation.entity.Relation;
import com.anryus.relation.mapper.RelationMapper;
import com.anryus.relation.service.client.UserClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RelationService {
    final
    RelationMapper relationMapper;

    final UserClient userClient;


    public RelationService(RelationMapper relationMapper, UserClient userClient) {
        this.relationMapper = relationMapper;
        this.userClient = userClient;
    }

    public int insertFollow(Long uid,long followUid){

        //TODO 检查是否存在历史的关注记录
        Relation relation = new Relation();
        relation.setFollowId(SnowFlake.Gen(1));
        relation.setUserUid(uid);
        relation.setFollowUid(followUid);
        relation.setDeleted(false);
        int insert = relationMapper.insert(relation);
        if (insert > 0){
            userClient.followAction(relation.getUserUid(),1);
            userClient.followAction(relation.getFollowUid(),2);
        }

        return insert;
    }

    public int unFollow(Long uid,long followUid){
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid",uid).eq("follow_uid",followUid).eq("deleted",false);
        Relation relation = relationMapper.selectOne(queryWrapper);

        relation.setDeleted(true);
        int update = relationMapper.updateById(relation);
        if (update > 0){
            userClient.unfollowAction(relation.getUserUid(),1);
            userClient.unfollowAction(relation.getFollowUid(),2);
        }
        return update;
    }

    /**
     * 查询关注列表 user_uid
     * follow_uid不同
     * 返回follow_uid的user信息
     * @param uid
     * @return
     */
    public List<UserDTO> getFollowList(long uid){
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid",uid).eq("deleted",false);

        List<Relation> relations = relationMapper.selectList(queryWrapper);
        List<UserDTO> userList = new ArrayList<>();

        for (Relation relation : relations) {
            Rest<User> userInfo = userClient.getUserInfo(relation.getFollowUid(), null);
            User user = userInfo.getAttributes().get("user");

            if (user!=null){
                UserDTO parse = UserDTO.parse(user,true);
                userList.add(parse);
            }
        }

        return userList;
    }

    /**
     * 查询粉丝列表 follow_uid
     * user_uid不同
     * 返回user_uid的user信息
     * @param uid
     * @return
     */
    public List<UserDTO> getFollowerList(long uid){
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follow_uid",uid).eq("deleted",false);
        List<Relation> relations = relationMapper.selectList(queryWrapper);
        List<UserDTO> userList = new ArrayList<>();

        for (Relation relation : relations) {
            Rest<User> userInfo = userClient.getUserInfo(relation.getUserUid(), null);
            User user = userInfo.getAttributes().get("user");
            if (user!=null){
                UserDTO parse = UserDTO.parse(user, true);
                userList.add(parse);
            }
        }

        return userList;
    }


}
