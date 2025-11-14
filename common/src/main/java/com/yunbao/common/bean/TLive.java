package com.yunbao.common.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Objects;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/27 18:17
 *
 * @package: com.yunbao.common.bean
 * Description：文字直播
 * *************************事件类型*****************************
 * 状态码	描述
 * 1	犯规
 * 2	个人犯规
 * 3	侵犯对手/受伤换人
 * 4	战术犯规/战术换人
 * 5	进攻犯规
 * 6	无球犯规
 * 7	持续犯规
 * 8	持续侵犯
 * 9	暴力行为
 * 10	危险动作
 * 11	手球犯规
 * 12	严重犯规
 * 13	故意犯规（防守球员为最后一名防守人时）
 * 14	阻挡进球机会
 * 15	拖延时间
 * 16	视频回看裁定
 * 17	判罚取消
 * 18	争论
 * 19	对判罚表达异议
 * 20	犯规和攻击言语
 * 21	过度庆祝
 * 22	没有回退到要求的距离
 * 23	打架
 * 24	辅助判罚
 * 25	替补席
 * 26	赛后行为
 * 27	其他原因
 * 28	未被允许进入场地
 * 29	进入比赛场地
 * 30	离开比赛赛场
 * 31	非体育道德行为
 * 32	未被发现的犯规
 * 33	冒充或顶替（指球员与球衣不是同一人上场比赛）
 * 34	干预var复审
 * 35	进入裁判评审区
 * 36	吐口水（向球员或裁判）
 * 37	病毒
 * 0	未知
 */
public class TLive implements MultiItemEntity {
    private int main = 0;//是否重要事件
    private String data = null;
    private int position = 0;//事件发生方，0-中立 1-主队 2-客队
    private int type = 0;//事件类型
    private String time = "";//事件时间

    public int getMain() {
        return main;
    }

    public void setMain(int main) {
        this.main = main;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TLive tLive = (TLive) o;
        return main == tLive.main &&
                position == tLive.position &&
                type == tLive.type &&
                Objects.equals(data, tLive.data) &&
                Objects.equals(time, tLive.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(main, data, position, type, time);
    }
}
