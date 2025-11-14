package com.yunbao.main.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.utils.SpUtil;
import com.yunbao.common.views.DiyDialog;
import com.yunbao.main.R;
import com.yunbao.main.activity.ServiceActivity;

import java.util.List;


public class MainAvtivityDialog {
    private Context context;
    private Activity mActivity;

    private DiyDialog dialog;
    private Display display;

    private String type = "";
    private LinearLayout llMain = null;

    public IsetDiaLogShadowOnClick getmIsetDiaLogShadowOnClick() {
        return mIsetDiaLogShadowOnClick;
    }

    public void setmIsetDiaLogShadowOnClick(IsetDiaLogShadowOnClick mIsetDiaLogShadowOnClick) {
        this.mIsetDiaLogShadowOnClick = mIsetDiaLogShadowOnClick;
    }

    public IsetDiaLogShadowOnClick mIsetDiaLogShadowOnClick = null;

    public interface IsetDiaLogShadowOnClick {
        void setDiaLogShadowOnClick();
    }

    //TODO 1
    public MainAvtivityDialog(Context context,Activity activity) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        this.context = context;
        this.mActivity = activity;
    }

    //TODO 2
    public MainAvtivityDialog builder() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_xieyi_yinsi_style, null, false);
        llMain = view.findViewById(R.id.fl_ll);
        dialog = new DiyDialog(context, R.style.AlertDialogStyle) {
            //监听阴影部分
            @Override
            protected void onTouchOutside() {
                if (mIsetDiaLogShadowOnClick != null) {
                    mIsetDiaLogShadowOnClick.setDiaLogShadowOnClick();
                }
            }
        };
        dialog.setContentView(view);
        // 调整dialog背景大小
        llMain.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }

    //设置数据 TODO 3
    public void setDialogData( ) {
        if (mActivity==null||dialog==null){
            return;
        }
        TextView contentTv =  dialog.findViewById(R.id.content);
        Spanned msConten = null;
        String getSite_privacy_policy = CommonAppConfig.getInstance().getConfig().getSite_privacy_policy();//隐私正常
        if (!TextUtils.isEmpty(getSite_privacy_policy)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                msConten = Html.fromHtml(getSite_privacy_policy.trim(), Html.FROM_HTML_MODE_LEGACY);
            } else {
                msConten = Html.fromHtml(getSite_privacy_policy.trim());
            }
            contentTv.setText(msConten);
        }


        TextView tv_no =  dialog.findViewById(R.id.tv_no);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.getInstance().setStringValue("ce","no");
                dismiss();
                mActivity.finish();
            }
        });


        TextView tv_yes =  dialog.findViewById(R.id.tv_yes);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.getInstance().setStringValue("ce","yes");
                dismiss();
            }
        });

        //TODO 服务政策
        TextView tv_service_msg= dialog.findViewById(R.id.tv_service_msg);
        tv_service_msg.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线

        tv_service_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(mActivity, ServiceActivity.class);
                mIntent.putExtra("me","2");
                mActivity.startActivity(mIntent);
            }
        });



    }


    public void show() {


        if (dialog != null) {

            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    public DiyDialog getDialog() {
        return dialog;
    }

    public void setDialog(DiyDialog dialog) {
        this.dialog = dialog;
    }
}
