package com.yunbao.live.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.Constants;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveAdminListActivity;
import com.yunbao.live.http.LiveHttpUtil;

public class LiveRoomChatManagerDialog extends AbsDialogFragment {

    private String mLiveUid;
    private String mToUid;
    private String mStream;
    private RelativeLayout mBtnAdmin;
    private RelativeLayout mBtnUserAdminList;
    private RelativeLayout mBtnUserBanned;
    private RelativeLayout mBtnUserCancel;
    private int mType;
    private TextView mTvLiveSetIsAdmin;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_room_chat_manager;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        window.setWindowAnimations(R.style.bottomToTopAnim);
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mLiveUid = bundle.getString(Constants.LIVE_UID);
        mToUid = bundle.getString(Constants.TO_UID);
        mType = bundle.getInt(Constants.LIVE_TYPE, 0);
        if (TextUtils.isEmpty(mLiveUid) || TextUtils.isEmpty(mToUid)) {
            return;
        }
        mStream = bundle.getString(Constants.STREAM);
        mBtnAdmin = mRootView.findViewById(R.id.btn_admin);
        mBtnUserAdminList = mRootView.findViewById(R.id.btn_user_admin_list);
        mBtnUserBanned = mRootView.findViewById(R.id.btn_user_banned);
        mBtnUserCancel = mRootView.findViewById(R.id.btn_user_cancel);
        mTvLiveSetIsAdmin = mRootView.findViewById(R.id.tv_live_set_is_admin);
        if (mType == 1) {
            //主播点普通观众
            mTvLiveSetIsAdmin.setText(R.string.live_set_is_admin);
            mBtnAdmin.setVisibility(View.VISIBLE);
            mBtnUserAdminList.setVisibility(View.VISIBLE);
            mBtnUserBanned.setVisibility(View.VISIBLE);
            mBtnUserCancel.setVisibility(View.VISIBLE);
        } else if (mType == 2) {
            //主播点房间管理员
            mTvLiveSetIsAdmin.setText(R.string.live_set_cancel_admin);
            mBtnAdmin.setVisibility(View.VISIBLE);
            mBtnUserAdminList.setVisibility(View.GONE);
            mBtnUserBanned.setVisibility(View.VISIBLE);
            mBtnUserCancel.setVisibility(View.VISIBLE);
        } else if (mType == 3) {
            //房间管理员点普通观众
            mBtnAdmin.setVisibility(View.GONE);
            mBtnUserAdminList.setVisibility(View.GONE);
            mBtnUserBanned.setVisibility(View.VISIBLE);
            mBtnUserCancel.setVisibility(View.VISIBLE);
        } else {
            mBtnAdmin.setVisibility(View.GONE);
            mBtnUserAdminList.setVisibility(View.GONE);
            mBtnUserBanned.setVisibility(View.GONE);
            mBtnUserCancel.setVisibility(View.GONE);
        }
        mBtnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //用于设置/取消管理员
                setAdmin(mToUid);
            }
        });
        mBtnUserAdminList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                LiveAdminListActivity.forward(mContext, mLiveUid);
            }
        });
        mBtnUserBanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //永久禁言
                liveCancelShutUp(mToUid, 0);
            }
        });
        mBtnUserCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        loadData();
    }

    /**
     * 设置或者取消管理员
     *
     * @param toUid
     */
    private void setAdmin(String toUid) {
        if (!TextUtils.isEmpty(toUid) && toUid.equals(CommonAppConfig.getInstance().getUid())) {
        }
        LiveHttpUtil.setAdmin(mLiveUid, toUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ToastUtil.show(mType == 1 ? CommonAppContext.sInstance.getString(R.string.live_set_is_admin_successful)
                            : CommonAppContext.sInstance.getString(R.string.live_set_cancel_admin_successful));
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    /**
     * 主播或管理员禁言
     * type 禁言类型,0永久，1本场
     *
     * @param toUid
     */
    private void liveCancelShutUp(String toUid, int type) {
        LiveHttpUtil.setShutUp(mLiveUid, mStream, type, toUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    // TODO: 2020/10/22
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    private void loadData() {

    }
}
