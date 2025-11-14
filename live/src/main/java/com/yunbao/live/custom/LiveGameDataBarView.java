package com.yunbao.live.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yunbao.common.custom.MyLinearLayout7;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveGameDataBean;
import com.yunbao.live.bean.MultiGroupHistogramChildData;
import com.yunbao.live.bean.MultiGroupHistogramGroupData;

import java.util.ArrayList;
import java.util.List;


public class LiveGameDataBarView extends LinearLayout {
    Context mContext;
    private MyLinearLayout7 include_1;
    private MyLinearLayout7 include_2;
    private MyLinearLayout7 include_3;
    private MyLinearLayout7 include_4;
    private TextView tv_data1_1;
    private TextView tv_data1_2;
    private View view1_1;
    private View view1_2;
    private TextView tv_game_data_type1;
    private TextView tv_data2_1;
    private TextView tv_data2_2;
    private View view2_1;
    private View view2_2;
    private TextView tv_game_data_type2;
    private TextView tv_data3_1;
    private TextView tv_data3_2;
    private View view3_1;
    private View view3_2;
    private TextView tv_game_data_type3;
    private TextView tv_data4_1;
    private TextView tv_data4_2;
    private View view4_1;
    private View view4_2;
    private TextView tv_game_data_type4;
    private int childData1Value;
    private int childData2Value;
    private int max = 0;

