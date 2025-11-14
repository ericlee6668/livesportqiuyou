package com.yunbao.main.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.live.bean.BasketPushData;
import com.yunbao.live.bean.FootballLive;
import com.yunbao.live.event.MatchData;
import com.yunbao.main.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhp on 2020/9/18.
 * 赛事
 */

public class MainGameViewHolder extends AbsMainViewHolder implements View.OnClickListener {

    private static final int PAGE_COUNT = 4;
    private List<FrameLayout> mViewList;
    private ViewPager mViewPager;
    private TextView tvFootball;
    private TextView tvBasketball;
    private TextView tvLol;
    private TextView tvOther;
    private LinearLayout llFootball;
    private LinearLayout llBasketball;
    private LinearLayout llLol;
    private LinearLayout llOther;
    private AllGameViewHolder allGameViewHolder;
    private GameIngViewHolder gameIngViewHolder;
    private GameTimeViewHolder gameTimeViewHolder;
    private GameResultViewHolder gameResultViewHolder;
    private AbsMainViewHolder[] mViewHolders;
    private int currGameType;


    public MainGameViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_game;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        llFootball = findViewById(R.id.ll_football);
        llBasketball = findViewById(R.id.ll_basketball);
        llLol = findViewById(R.id.ll_Lol);
        llOther = findViewById(R.id.ll_other);
        tvFootball = findViewById(R.id.tv_football);
        tvBasketball = findViewById(R.id.tv_basketball);
        tvLol = findViewById(R.id.tv_Lol);
        tvOther = findViewById(R.id.tv_other);
    }

    private void initData() {
        mViewList = new ArrayList<>();
        for (int i = 0; i < PAGE_COUNT; i++) {
            FrameLayout frameLayout = new FrameLayout(mContext);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mViewList.add(frameLayout);
        }
        mViewHolders = new AbsMainViewHolder[PAGE_COUNT];
        TabLayout mTableLayout = (TabLayout) findViewById(R.id.tableLayout);
        String[] tabTitle = {mContext.getString(R.string.all), mContext.getString(R.string.ongoing), mContext.getString(R.string.game_schedule), mContext.getString(R.string.game_result)};
        for (String aTabTitle : tabTitle) {
            mTableLayout.addTab(mTableLayout.newTab().setText(aTabTitle));
        }
        mTableLayout.setTabMode(TabLayout.MODE_FIXED);
        mTableLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //设置顶部标签指示条的颜色和选中页面时标签字体颜色
        mTableLayout.setTabTextColors(mContext.getResources().getColor(R.color.black2), mContext.getResources().getColor(R.color.global));
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(PAGE_COUNT - 1);
        mViewPager.setAdapter(new ViewPagerAdapter(mViewList));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTableLayout));
        mTableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //在选中的顶部标签时，为viewpager设置currentitem
                mViewPager.setCurrentItem(tab.getPosition());
                loadPageData(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initListener() {
        llFootball.setOnClickListener(this);
        llBasketball.setOnClickListener(this);
        llLol.setOnClickListener(this);
        llOther.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_football) {
            showTopTabView(0);
        } else if (id == R.id.ll_basketball) {
            showTopTabView(1);
        } else if (id == R.id.ll_Lol) {
            showTopTabView(2);
        } else if (id == R.id.ll_other) {
            showTopTabView(3);
        }
    }

    /**
     * 切换游戏分类tab
     *
     * @param position
     */
    private void showTopTabView(int position) {
        currGameType = position;
        tvFootball.setBackgroundResource(position == 0 ? R.drawable.bg_shape_rounded_corners_red : R.color.white);
        tvBasketball.setBackgroundResource(position == 1 ? R.drawable.bg_shape_rounded_corners_red : R.color.white);
        tvLol.setBackgroundResource(position == 2 ? R.drawable.bg_shape_rounded_corners_red : R.color.white);
        tvOther.setBackgroundResource(position == 3 ? R.drawable.bg_shape_rounded_corners_red : R.color.white);
        tvFootball.setTextColor(position == 0 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.black2));
        tvBasketball.setTextColor(position == 1 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.black2));
        tvLol.setTextColor(position == 2 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.black2));
        tvOther.setTextColor(position == 3 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.black2));
        tvFootball.setTextSize(position == 0 ? 20 : 18);
        tvBasketball.setTextSize(position == 1 ? 20 : 18);
        tvLol.setTextSize(position == 2 ? 20 : 18);
        tvOther.setTextSize(position == 3 ? 20 : 18);
        //切换赛事状态tab
        mViewPager.setCurrentItem(0);
        loadPageData(0);
    }

    private void loadPageData(int position) {
        if (mViewHolders == null) {
            return;
        }
        AbsMainViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    allGameViewHolder = new AllGameViewHolder(mContext, parent);
                    vh = allGameViewHolder;
                } else if (position == 1) {
                    gameIngViewHolder = new GameIngViewHolder(mContext, parent);
                    vh = gameIngViewHolder;
                } else if (position == 2) {
                    gameTimeViewHolder = new GameTimeViewHolder(mContext, parent);
                    vh = gameTimeViewHolder;
                } else if (position == 3) {
                    gameResultViewHolder = new GameResultViewHolder(mContext, parent);
                    vh = gameResultViewHolder;
                }
                if (vh == null) {
                    return;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
                vh.subscribeActivityLifeCycle();
            }
        }
        if (vh != null) {
            vh.loadData(currGameType);
        }
    }


    @Override
    public void loadData() {
        if (mViewPager != null) {
            loadPageData(mViewPager.getCurrentItem());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiverFootballChange(MatchData matchData) {
        if (matchData.getType() == 1) {
            List<FootballLive> changeList = matchData.getFootballList();
            if (allGameViewHolder != null) {
                allGameViewHolder.refreshFootballData(changeList);
            }

            if (gameIngViewHolder != null) {
                gameIngViewHolder.refreshFootballData(changeList);
            }
        } else {
            List<BasketPushData> changeList = matchData.getBasketballList();
            if (allGameViewHolder != null) {
                allGameViewHolder.refreshBasketBallData(changeList);
            }

            if (gameIngViewHolder != null) {
                gameIngViewHolder.refreshBasketballData(changeList);
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
