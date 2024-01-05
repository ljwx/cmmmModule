//package com.sisensing.common.utils;
//
//import android.content.Context;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.qiyukf.unicorn.api.UnicornGifImageLoader;
//
///**
// * @author y.xie
// * @date 2021/7/27 17:29
// * @desc 七鱼客服gif加载器
// */
//public class GlideGifImagerLoader implements UnicornGifImageLoader {
//    Context context;
//
//    public GlideGifImagerLoader(Context context) {
//        this.context = context.getApplicationContext();
//    }
//
//    //当需要加载 gif 图片的时候会回调该方法
//    @Override
//    public void loadGifImage(String url, ImageView imageView,String imgName) {
//        if (url == null || imgName == null) {
//            return;
//        }
//        Glide.with(context).load(url).into(imageView);
//    }
//}
