package com.yunbao.live.event;

public class DeleteChatEvent {
    private String id;

    public DeleteChatEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
