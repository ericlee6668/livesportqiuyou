package com.yunbao.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by cxf on 2019/7/9.
 */

public class ExponentNumBean implements Parcelable {

   private String matchNumber;
   private boolean isChoose;

   public ExponentNumBean(){

   }

    protected ExponentNumBean(Parcel in) {
        matchNumber = in.readString();
        isChoose = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(matchNumber);
        dest.writeByte((byte) (isChoose ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExponentNumBean> CREATOR = new Creator<ExponentNumBean>() {
        @Override
        public ExponentNumBean createFromParcel(Parcel in) {
            return new ExponentNumBean(in);
        }

        @Override
        public ExponentNumBean[] newArray(int size) {
            return new ExponentNumBean[size];
        }
    };

    public String getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(String matchNumber) {
        this.matchNumber = matchNumber;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
