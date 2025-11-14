package com.yunbao.main.bean;


public class SubscribeAnchorBean {

    /**
     * id : 7
     * user_nicename : 手机用户3227
     * avatar : http://live.zbitcLoud.com/default.jpg
     * avatar_thumb : http://live.zbitcLoud.com/default_thumb.jpg
     * sex : 0
     * signature : 这家伙很懒，什么都没留下
     * consumption : 0
     * votestotal : 0
     * province :
     * city : 好像在火星
     * birthday :
     * user_status : 1
     * issuper : 0
     * location :
     * viewnum : 2002
     * level : 1
     * level_anchor : 1
     * vip : {"type":"0"}
     * liang : {"name":"0"}
     * islive : 0
     */

    private String id;
    private String user_nicename;
    private String avatar;
    private String avatar_thumb;
    private String sex;
    private String signature;
    private String consumption;
    private String votestotal;
    private String province;
    private String city;
    private String birthday;
    private String user_status;
    private String issuper;
    private String location;
    private String viewnum;
    private String level;
    private String level_anchor;
    private VipBean vip;
    private LiangBean liang;
    private String islive;

    private int isSubscribe = 1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getVotestotal() {
        return votestotal;
    }

    public void setVotestotal(String votestotal) {
        this.votestotal = votestotal;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getIssuper() {
        return issuper;
    }

    public void setIssuper(String issuper) {
        this.issuper = issuper;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getViewnum() {
        return viewnum;
    }

    public void setViewnum(String viewnum) {
        this.viewnum = viewnum;
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

    public VipBean getVip() {
        return vip;
    }

    public void setVip(VipBean vip) {
        this.vip = vip;
    }

    public LiangBean getLiang() {
        return liang;
    }

    public void setLiang(LiangBean liang) {
        this.liang = liang;
    }

    public String getIslive() {
        return islive;
    }

    public void setIslive(String islive) {
        this.islive = islive;
    }

    public static class VipBean {
        /**
         * type : 0
         */

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class LiangBean {
        /**
         * name : 0
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;

        }
    }

    public int getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }
}
