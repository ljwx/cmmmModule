package com.sisensing.common.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.ljwx.view.SplitTextView;

public class SplitTextViewAdapter {

    @BindingAdapter("stvRightText")
    public static void setRightText(SplitTextView tv, String content) {
        if (StringUtils.isEmpty(content)) {
            tv.setTextRight("");
        } else {
            tv.setTextRight(content);
        }
    }

    @BindingAdapter("stvCenterText")
    public static void setCenterText(SplitTextView tv, String content) {
        if (StringUtils.isEmpty(content)) {
            tv.setTextCenter("");
        } else {
            tv.setTextCenter(content);
        }
    }

}
