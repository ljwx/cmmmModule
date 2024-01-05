package com.sisensing.common.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sisensing.common.R;
import com.sisensing.common.router.RouterActivityPath;


/**
 * @author y.xie
 * @date 2021/5/7 9:22
 * @desc
 */
public class ViewUtils {
    /**
     * 初始化服务协议与隐私政策view
     * @param context
     * @param textView
     */
    public static void initAgreementAndPrivacy(Context context, TextView textView){
        String str = context.getString(R.string.common_login_agreement);
        String user_agreement = context.getString(R.string.login_user_agreement);
        String privacy_policy = context.getString(R.string.login_privacy_policy);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        //第一个出现的位置
        final int start = str.indexOf(user_agreement);
        ssb.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                //用户服务协议点击事件

                ARouter.getInstance()
                        .build(RouterActivityPath.Web.PAGER_WEB)
                        .withString("url", AppLanguageUtils.getWebPrivacyUrl(AppLanguageUtils.WEB_URL_ARGUMENT))
                        .withString("title", context.getString(R.string.common_use_agreement))
                        .navigation();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //设置文件颜色
                ds.setColor(context.getResources().getColor(R.color.color_00D5B8));
                // 去掉下划线
                ds.setUnderlineText(false);
            }

        }, start, start + user_agreement.length(), 0);

        //最后一个出现的位置
        final int end = str.indexOf(privacy_policy);
        ssb.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                //隐私政策点击事件
                ARouter.getInstance()
                        .build(RouterActivityPath.Web.PAGER_WEB)
                        .withString("url", AppLanguageUtils.getWebPrivacyUrl(AppLanguageUtils.WEB_URL_PRIVACY))
                        .withString("title", context.getString(R.string.common_privacy_policy))
                        .navigation();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //设置文件颜色
                ds.setColor(context.getResources().getColor(R.color.color_00D5B8));
                // 去掉下划线
                ds.setUnderlineText(false);
            }

        }, end, end + privacy_policy.length(), 0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(ssb, TextView.BufferType.SPANNABLE);
        textView.setHighlightColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

}
