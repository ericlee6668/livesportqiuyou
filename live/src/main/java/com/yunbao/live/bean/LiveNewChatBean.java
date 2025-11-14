package com.yunbao.live.bean;

import java.util.List;

/**
 * @author Juwan
 * @date 2020/10/8
 * Description:
 */
public class LiveNewChatBean {

    /**
     * type : come
     * msg : 游客1117 进来了
     * logs : []
     * user : {"user_nicename":"游客1117","uid":""}
     */

    private String type;
    private String msg;
    private LiveChatUserBean user;
    private List<LiveNewChatBean> logs;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LiveChatUserBean getUser() {
        return user;
    }

    public void setUser(LiveChatUserBean user) {
        this.user = user;
    }

    public List<LiveNewChatBean> getLogs() {
        return logs;
    }

    public void setLogs(List<LiveNewChatBean> logs) {
        this.logs = logs;
    }

}
