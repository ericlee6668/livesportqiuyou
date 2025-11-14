package com.yunbao.live.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/5 22:26
 *
 * @package: com.yunbao.live.bean
 * Description：TODO
 */
public class MatchLiveBean implements Parcelable {
    private String uid;
    private String showid;
    private String islive;
    private String starttime;
    private String title;
    private String province;
    private String city;
    private String txzb_order;
    private String stream;
    private String thumb;
    private String pull;
    private String third_pull;
    private String lng;
    private String lat;
    private String type;
    private String type_val;
    private String isvideo;
    private String wy_cid;
    private String goodnum;
    private String anyway;
    private String liveclassid;
    private String hotvotes;
    private String pkuid;
    private String pkstream;
    private String ismic;
    private String ishot;
    private String isrecommend;
    private String deviceinfo;
    private String isshop;
    private String game_action;
    private String banker_coin;
    private String isoff;
    private String offtime;
    private String recom_sort;
    private String notice;
    private String match_id;
    private String pic_full_url;
    private String isrecord;
    private String recordid;
    private int is_chat_off;
    private String is_manual_close;
    private User user;


    protected MatchLiveBean(Parcel in) {
        uid = in.readString();
        showid = in.readString();
        islive = in.readString();
        starttime = in.readString();
        title = in.readString();
        province = in.readString();
        city = in.readString();
        txzb_order = in.readString();
        stream = in.readString();
        thumb = in.readString();
        pull = in.readString();
        third_pull = in.readString();
        lng = in.readString();
        lat = in.readString();
        type = in.readString();
        type_val = in.readString();
        isvideo = in.readString();
        wy_cid = in.readString();
        goodnum = in.readString();
        anyway = in.readString();
        liveclassid = in.readString();
        hotvotes = in.readString();
        pkuid = in.readString();
        pkstream = in.readString();
        ismic = in.readString();
        ishot = in.readString();
        isrecommend = in.readString();
        deviceinfo = in.readString();
        isshop = in.readString();
        game_action = in.readString();
        banker_coin = in.readString();
        isoff = in.readString();
        offtime = in.readString();
        recom_sort = in.readString();
        notice = in.readString();
        match_id = in.readString();
        pic_full_url = in.readString();
        isrecord = in.readString();
        recordid = in.readString();
        is_chat_off = in.readInt();
        is_manual_close = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(showid);
        dest.writeString(islive);
        dest.writeString(starttime);
        dest.writeString(title);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(txzb_order);
        dest.writeString(stream);
        dest.writeString(thumb);
        dest.writeString(pull);
        dest.writeString(third_pull);
        dest.writeString(lng);
        dest.writeString(lat);
        dest.writeString(type);
        dest.writeString(type_val);
        dest.writeString(isvideo);
        dest.writeString(wy_cid);
        dest.writeString(goodnum);
        dest.writeString(anyway);
        dest.writeString(liveclassid);
        dest.writeString(hotvotes);
        dest.writeString(pkuid);
        dest.writeString(pkstream);
        dest.writeString(ismic);
        dest.writeString(ishot);
        dest.writeString(isrecommend);
        dest.writeString(deviceinfo);
        dest.writeString(isshop);
        dest.writeString(game_action);
        dest.writeString(banker_coin);
        dest.writeString(isoff);
        dest.writeString(offtime);
        dest.writeString(recom_sort);
        dest.writeString(notice);
        dest.writeString(match_id);
        dest.writeString(pic_full_url);
        dest.writeString(isrecord);
        dest.writeString(recordid);
        dest.writeInt(is_chat_off);
        dest.writeString(is_manual_close);
        dest.writeParcelable(user, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MatchLiveBean> CREATOR = new Creator<MatchLiveBean>() {
        @Override
        public MatchLiveBean createFromParcel(Parcel in) {
            return new MatchLiveBean(in);
        }

        @Override
        public MatchLiveBean[] newArray(int size) {
            return new MatchLiveBean[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShowid() {
        return showid;
    }

    public void setShowid(String showid) {
        this.showid = showid;
    }

    public String getIslive() {
        return islive;
    }

    public void setIslive(String islive) {
        this.islive = islive;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTxzb_order() {
        return txzb_order;
    }

    public void setTxzb_order(String txzb_order) {
        this.txzb_order = txzb_order;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPull() {
        return pull;
    }

    public void setPull(String pull) {
        this.pull = pull;
    }

    public String getThird_pull() {
        return third_pull;
    }

    public void setThird_pull(String third_pull) {
        this.third_pull = third_pull;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
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

    public String getIsvideo() {
        return isvideo;
    }

    public void setIsvideo(String isvideo) {
        this.isvideo = isvideo;
    }

    public String getWy_cid() {
        return wy_cid;
    }

    public void setWy_cid(String wy_cid) {
        this.wy_cid = wy_cid;
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

    public String getLiveclassid() {
        return liveclassid;
    }

    public void setLiveclassid(String liveclassid) {
        this.liveclassid = liveclassid;
    }

    public String getHotvotes() {
        return hotvotes;
    }

    public void setHotvotes(String hotvotes) {
        this.hotvotes = hotvotes;
    }

    public String getPkuid() {
        return pkuid;
    }

    public void setPkuid(String pkuid) {
        this.pkuid = pkuid;
    }

    public String getPkstream() {
        return pkstream;
    }

    public void setPkstream(String pkstream) {
        this.pkstream = pkstream;
    }

    public String getIsmic() {
        return ismic;
    }

    public void setIsmic(String ismic) {
        this.ismic = ismic;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public String getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(String isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getDeviceinfo() {
        return deviceinfo;
    }

    public void setDeviceinfo(String deviceinfo) {
        this.deviceinfo = deviceinfo;
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

    public String getBanker_coin() {
        return banker_coin;
    }

    public void setBanker_coin(String banker_coin) {
        this.banker_coin = banker_coin;
    }

    public String getIsoff() {
        return isoff;
    }

    public void setIsoff(String isoff) {
        this.isoff = isoff;
    }

    public String getOfftime() {
        return offtime;
    }

    public void setOfftime(String offtime) {
        this.offtime = offtime;
    }

    public String getRecom_sort() {
        return recom_sort;
    }

    public void setRecom_sort(String recom_sort) {
        this.recom_sort = recom_sort;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getPic_full_url() {
        return pic_full_url;
    }

    public void setPic_full_url(String pic_full_url) {
        this.pic_full_url = pic_full_url;
    }

    public String getIsrecord() {
        return isrecord;
    }

    public void setIsrecord(String isrecord) {
        this.isrecord = isrecord;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public int getIs_chat_off() {
        return is_chat_off;
    }

    public void setIs_chat_off(int is_chat_off) {
        this.is_chat_off = is_chat_off;
    }

    public String getIs_manual_close() {
        return is_manual_close;
    }

    public void setIs_manual_close(String is_manual_close) {
        this.is_manual_close = is_manual_close;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User implements Parcelable{
        private String id;
        private String user_nicename;
        private String avatar;
        private String avatar_thumb;
        private String viewnum;

        protected User(Parcel in) {
            id = in.readString();
            user_nicename = in.readString();
            avatar = in.readString();
            avatar_thumb = in.readString();
            viewnum = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(user_nicename);
            dest.writeString(avatar);
            dest.writeString(avatar_thumb);
            dest.writeString(viewnum);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };

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

        public String getViewnum() {
            return viewnum;
        }

        public void setViewnum(String viewnum) {
            this.viewnum = viewnum;
        }
    }
}