    public LiveGameDataBarView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public LiveGameDataBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
        setNoData();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_live_game_data_bar, this);
        findView();
    }

    private void findView() {
        include_1 = findViewById(R.id.include_1);
        include_2 = findViewById(R.id.include_2);
        include_3 = findViewById(R.id.include_3);
        include_4 = findViewById(R.id.include_4);

        tv_data1_1 = include_1.findViewById(R.id.tv_data1);
        tv_data1_2 = include_1.findViewById(R.id.tv_data2);
        view1_1 = include_1.findViewById(R.id.view1);
        view1_2 = include_1.findViewById(R.id.view2);
        tv_game_data_type1 = include_1.findViewById(R.id.tv_game_data_type);

        tv_data2_1 = include_2.findViewById(R.id.tv_data1);
        tv_data2_2 = include_2.findViewById(R.id.tv_data2);
        view2_1 = include_2.findViewById(R.id.view1);
        view2_2 = include_2.findViewById(R.id.view2);
        tv_game_data_type2 = include_2.findViewById(R.id.tv_game_data_type);

        tv_data3_1 = include_3.findViewById(R.id.tv_data1);
        tv_data3_2 = include_3.findViewById(R.id.tv_data2);
        view3_1 = include_3.findViewById(R.id.view1);
        view3_2 = include_3.findViewById(R.id.view2);
        tv_game_data_type3 = include_3.findViewById(R.id.tv_game_data_type);

        tv_data4_1 = include_4.findViewById(R.id.tv_data1);
        tv_data4_2 = include_4.findViewById(R.id.tv_data2);
        view4_1 = include_4.findViewById(R.id.view1);
        view4_2 = include_4.findViewById(R.id.view2);
        tv_game_data_type4 = include_4.findViewById(R.id.tv_game_data_type);

    }

    public void reFreshMultiGroupHistogramView(LiveGameDataBean dataBean) {
        try {
            max = 0;
            if (dataBean != null) {
                int groupSize = 4;
                List<MultiGroupHistogramGroupData> groupDataList = new ArrayList<>();
                // 数据
                for (int i = 0; i < groupSize; i++) {
                    List<MultiGroupHistogramChildData> childDataList = new ArrayList<>();
                    MultiGroupHistogramGroupData groupData = new MultiGroupHistogramGroupData();
                    String team_a_id = dataBean.getTeam_a_id();
                    String team_b_id = dataBean.getTeam_b_id();
                    List<LiveGameDataBean.DataBean.TeamStatsBean> team_stats = dataBean.getData().getTeam_stats();
                    String team_id1 = team_stats.get(0).getTeam_id();
                    String team_id2 = team_stats.get(1).getTeam_id();
                    if (team_a_id.equals(team_id1)) {
                        if (i == 0) {
                            groupData.setGroupName("大龙");
                            childData1Value = team_stats.get(0).getBig_dragon_count();
                            childData2Value = team_stats.get(1).getBig_dragon_count();
                        } else if (i == 1) {
                            groupData.setGroupName("小龙");
                            childData1Value = team_stats.get(0).getSmall_dragon_count();
                            childData2Value = team_stats.get(1).getSmall_dragon_count();
                        } else if (i == 2) {
                            groupData.setGroupName("推塔");
                            childData1Value = team_stats.get(0).getTower_success_count();
                            childData2Value = team_stats.get(1).getTower_success_count();
                        } else {
                            groupData.setGroupName("水晶");
                            childData1Value = team_stats.get(0).getInhibitor_success_count();
                            childData2Value = team_stats.get(1).getInhibitor_success_count();
                        }
                    } else {
                        if (i == 0) {
                            groupData.setGroupName("大龙");
                            childData1Value = team_stats.get(1).getBig_dragon_count();
                            childData2Value = team_stats.get(0).getBig_dragon_count();
                        } else if (i == 1) {
                            groupData.setGroupName("小龙");
                            childData1Value = team_stats.get(1).getSmall_dragon_count();
                            childData2Value = team_stats.get(0).getSmall_dragon_count();
                        } else if (i == 2) {
                            groupData.setGroupName("推塔");
                            childData1Value = team_stats.get(1).getTower_success_count();
                            childData2Value = team_stats.get(0).getTower_success_count();
                        } else {
                            groupData.setGroupName("水晶");
                            childData1Value = team_stats.get(1).getInhibitor_success_count();
                            childData2Value = team_stats.get(0).getInhibitor_success_count();
                        }
                    }

                    MultiGroupHistogramChildData childData1 = new MultiGroupHistogramChildData();
                    childData1.setValue(childData1Value);
                    childDataList.add(childData1);
                    MultiGroupHistogramChildData childData2 = new MultiGroupHistogramChildData();
                    childData2.setValue(childData2Value);
                    childDataList.add(childData2);
                    groupData.setChildDataList(childDataList);
                    groupDataList.add(groupData);

                }
                setDataList(groupDataList);
                // 设置直方图颜色
//                int[] color1 = new int[]{mContext.getResources().getColor(R.color.color_orange), mContext.getResources().getColor(R.color.color_orange)};
//                int[] color2 = new int[]{mContext.getResources().getColor(R.color.color_supper_tip_normal), mContext.getResources().getColor(R.color.color_supper_tip_normal)};
//            mLiveGameDataBarView.setHistogramColor(color1, color2);
            } else {
                setNoData();
            }
        } catch (Exception e) {
            AppLog.e("bar1", e.toString());
            setNoData();
        }
    }

    public void setDataList(List<MultiGroupHistogramGroupData> groupDataList) {
        try {
            if (groupDataList != null && !groupDataList.isEmpty()) {
                tv_game_data_type1.setText(groupDataList.get(0).getGroupName());
                tv_game_data_type2.setText(groupDataList.get(1).getGroupName());
                tv_game_data_type3.setText(groupDataList.get(2).getGroupName());
                tv_game_data_type4.setText(groupDataList.get(3).getGroupName());
                tv_data1_1.setText(String.valueOf(groupDataList.get(0).getChildDataList().get(0).getValue()));
                tv_data1_2.setText(String.valueOf(groupDataList.get(0).getChildDataList().get(1).getValue()));
                tv_data2_1.setText(String.valueOf(groupDataList.get(1).getChildDataList().get(0).getValue()));
                tv_data2_2.setText(String.valueOf(groupDataList.get(1).getChildDataList().get(1).getValue()));
                tv_data3_1.setText(String.valueOf(groupDataList.get(2).getChildDataList().get(0).getValue()));
                tv_data3_2.setText(String.valueOf(groupDataList.get(2).getChildDataList().get(1).getValue()));
                tv_data4_1.setText(String.valueOf(groupDataList.get(3).getChildDataList().get(0).getValue()));
                tv_data4_2.setText(String.valueOf(groupDataList.get(3).getChildDataList().get(1).getValue()));

                int data1 = Integer.parseInt(tv_data1_1.getText().toString());
                int data2 = Integer.parseInt(tv_data1_2.getText().toString());
                int data3 = Integer.parseInt(tv_data2_1.getText().toString());
                int data4 = Integer.parseInt(tv_data2_2.getText().toString());
                int data5 = Integer.parseInt(tv_data3_1.getText().toString());
                int data6 = Integer.parseInt(tv_data3_2.getText().toString());
                int data7 = Integer.parseInt(tv_data4_1.getText().toString());
                int data8 = Integer.parseInt(tv_data4_2.getText().toString());

                List<Integer> list = new ArrayList<>();
                list.add(data1);
                list.add(data2);
                list.add(data3);
                list.add(data4);
                list.add(data5);
                list.add(data6);
                list.add(data7);
                list.add(data8);
                for (int i = 1; i < list.size(); i++) {
                    if (max < list.get(i)) {
                        max = list.get(i);
                    }
                }
                AppLog.e("bar2", "max=" + max);
                setBarView(false, list);
            } else {
                setNoData();
            }
        } catch (Exception e) {
            AppLog.e("bar2", e.toString());
            setNoData();
        }
    }

    private void setNoData() {
        tv_game_data_type1.setText("大龙");
        tv_game_data_type2.setText("小龙");
        tv_game_data_type3.setText("推塔");
        tv_game_data_type4.setText("水晶");
        tv_data1_1.setText("0");
        tv_data1_2.setText("0");
        tv_data2_1.setText("0");
        tv_data2_2.setText("0");
        tv_data3_1.setText("0");
        tv_data3_2.setText("0");
        tv_data4_1.setText("0");
        tv_data4_2.setText("0");
        setBarView(true, null);
    }

    private void setBarView(boolean noData, List<Integer> list) {
        try {
            ViewGroup.LayoutParams layoutParams1 = view1_1.getLayoutParams();
            layoutParams1.height = DpUtil.dp2px((noData || list == null) ? 10 : list.get(0) * (70 / max) + 10);
            layoutParams1.width = DpUtil.dp2px(10);
            view1_1.setLayoutParams(layoutParams1);
            ViewGroup.LayoutParams layoutParams2 = view1_2.getLayoutParams();
            layoutParams2.height = DpUtil.dp2px((noData || list == null) ? 10 : list.get(1) * (70 / max) + 10);
            layoutParams2.width = DpUtil.dp2px(10);
            view1_2.setLayoutParams(layoutParams2);
            ViewGroup.LayoutParams layoutParams3 = view2_1.getLayoutParams();
            layoutParams3.height = DpUtil.dp2px((noData || list == null) ? 10 : list.get(2) * (70 / max) + 10);
            layoutParams3.width = DpUtil.dp2px(10);
            view2_1.setLayoutParams(layoutParams3);
            ViewGroup.LayoutParams layoutParams4 = view2_2.getLayoutParams();
            layoutParams4.height = DpUtil.dp2px((noData || list == null) ? 10 : list.get(3) * (70 / max) + 10);
            layoutParams4.width = DpUtil.dp2px(10);
            view2_2.setLayoutParams(layoutParams4);
            ViewGroup.LayoutParams layoutParams5 = view3_1.getLayoutParams();
            layoutParams5.height = DpUtil.dp2px((noData || list == null) ? 10 : list.get(4) * (70 / max) + 10);
            layoutParams5.width = DpUtil.dp2px(10);
            view3_1.setLayoutParams(layoutParams5);
            ViewGroup.LayoutParams layoutParams6 = view3_2.getLayoutParams();
            layoutParams6.height = DpUtil.dp2px((noData || list == null) ? 10 : list.get(5) * (70 / max) + 10);
            layoutParams6.width = DpUtil.dp2px(10);
            view3_2.setLayoutParams(layoutParams6);
            ViewGroup.LayoutParams layoutParams7 = view4_1.getLayoutParams();
            layoutParams7.height = DpUtil.dp2px((noData || list == null) ? 10 : list.get(6) * (70 / max) + 10);
            layoutParams7.width = DpUtil.dp2px(10);
            view4_1.setLayoutParams(layoutParams7);
            ViewGroup.LayoutParams layoutParams8 = view4_2.getLayoutParams();
            layoutParams8.height = DpUtil.dp2px((noData || list == null) ? 10 : list.get(7) * (70 / max) + 10);
            layoutParams8.width = DpUtil.dp2px(10);
            view4_2.setLayoutParams(layoutParams8);
        } catch (Exception e) {
            setNoData();
        }
    }

}
