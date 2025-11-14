package com.yunbao.common.event;


public class UpdateAvatarEvent {

    private  String avatar;

    public UpdateAvatarEvent(String avatar) {
        this.avatar  =avatar;
    }

    public String getAvatar() {
        return avatar;
    }
}
