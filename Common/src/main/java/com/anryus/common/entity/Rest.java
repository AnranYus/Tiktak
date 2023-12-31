package com.anryus.common.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Rest<T> {

    /**
     * 状态码，0-成功，其他值-失败
     */
    private Integer statusCode;
    /**
     * 返回状态描述
     */
    private String statusMsg;

    //附加对象
    @JsonIgnore
    @JsonAnyGetter
    @JsonAnySetter
    private Map<String,T> attributes = new HashMap<>();

    public static Integer STATUS_SUCCESS = 0;
    public static Integer STATUS_FAIL = 1;

    public Rest() {
    }

    public Rest(Integer statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public Rest(Integer statusCode, String statusMsg,Map<String,T> attributes) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.attributes.putAll(attributes);
    }

    public static <T> Rest<T> success(String statusMsg,String key,T value){
        Map<String,T> map = new HashMap<>();
        map.put(key,value);
        return new Rest<>(STATUS_SUCCESS, statusMsg,map);
    }

    public static <T> Rest<T> fail(String statusMsg){
        return new Rest<>(STATUS_FAIL, statusMsg);
    }

    public static <T> Rest<T> success(String statusMsg){
        return new Rest<>(STATUS_SUCCESS, statusMsg);
    }

    public static <T> Rest<T> success(String statusMsg,Map<String,T> map){
        return new Rest<>(STATUS_SUCCESS, statusMsg,map);
    }

}
