package com.yunbao.video.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.ImChatFacePagerAdapter;
import com.yunbao.common.interfaces.OnFaceClickListener;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.video.R;
import com.yunbao.video.bean.VideoCommentBean;
import com.yunbao.video.dialog.VideoInputDialogFragment;

public class AbsVideoPlayHolder  extends AbsViewHolder implements View.OnClickListener, OnFaceClickListener {
    protected VideoCommentViewHolder mVideoCommentViewHolder;
    protected VideoInputDialogFragment mVideoInputDialogFragment;
    private View mFaceView;//表情面板
    private int mFaceHeight;//表情面板高度

    public AbsVideoPlayHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_send) {
            if (mVideoInputDialogFragment != null) {
                mVideoInputDialogFragment.sendComment();
            }
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
        fragment.show(((AppCompatActivity)mContext).getSupportFragmentManager(), "VideoInputDialogFragment");
    }


    public View getFaceView() {
        if (mFaceView == null) {
            mFaceView = initFaceView();
        }
        return mFaceView;
    }

    /**
     * 初始化表情控件
     */
    private View initFaceView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.view_chat_face, null);
        v.measure(0, 0);
        mFaceHeight = v.getMeasuredHeight();
        v.findViewById(R.id.btn_send).setOnClickListener(this);
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

    /**
     * 显示评论
     */
    public void openCommentWindow(String videoId, String videoUid) {
        if (mVideoCommentViewHolder == null) {
            mVideoCommentViewHolder = new VideoCommentViewHolder(mContext, (ViewGroup) findViewById(R.id.root));
            mVideoCommentViewHolder.addToParent();
        }
        mVideoCommentViewHolder.setVideoInfo(videoId, videoUid);
        mVideoCommentViewHolder.showBottom();
    }

    /**
     * 隐藏评论
     */
    public void hideCommentWindow(boolean commentSuccess) {
        if (mVideoCommentViewHolder != null) {
            mVideoCommentViewHolder.hideBottom();
            if (commentSuccess) {
                mVideoCommentViewHolder.needRefresh();
            }
        }
        mVideoInputDialogFragment = null;
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
    protected int getLayoutId() {
        return R.layout.view_video_play_wrap;
    }

    @Override
    public void init() {

    }

    public void release() {
        if (mVideoCommentViewHolder != null) {
            mVideoCommentViewHolder.release();
        }

        mVideoCommentViewHolder = null;
        mVideoInputDialogFragment = null;
    }
}
