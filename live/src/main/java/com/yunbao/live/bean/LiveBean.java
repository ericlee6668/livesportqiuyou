package com.yunbao.live.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.R;

/**
 * Created by cxf on 2017/8/9.
 */

public class LiveBean implements Parcelable {
    private String uid;
    private String avatar;
    private String avatarThumb;
    private String userNiceName;
    private String title;
    private String city;
    private String stream;
    private String pull;
    private String thumb;
    private String nums;
    private int sex;
    private String distance;
    private int levelAnchor;
    private int type;
    private String typeVal;
    private String goodNum;//主播的靓号
    private int gameAction;//正在进行的游戏的标识
    private String game;
    private int isshop;
    private String match_id;
    private String liveclassid;

    protected LiveBean(Parcel in) {
        uid = in.readString();
        avatar = in.readString();
        avatarThumb = in.readString();
        userNiceName = in.readString();
        title = in.readString();
        city = in.readString();
        stream = in.readString();
        pull = in.readString();
        thumb = in.readString();
        nums = in.readString();
        sex = in.readInt();
        distance = in.readString();
        levelAnchor = in.readInt();
        type = in.readInt();
        typeVal = in.readString();
        goodNum = in.readString();
        gameAction = in.readInt();
        game = in.readString();
        isshop = in.readInt();
        match_id = in.readString();
        liveclassid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(avatar);
        dest.writeString(avatarThumb);
        dest.writeString(userNiceName);
        dest.writeString(title);
        dest.writeString(city);
        dest.writeString(stream);
        dest.writeString(pull);
        dest.writeString(thumb);
        dest.writeString(nums);
        dest.writeInt(sex);
        dest.writeString(distance);
        dest.writeInt(levelAnchor);
        dest.writeInt(type);
        dest.writeString(typeVal);
        dest.writeString(goodNum);
        dest.writeInt(gameAction);
        dest.writeString(game);
        dest.writeInt(isshop);
        dest.writeString(match_id);
        dest.writeString(liveclassid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LiveBean> CREATOR = new Creator<LiveBean>() {
        @Override
        public LiveBean createFromParcel(Parcel in) {
            return new LiveBean(in);
        }

        @Override
        public LiveBean[] newArray(int size) {
            return new LiveBean[size];
        }
    };

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getLiveclassid() {
        return liveclassid;
    }

    public void setLiveclassid(String liveclassid) {
        this.liveclassid = liveclassid;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    @JSONField(name = "avatar_thumb")
    public String getAvatarThumb() {
        return avatarThumb;
    }

    @JSONField(name = "avatar_thumb")
    public void setAvatarThumb(String avatarThumb) {
        this.avatarThumb = avatarThumb;
    }

    @JSONField(name = "user_nicename")
    public String getUserNiceName() {
        return userNiceName;
    }

    @JSONField(name = "user_nicename")
    public void setUserNiceName(String userNiceName) {
        this.userNiceName = userNiceName;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }


    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    @JSONField(name = "level_anchor")
    public int getLevelAnchor() {
        return levelAnchor;
    }

    @JSONField(name = "level_anchor")
    public void setLevelAnchor(int levelAnchor) {
        this.levelAnchor = levelAnchor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JSONField(name = "type_val")
    public String getTypeVal() {
        return typeVal;
    }

    @JSONField(name = "type_val")
    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
    }

    @JSONField(name = "goodnum")
    public String getGoodNum() {
        return goodNum;
    }

    @JSONField(name = "goodnum")
    public void setGoodNum(String goodNum) {
        this.goodNum = goodNum;
    }

    @JSONField(name = "game_action")
    public int getGameAction() {
        return gameAction;
    }

    @JSONField(name = "game_action")
    public void setGameAction(int gameAction) {
        this.gameAction = gameAction;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * 显示靓号
     */
    public String getLiangNameTip() {
        if (!TextUtils.isEmpty(this.goodNum) && !"0".equals(this.goodNum)) {
            return WordUtil.getString(R.string.live_liang) + ":" + this.goodNum;
        }
        return "ID:" + this.uid;
    }

    public LiveBean() {

    }


    public int getIsshop() {
        return isshop;
    }

    public void setIsshop(int isshop) {
        this.isshop = isshop;
    }

}
