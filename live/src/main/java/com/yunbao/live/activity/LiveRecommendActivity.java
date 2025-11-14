package com.yunbao.live.activity;

import android.content.Intent;

import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.MainRecommendBean;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.CommonRefreshViewNewX;
import com.yunbao.common.http.DataNewX;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.http.HttpCallbackNewX;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.live.R;
import com.yunbao.live.adapter.LiveRecommendAdapter;
import com.yunbao.live.adapterX.LiveRecommendAdapterNewX;
import com.yunbao.live.http.LiveHttpUtil;
import com.yunbao.live.httpX.LiveHttpUtilNewX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Juwan
 * @date 2020/9/22
 * Description:
 */
public class LiveRecommendActivity extends AbsActivity {
//     private LiveRecommendAdapter adapter;
//    List<MainRecommendBean> mainRecommendBeanList = new ArrayList<>();

    private LiveRecommendAdapterNewX adapter;

    List<DataNewX.DataDTO> mainRecommendBeanList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_recommend;
    }


    @Override
    protected void main() {
        setRecommendList();
        setTitle("推荐直播");
    }

    private void setRecommendList() {

        CommonRefreshViewNewX mRefreshView = findViewById(R.id.live_refreshView_recommend);
        mRefreshView.setEmptyLayoutId(R.layout.layout_empty_view);
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2));


        mRefreshView.setDataHelper(new CommonRefreshViewNewX.DataHelperNewX<DataNewX.DataDTO>() {

            @Override
            public RefreshAdapter<DataNewX.DataDTO> getAdapterX() {
                if (adapter == null) {
                    adapter = new LiveRecommendAdapterNewX(mContext);
                    adapter.setOnItemClickListener(new OnItemClickListener<DataNewX.DataDTO>() {
                        @Override
                        public void onItemClick(DataNewX.DataDTO bean, int position) {
                            MainRecommendBean mainRecommendBean = new MainRecommendBean();
                            mainRecommendBean.setAvatar(bean.getUser().getAvatar());
                            mainRecommendBean.setAvatar_thumb(bean.getUser().getAvatarThumb());
                            mainRecommendBean.setUser_nicename(bean.getUser().getUserNicename());
                            //TODO 必传字段
                            mainRecommendBean.setLiveclassid(bean.getLiveclassid());
                            mainRecommendBean.setUid(bean.getUid());
                            mainRecommendBean.setTitle(bean.getTitle());
                            mainRecommendBean.setStream(bean.getStream());
                            mainRecommendBean.setPull(bean.getPull());
                            mainRecommendBean.setIsvideo(bean.getIsvideo());
                            mainRecommendBean.setAnyway(bean.getAnyway());
                            mainRecommendBean.setMatch_id(bean.getMatchId());
                            mainRecommendBean.setIs_chat_off(Integer.parseInt(bean.getIsChatOff()));
                            mainRecommendBean.setViewnum( bean.getUser().getViewnum());
                            Intent intent = new Intent(mContext, LiveGameActivity.class);
                            intent.putExtra(Constants.LIVE_RECOMMEND_DATA, mainRecommendBean);
                            mContext.startActivity(intent);
                        }
                    });
                }
                return adapter;
            }


            @Override
            public void loadData(int p, HttpCallbackNewX callback) {
                if (p == 1) {
                    mainRecommendBeanList.clear();
                }

                LiveHttpUtilNewX.getHomeHotNewX(p, 12, callback);//请求网络
            }

            @Override
            public List<DataNewX.DataDTO> processData(List<DataNewX.DataDTO> mainRecommendBeans) {
                if (mainRecommendBeanList != null && !mainRecommendBeanList.isEmpty()) {
                    for (int i = 0; i < mainRecommendBeans.size(); i++) {
                        for (int j = 0; j < mainRecommendBeanList.size(); j++) {
                            if (mainRecommendBeanList.get(j).getUid().equals(mainRecommendBeans.get(i).getUid())) {
                                mainRecommendBeans.remove(i);
                            }
                        }
                    }
                }
                if (mainRecommendBeanList != null) {
                    mainRecommendBeanList.addAll(mainRecommendBeans);
                }
                return mainRecommendBeans;
            }

            @Override
            public void onRefreshSuccess(List<DataNewX.DataDTO> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<DataNewX.DataDTO> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {
            }
        });
        mRefreshView.initData();
    }
}
