package com.anryus.relation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@TableName("follow")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Relation {
    @TableId
    long followId;
    long userUid;
    long followUid;
    boolean deleted;

}
