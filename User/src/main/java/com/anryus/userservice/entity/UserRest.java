package com.anryus.userservice.entity;

import lombok.Data;

@Data
public class UserRest{


    public UserRest(int statusCode, String statusMsg, long userId, String token) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.userId = userId;
        this.token = token;
    }

    public UserRest(int statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    int statusCode;
    String statusMsg;
    long userId;
    String token;
    public static UserRest success(String msg,long userId,String token){
        return new UserRest(0,msg,userId,token);
    }

    public static UserRest fail(String msg){
        return new UserRest(1,msg);
    }

}
