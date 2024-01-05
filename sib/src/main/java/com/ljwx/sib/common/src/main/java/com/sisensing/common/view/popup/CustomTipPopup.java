package com.sisensing.common.view.popup;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.sisensing.common.R;

/**
 * @author y.xie
 * @date 2021/3/18 9:14
 * @desc 自定义提示弹窗
 */
public class CustomTipPopup extends CenterPopupView {

    private TextView tvContent;
    private TextView tvConfirm;
    private ImageView ivClose;
    private OnConfirmListener listener;

    private String content = "";
    public CustomTipPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.common_custom_tip_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvContent = findViewById(R.id.tv_content);
        tvConfirm = findViewById(R.id.tv_confirm);
        ivClose = findViewById(R.id.iv_close);
        tvContent.setText(content);
        tvConfirm.setOnClickListener(o->{
            if(listener!=null){
                listener.onConfirm();
            }
        });
        ivClose.setOnClickListener(o->{
            dismiss();
        });
    }

    public void setListener(OnConfirmListener listener){
        this.listener = listener;
    }
    public void setContent(String content){
        this.content = content;
    }

    @Override
    protected int getMaxWidth() {
        return (int) (ScreenUtils.getScreenWidth()*0.8);
    }
}
