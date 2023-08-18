package com.anryus.relation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("follow")
public class Relation {
    @TableId
    long followId;
    long userUid;
    long followUid;
    boolean deleted;

}
