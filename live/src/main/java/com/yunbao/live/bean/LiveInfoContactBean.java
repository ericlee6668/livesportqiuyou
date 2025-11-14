package com.yunbao.live.bean;

import com.yunbao.common.http.JsonBean;

import java.util.List;

import cn.jmessage.support.qiniu.android.utils.Json;

public class LiveInfoContactBean {

    /**
     * uid : 403
     * title : 英雄联盟赛事
     * notice : undefined
     * city : 好像在火星
     * stream : 403_1614570811
     * pull : https://liveplay.sxxbtd.com/live/403_1614570811.flv
     * thumb : https://img.ncyrsc.com/image/live/20210301/69be1a4b71e0296d8c2a450a1e32ce88?imageView2/2/w/600/h/600
     * isvideo : 0
     * type : 0
     * type_val :
     * goodnum : 0
     * anyway : 1
     * starttime : 1614570865
     * isshop : 0
     * game_action : 0
     * is_chat_off : 0
     * nums : 251
     * avatar : https://img.ncyrsc.com/image/avatar/default.jpg
     * avatar_thumb : https://img.ncyrsc.com/image/avatar/default_thumb.jpg
     * user_nicename : 英雄联盟 绝地求生
     * sex : 1
     * level : 1
     * level_anchor : 1
     * viewnum : 1007887
     * game :
     * pull_h5 : https://liveplay.sxxbtd.com/live/403_1614570811.m3u8
     * isSubscribe : 0
     * subscribes : 3
     * contract : [{"uid":"403","contract":"482090917","type":"1","status":"1"},{"uid":"403","contract":"482090917","type":"2","status":"2"}]
     */

    private String uid;
    private String title;
    private String notice;
    private String city;
    private String stream;
    private String pull;
    private String thumb;
    private String isvideo;
    private String type;
    private String type_val;
    private String goodnum;
    private String anyway;
    private String starttime;
    private String isshop;
    private String game_action;
    private String is_chat_off;


    private String audio_path;
    private String nums;
    private String avatar;
    private String avatar_thumb;
    private String user_nicename;
    private String sex;
    private String level;
    private String level_anchor;
    private int viewnum;
    private String game;
    private String pull_h5;
    private int isSubscribe;
    private String subscribes;
    private List<ContractBean> contract;
    private LiveIm live_im;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getPull() {
        return pull;
    }

    public void setPull(String pull) {
        this.pull = pull;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getIsvideo() {
        return isvideo;
    }

    public void setIsvideo(String isvideo) {
        this.isvideo = isvideo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_val() {
        return type_val;
    }

    public void setType_val(String type_val) {
        this.type_val = type_val;
    }

    public String getGoodnum() {
        return goodnum;
    }

    public void setGoodnum(String goodnum) {
        this.goodnum = goodnum;
    }

    public String getAnyway() {
        return anyway;
    }

    public void setAnyway(String anyway) {
        this.anyway = anyway;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getIsshop() {
        return isshop;
    }

    public void setIsshop(String isshop) {
        this.isshop = isshop;
    }

    public String getGame_action() {
        return game_action;
    }

    public void setGame_action(String game_action) {
        this.game_action = game_action;
    }

    public String getAudio_path() {
        return audio_path;
    }

    public void setAudio_path(String audio_path) {
        this.audio_path = audio_path;
    }
    public String getIs_chat_off() {
        return is_chat_off;
    }

    public void setIs_chat_off(String is_chat_off) {
        this.is_chat_off = is_chat_off;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_thumb() {
        return avatar_thumb;
    }

    public void setAvatar_thumb(String avatar_thumb) {
        this.avatar_thumb = avatar_thumb;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel_anchor() {
        return level_anchor;
    }

    public void setLevel_anchor(String level_anchor) {
        this.level_anchor = level_anchor;
    }

    public int getViewnum() {
        return viewnum;
    }

    public void setViewnum(int viewnum) {
        this.viewnum = viewnum;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPull_h5() {
        return pull_h5;
    }

    public void setPull_h5(String pull_h5) {
        this.pull_h5 = pull_h5;
    }

    public int getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(String subscribes) {
        this.subscribes = subscribes;
    }

    public List<ContractBean> getContract() {
        return contract;
    }

    public void setContract(List<ContractBean> contract) {
        this.contract = contract;
    }

    public static class ContractBean {
        /**
         * uid : 403
         * contract : 482090917
         * type : 1
         * status : 1
         */

        private String uid;
        private String contract;
        private String type;
        private int status;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getContract() {
            return contract;
        }

        public void setContract(String contract) {
            this.contract = contract;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public LiveIm getLive_im() {
        return live_im;
    }

    public void setLive_im(LiveIm live_im) {
        this.live_im = live_im;
    }

    public static class LiveIm{
        private String uid;
        private JsonBeans json;
        private String status;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public JsonBeans getJson() {
            return json;
        }

        public void setJson(JsonBeans json) {
            this.json = json;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public static class JsonBeans{
            private String im_script;
            private String im_url;

            public String getIm_script() {
                return im_script;
            }

            public void setIm_script(String im_script) {
                this.im_script = im_script;
            }

            public String getIm_url() {
                return im_url;
            }

            public void setIm_url(String im_url) {
                this.im_url = im_url;
            }
        }
    }


}
