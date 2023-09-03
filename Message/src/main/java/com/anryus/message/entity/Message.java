package com.anryus.message.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Message {
    @TableId("message_id")
    Long id;

    @TableField("uid_send")
    Long senderUid;

    @TableField("uid_receive")
    Long receiverUid;

    String content;
    long createTime;

}
