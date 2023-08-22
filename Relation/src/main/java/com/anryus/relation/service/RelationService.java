package com.anryus.relation.service;

import com.anryus.common.utils.JwtUtils;
import com.anryus.common.utils.SnowFlake;
import com.anryus.relation.entity.Relation;
import com.anryus.relation.mapper.RelationMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RelationService {
    final
    RelationMapper relationMapper;

    final
    JwtUtils jwtUtils;


    public RelationService(RelationMapper relationMapper, JwtUtils jwtUtils) {
        this.relationMapper = relationMapper;
        this.jwtUtils = jwtUtils;
    }

    public int insertFollow(Long uid,long followUid){

        //TODO 检查是否存在历史的关注记录
        Relation relation = new Relation();
        relation.setFollowId(SnowFlake.Gen(1));
        relation.setUserUid(uid);
        relation.setFollowUid(followUid);
        relation.setDeleted(false);

        return relationMapper.insert(relation);
    }

    public int unFollow(Long uid,long followUid){
        UpdateWrapper<Relation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_uid",uid).eq("follow_uid",followUid).eq("deleted",false);
        Relation relation = new Relation();
        relation.setDeleted(true);

        return relationMapper.update(relation, updateWrapper);
    }

    public List<Relation> getFollowList(String token, long uid){
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_uid",uid).eq("deleted",false);
        return relationMapper.selectList(queryWrapper);
    }

    public List<Relation> getFollowerList(String token,long uid){
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follow_uid",uid).eq("deleted",false);
        return relationMapper.selectList(queryWrapper);
    }

}
