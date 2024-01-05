package com.sisensing.common.view.popup;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.sisensing.common.R;

/**
 * @author y.xie
 * @date 2021/5/21 13:57
 * @desc 血糖值输入框
 */
public class BsValueInputPop extends CenterPopupView {

    private OnInputConfirmListener listener;

    public BsValueInputPop(@NonNull Context context) {
        super(context);
    }

    public void setListener(OnInputConfirmListener listener) {
        this.listener = listener;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.common_pop_bs_value_input;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        EditText mEdtInput = findViewById(R.id.edt_input);

        TextView mTvCancel = findViewById(R.id.tv_cancel);
        TextView mTvConfirm = findViewById(R.id.tv_confirm);

        mTvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener!=null){
                    listener.onConfirm(mEdtInput.getText().toString().trim());
                }
            }
        });

        mEdtInput.addTextChangedListener(new TextWatcher() {
            boolean deleteLastChar;// 是否需要删除末尾

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    // 如果点后面有超过三位数值,则删掉最后一位
                    int length = s.length() - s.toString().lastIndexOf(".");
                    // 说明后面有三位数值
                    deleteLastChar = length >= 3;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) {
                    return;
                }
                if (deleteLastChar) {
                    // 设置新的截取的字符串
                    mEdtInput.setText(s.toString().substring(0, s.toString().length() - 1));
                    // 光标强制到末尾
                    mEdtInput.setSelection(mEdtInput.getText().length());
                }
                // 以小数点开头，前面自动加上 "0"
                if (s.toString().startsWith(".")) {
                    mEdtInput.setText("0" + s);
                    mEdtInput.setSelection(mEdtInput.getText().length());
                }
            }
        });

        mEdtInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText1 = (EditText) v;
                // 以小数点结尾，去掉小数点
                if (!hasFocus && editText1.getText() != null && editText1.getText().toString().endsWith(".")) {
                    mEdtInput.setText(editText1.getText().subSequence(0, editText1.getText().length() - 1));
                    mEdtInput.setSelection(mEdtInput.getText().length());
                }
            }
        });
    }

    @Override
    protected int getMaxWidth() {
        return (int) (ScreenUtils.getScreenWidth()*0.8);
    }
}
