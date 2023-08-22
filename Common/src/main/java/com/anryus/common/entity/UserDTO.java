package com.anryus.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends User{
    @JsonProperty("is_follow")
    private boolean follow;

    public static UserDTO parse(User user,boolean follow){
        UserDTO userDTO = new UserDTO();
        userDTO.setAvatar(user.getAvatar());
        userDTO.setBackgroundImage(user.getBackgroundImage());
        userDTO.setFollowCount(user.getFollowCount());
        userDTO.setFollowerCount(user.getFollowerCount());
        userDTO.setUid(user.getUid());
        userDTO.setName(user.getName());
        userDTO.setSignature(user.getSignature());
        userDTO.setTotalFavorited(user.getTotalFavorited());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setDeleted(user.isDeleted());
        userDTO.setFollow(follow);

        return userDTO;


    }


}
