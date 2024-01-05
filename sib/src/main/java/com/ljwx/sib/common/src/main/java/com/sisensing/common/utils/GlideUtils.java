package com.sisensing.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sisensing.common.R;

/**
 * @author y.xie
 * @date 2021/3/2 16:03
 * @desc glide相关设置
 */
public class GlideUtils {
    public static RequestOptions getRoundPlaceHolder(){
        return new RequestOptions().circleCrop();
    }

    public static RequestOptions getAvatarPlaceHolder(){
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.personalcenter_head_default).error(R.mipmap.personalcenter_head_default);
        return options;
    }

    /**
     * 将资源文件路径转换为url形式
     * @param context
     * @param id
     * @return
     */
    public static String getResourcesUri(Context context, @DrawableRes int id) {
        Resources resources = context.getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }

}
