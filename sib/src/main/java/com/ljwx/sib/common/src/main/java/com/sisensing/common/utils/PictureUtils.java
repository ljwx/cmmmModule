package com.sisensing.common.utils;

import android.app.Activity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.sisensing.common.R;

/**
 * @author y.xie
 * @date 2021/3/30 15:25
 * @desc
 */
public class PictureUtils {
    /**
     *
     * @param activity
     * @param isEnableCrop 是否开启裁剪
     * @param aspect_ratio_x 裁剪宽的比例
     * @param aspect_ratio_y 裁剪高的比例
     */
    public static void takePhoto(Activity activity, Fragment fragment, boolean isEnableCrop, int aspect_ratio_x, int aspect_ratio_y){
        PictureSelector pictureSelector;
        if (activity == null){
            pictureSelector = PictureSelector.create(fragment);
        }else {
            pictureSelector = PictureSelector.create(activity);
        }
        pictureSelector
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .theme(R.style.picture_WeChat_style)//style
                .isEnableCrop(isEnableCrop)// 是否裁剪
                .setCropDimmedColor(ContextCompat.getColor(activity, R.color.ucrop_color_default_dimmed))
                .showCropFrame(false)
                .showCropGrid(false)
                .isCompress(true)// 是否压缩
                .compressQuality(60)// 图片压缩后输出质量
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .withAspectRatio(aspect_ratio_x,aspect_ratio_y)//裁剪比例
                .freeStyleCropEnabled(true)//裁剪框是否可拖拽
                .rotateEnabled(false)//裁剪是否可旋转图片
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.REQUEST_CAMERA);
    }

    /**
     *
     * @param activity
     * @param isEnableCrop 是否开启裁剪
     * @param aspect_ratio_x 裁剪宽的比例
     * @param aspect_ratio_y 裁剪高的比例
     */
    public static void openAlbum(Activity activity, Fragment fragment, boolean isEnableCrop, int aspect_ratio_x, int aspect_ratio_y){
        PictureSelector pictureSelector;
        if (activity == null){
            pictureSelector = PictureSelector.create(fragment);
        }else {
            pictureSelector = PictureSelector.create(activity);
        }
       pictureSelector
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                .theme(R.style.picture_WeChat_style)//style
                .selectionMode(PictureConfig.SINGLE)//单选
                .isCamera(false)
                .isEnableCrop(isEnableCrop)// 是否裁剪
                .setCropDimmedColor(ContextCompat.getColor(activity, R.color.ucrop_color_default_dimmed))
                .showCropFrame(false)
                .showCropGrid(false)
                .isCompress(true)// 是否压缩
                .compressQuality(60)// 图片压缩后输出质量
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .withAspectRatio(aspect_ratio_x,aspect_ratio_y)//裁剪比例
                .freeStyleCropEnabled(true)//裁剪框是否可拖拽
                .rotateEnabled(false)//裁剪是否可旋转图片
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
