package com.sisensing.common.view.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.sisensing.common.R;

public class FirstConnectTipPopup extends CenterPopupView {

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvConfirm;
    private OnConfirmListener listener;

    private String title = "";
    private String content = "";
    public FirstConnectTipPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.common_dialog_tips_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvTitle = findViewById(R.id.base_dialog_title);
        tvContent = findViewById(R.id.base_dialog_content_string);
        tvConfirm = findViewById(R.id.base_dialog_positive);
        findViewById(R.id.base_dialog_negative).setVisibility(View.GONE);
        tvConfirm.setTextColor(getContext().getResources().getColor(R.color.black));
        tvConfirm.setText(R.string.common_i_know);
        tvTitle.setText(R.string.tips);
        tvContent.setText(R.string.to_ensure_the_transmission_of_glucose_data_please_make_sure_the_app_is_running);
        tvConfirm.setOnClickListener(o->{
            if(listener!=null){
                listener.onConfirm();
            } else {
                dismiss();
            }
        });
    }

    public void setListener(OnConfirmListener listener){
        this.listener = listener;
    }
    public void setContent(String content){
        this.content = content;
    }

    public void setTitleContent(String title, String content) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
    }

    @Override
    protected int getMaxWidth() {
        return (int) (ScreenUtils.getScreenWidth()*1);
    }
}
