package com.sisensing.common.view.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;

import com.lxj.xpopup.core.CenterPopupView;
import com.sisensing.common.R;


/**
 * @author y.xie
 * @date 2021/6/4 19:34
 * @desc 通用的解释弹框
 */
public class CommonExplainPop extends CenterPopupView {
    private String title;
    private String tip;
    public CommonExplainPop(@NonNull Context context,String title,String tip) {
        super(context);
        this.title = title;
        this.tip = tip;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.common_pop_explain;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView mTvTitle = findViewById(R.id.tv_title);
        TextView mTvExplain = findViewById(R.id.tv_content);

        mTvTitle.setText(title);
        mTvExplain.setText(tip);

        findViewById(R.id.tv_i_known).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getMaxWidth() {
        return (int) (ScreenUtils.getScreenWidth()*0.8);
    }
}
