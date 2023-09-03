package com.anryus.message.service;

import com.anryus.common.entity.Rest;
import com.anryus.common.entity.User;
import com.anryus.common.utils.SnowFlake;
import com.anryus.message.entity.Message;
import com.anryus.message.mapper.MessageMapper;
import com.anryus.message.service.client.UserClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class MessageService {
    final MessageMapper messageMapper;
    final UserClient userClient;
    public static final int UNFINDUSER = -404;

    public MessageService(MessageMapper messageMapper, UserClient userClient) {
        this.messageMapper = messageMapper;
        this.userClient = userClient;
    }

    public int sendMessage(Long uid,Long receiverUid,String content){
        Rest<User> receiverInfo = userClient.getUserInfo(receiverUid, null);
        if (!Objects.equals(receiverInfo.getStatusCode(), Rest.STATUS_SUCCESS)){
            return UNFINDUSER;
        }

        Message message = new Message();
        message.setId(SnowFlake.Gen(1));
        message.setSenderUid(uid);
        message.setReceiverUid(receiverUid);
        message.setContent(content);
        message.setCreateTime(new Date().getTime());

        return messageMapper.insert(message);
    }

    public List<Message> MessageList(long snederUid,long receiverUid){
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid_send", snederUid).eq("uid_receive", receiverUid);
        return messageMapper.selectList(queryWrapper);
    }

}
