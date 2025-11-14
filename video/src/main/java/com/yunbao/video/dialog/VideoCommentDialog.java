package com.yunbao.video.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.adapter.ImChatFacePagerAdapter;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.MyLinearLayout3;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnFaceClickListener;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.video.R;
import com.yunbao.video.adapter.VideoCommentAdapter;
import com.yunbao.video.bean.VideoCommentBean;
import com.yunbao.video.event.SendCommentEvent;
import com.yunbao.video.event.VideoCommentEvent;
import com.yunbao.video.http.VideoHttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;

public class VideoCommentDialog extends AbsDialogFragment implements View.OnClickListener, OnItemClickListener<VideoCommentBean>, VideoCommentAdapter.ActionListener, OnFaceClickListener {
    private View mRoot;
    private MyLinearLayout3 mBottom;
    private CommonRefreshView mRefreshView;
    private TextView mCommentNum;
    private VideoCommentAdapter mVideoCommentAdapter;
    private String mVideoId;
    private String mVideoUid;
    private String mCommentString;
    private int mFaceHeight;
    private View mFaceView;
    private VideoInputDialogFragment mVideoInputDialogFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_comment;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.bottomToTopAnim;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DpUtil.dip2px(520);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.input).setOnClickListener(this);
        findViewById(R.id.btn_face).setOnClickListener(this);
        mCommentString = WordUtil.getString(R.string.video_comment);
        mCommentNum = (TextView) findViewById(R.id.comment_num);
        mRefreshView = (CommonRefreshView) findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_comment);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (Exception e) {
                    L.e("onLayoutChildren------>" + e.getMessage());
                }
            }
        });

        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<VideoCommentBean>() {
            @Override
            public RefreshAdapter<VideoCommentBean> getAdapter() {
                if (mVideoCommentAdapter == null) {
                    mVideoCommentAdapter = new VideoCommentAdapter(mContext);
                    mVideoCommentAdapter.setOnItemClickListener(VideoCommentDialog.this);
                    mVideoCommentAdapter.setActionListener(VideoCommentDialog.this);
                }
                return mVideoCommentAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                if (!TextUtils.isEmpty(mVideoId)) {
                    VideoHttpUtil.getVideoCommentList(mVideoId, p, callback);
                }
            }

            @Override
            public List<VideoCommentBean> processData(String[] info) {
                JSONObject obj = JSON.parseObject(info[0]);
                String commentNum = obj.getString("comments");
                EventBus.getDefault().post(new VideoCommentEvent(mVideoId, commentNum));
                if (mCommentNum != null) {
                    mCommentNum.setText(commentNum + " " + mCommentString);
                }
                List<VideoCommentBean> list = JSON.parseArray(obj.getString("commentlist"), VideoCommentBean.class);
                for (VideoCommentBean bean : list) {
                    if (bean != null) {
                        bean.setParentNode(true);
                    }
                }
                return list;
            }

            @Override
            public void onRefreshSuccess(List<VideoCommentBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<VideoCommentBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        mRefreshView.initData();
    }
    @Subscribe
    public void onCommentEvent(SendCommentEvent event){
        mRefreshView.initData();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.root || i == R.id.btn_close) {
            dismiss();
        } else if (i == R.id.input) {
            if(TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())){
                RouteUtil.forwardLogin("");
                return;
            }
            if (!TextUtils.isEmpty(mVideoId) && !TextUtils.isEmpty(mVideoUid)) {
                openCommentInputWindow(false, mVideoId, mVideoUid, null);
            }
        } else if (i == R.id.btn_face) {
            if (!TextUtils.isEmpty(mVideoId) && !TextUtils.isEmpty(mVideoUid)) {
                openCommentInputWindow(true, mVideoId, mVideoUid, null);
            }
        }
    }

    public void setVideoInfo(String videoId, String videoUid) {
        mVideoId = videoId;
        mVideoUid = videoUid;
    }

    public View getFaceView() {
        if (mFaceView == null) {
            mFaceView = initFaceView();
        }
        return mFaceView;
    }

    @Override
    public void onItemClick(VideoCommentBean bean, int position) {
        if (!TextUtils.isEmpty(mVideoId) && !TextUtils.isEmpty(mVideoUid)) {
            openCommentInputWindow(false, mVideoId, mVideoUid, bean);
        }
    }

    /**
     * 打开评论输入框
     */
    public void openCommentInputWindow(boolean openFace, String videoId, String videoUid, VideoCommentBean bean) {
        if (mFaceView == null) {
            mFaceView = initFaceView();
        }
        VideoInputDialogFragment fragment = new VideoInputDialogFragment(mFaceView);
        fragment.setVideoInfo(videoId, videoUid);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.VIDEO_FACE_OPEN, openFace);
        bundle.putInt(Constants.VIDEO_FACE_HEIGHT, mFaceHeight);
        bundle.putParcelable(Constants.VIDEO_COMMENT_BEAN, bean);
        fragment.setArguments(bundle);
        mVideoInputDialogFragment = fragment;
        fragment.show(getChildFragmentManager(), "VideoInputDialogFragment");
    }


    @Override
    public void onExpandClicked(final VideoCommentBean commentBean) {
        final VideoCommentBean parentNodeBean = commentBean.getParentNodeBean();
        if (parentNodeBean == null) {
            return;
        }
        VideoHttpUtil.getCommentReply(parentNodeBean.getId(), parentNodeBean.getChildPage(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<VideoCommentBean> list = JSON.parseArray(Arrays.toString(info), VideoCommentBean.class);
                    if (list == null || list.size() == 0) {
                        return;
                    }
                    if (parentNodeBean.getChildPage() == 1) {
                        if (list.size() > 1) {
                            list = list.subList(1, list.size());
                        }
                    }
                    for (VideoCommentBean bean : list) {
                        bean.setParentNodeBean(parentNodeBean);
                    }
                    List<VideoCommentBean> childList = parentNodeBean.getChildList();
                    if (childList != null) {
                        childList.addAll(list);
                        if (childList.size() < parentNodeBean.getReplyNum()) {
                            parentNodeBean.setChildPage(parentNodeBean.getChildPage() + 1);
                        }
                        if (mVideoCommentAdapter != null) {
                            mVideoCommentAdapter.insertReplyList(commentBean, list.size());
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCollapsedClicked(VideoCommentBean commentBean) {
        VideoCommentBean parentNodeBean = commentBean.getParentNodeBean();
        if (parentNodeBean == null) {
            return;
        }
        List<VideoCommentBean> childList = parentNodeBean.getChildList();
        VideoCommentBean node0 = childList.get(0);
        int orignSize = childList.size();
        parentNodeBean.removeChild();
        parentNodeBean.setChildPage(1);
        if (mVideoCommentAdapter != null) {
            mVideoCommentAdapter.removeReplyList(node0, orignSize - childList.size());
        }
    }

    /**
     * 初始化表情控件
     */
    private View initFaceView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.view_chat_face, null);
        v.measure(0, 0);
        mFaceHeight = v.getMeasuredHeight();
        v.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoInputDialogFragment != null) {
                    mVideoInputDialogFragment.sendComment();
                }
            }
        });
        final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(10);
        ImChatFacePagerAdapter adapter = new ImChatFacePagerAdapter(mContext, this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0, pageCount = adapter.getCount(); i < pageCount; i++) {
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.view_chat_indicator, radioGroup, false);
            radioButton.setId(i + 10000);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            radioGroup.addView(radioButton);
        }
        return v;
    }


    @Override
    public void onFaceClick(String str, int faceImageRes) {
        if (mVideoInputDialogFragment != null) {
            mVideoInputDialogFragment.onFaceClick(str, faceImageRes);
        }
    }

    @Override
    public void onFaceDeleteClick() {
        if (mVideoInputDialogFragment != null) {
            mVideoInputDialogFragment.onFaceDeleteClick();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
