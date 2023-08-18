package com.anryus.common.entity;

import lombok.Data;

@Data
public class Rest<T> {

    /**
     * 状态码，0-成功，其他值-失败
     */
    private long statusCode;
    /**
     * 返回状态描述
     */
    private String statusMsg;
    private T t;

    public static byte STATUS_SUCCESS = 0;
    public static byte STATUS_FAIL = 1;

    public Rest() {
    }

    public Rest(long statusCode, String statusMsg, T t) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.t = t;
    }

    public static <T> Rest<T> success(String statusMsg,T t){
        return new Rest<>(0, statusMsg, t);
    }

    public static <T> Rest<T> fail(String statusMsg,T t){
        return new Rest<>(1, statusMsg, t);
    }


}
