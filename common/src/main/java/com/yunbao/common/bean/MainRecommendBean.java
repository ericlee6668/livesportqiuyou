package com.yunbao.common.bean;


import java.io.Serializable;

public class MainRecommendBean implements Serializable {


    private String liveclassid;
    private String uid;//	字符串	主播id
    private String avatar;//	字符串	主播头像
    private String avatar_thumb;//	字符串	头像缩略图
    private String user_nicename;//	字符串	直播昵称
    private String title;//	字符串	直播标题
    private String city;//	字符串	主播位置
    private String stream;//	字符串	流名
    private String pull;//	字符串	播流地址
    private String nums;//	字符串	人数
    private String distance;//	字符串	距离
    private String thumb;//	字符串	直播封面
    private String level_anchor;//	字符串	主播等级
    private String type;//	字符串	直播类型
    private String goodnum;//	字符串	靓号
    /**
     * game :
     * level : 1
     * sex : 1
     * type_val :
     * isvideo : 1
     * starttime : 1601286001
     * anyway : 0
     * hotvotes : 0
     * game_action : 0
     * isshop : 0
     */

    private String game;  //game
    private String level; //等级
    private String sex;   //主播性别
    private String type_val; //类型值
    private String isvideo;  //是否假视频
    private String starttime;  //开播时间
    private String anyway;  //横竖屏，0表示竖屏，1表示横屏
    private String hotvotes;  // hotvotes
    private String game_action;  //游戏类型
    private String isshop;  //是否开启店铺
    /**
     * viewnum : 2000
     * notice :
     * issubscribed : 0
     */

    private String viewnum;
    private String notice;   //	直播公告
    private int issubscribed; //是否已订阅
    private String match_id; //赛事id
    private int is_chat_off; //聊天室是否关闭 0.未关闭 1.已关闭
    private boolean isNetError;


    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getLevel_anchor() {
        return level_anchor;
    }

    public void setLevel_anchor(String level_anchor) {
        this.level_anchor = level_anchor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoodnum() {
        return goodnum;
    }

    public void setGoodnum(String goodnum) {
        this.goodnum = goodnum;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getType_val() {
        return type_val;
    }

    public void setType_val(String type_val) {
        this.type_val = type_val;
    }

    public String getIsvideo() {
        return isvideo;
    }

    public void setIsvideo(String isvideo) {
        this.isvideo = isvideo;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getAnyway() {
        return anyway;
    }

    public void setAnyway(String anyway) {
        this.anyway = anyway;
    }

    public String getHotvotes() {
        return hotvotes;
    }

    public void setHotvotes(String hotvotes) {
        this.hotvotes = hotvotes;
    }

    public String getGame_action() {
        return game_action;
    }

    public void setGame_action(String game_action) {
        this.game_action = game_action;
    }

    public String getIsshop() {
        return isshop;
    }

    public void setIsshop(String isshop) {
        this.isshop = isshop;
    }

    public String getViewnum() {
        return viewnum;
    }

    public void setViewnum(String viewnum) {
        this.viewnum = viewnum;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getIssubscribed() {
        return issubscribed;
    }

    public void setIssubscribed(int issubscribed) {
        this.issubscribed = issubscribed;
    }

    public boolean isNetError() {
        return isNetError;
    }

    public void setNetError(boolean netError) {
        isNetError = netError;
    }

    public int getIs_chat_off() {
        return is_chat_off;
    }

    public void setIs_chat_off(int is_chat_off) {
        this.is_chat_off = is_chat_off;
    }

    public String getLiveclassid() {
        return liveclassid;
    }

    public void setLiveclassid(String liveclassid) {
        this.liveclassid = liveclassid;
    }
}
