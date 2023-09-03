package com.anryus.message.controller;

import com.anryus.common.entity.Rest;
import com.anryus.message.entity.Message;
import com.anryus.message.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
public class MessageController {

    final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/douyin/message/action/")
    public Rest<Object> sendMessage(@RequestHeader("user-id")long requestUid, @RequestParam("to_user_id")Long receiverUid
            ,@RequestParam("action_type")int type,@RequestParam("content")String content){
        int i = messageService.sendMessage(requestUid, receiverUid, content);
        if (i == MessageService.UNFINDUSER){
            return Rest.fail("错误的接收者参数");
        }
        if (i>0){
            return Rest.success(null);
        }else {
            return Rest.fail("发送失败");
        }
    }

    @GetMapping("/douyin/message/chat/")
    public Rest<List<Message>> getMessageList(@RequestHeader("user-id")Long requestUid, @RequestParam("to_user_id")Long receiverUid){
        List<Message> messages = messageService.MessageList(requestUid, receiverUid);
        Map<String,List<Message>> map = new HashMap<>();
        map.put("message_list",messages);
        return Rest.success(null,map);
    }
}
