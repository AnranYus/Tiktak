package com.anryus.common.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Rest<T> {

    /**
     * 状态码，0-成功，其他值-失败
     */
    private long statusCode;
    /**
     * 返回状态描述
     */
    private String statusMsg;

    //附加对象
    @JsonAnyGetter
    private Map<String,T> attributes = new HashMap<>();
    private String key;

    public static byte STATUS_SUCCESS = 0;
    public static byte STATUS_FAIL = 1;

    public Rest() {
    }

    public Rest(long statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public Rest(long statusCode, String statusMsg,String key ,Map<String,T> attributes) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.key = key;
        this.attributes.putAll(attributes);
    }

    public static <T> Rest<T> success(String statusMsg,String key,T value){
        Map<String,T> map = new HashMap<>();
        map.put(key,value);
        return new Rest<>(STATUS_SUCCESS, statusMsg, key,map);
    }

    public static <T> Rest<T> fail(String statusMsg){
        return new Rest<>(STATUS_FAIL, statusMsg);
    }

    public static <T> Rest<T> success(String statusMsg){
        return new Rest<>(STATUS_SUCCESS, statusMsg);
    }

    public T getAttributes(){
        return attributes.get(key);
    }


}
