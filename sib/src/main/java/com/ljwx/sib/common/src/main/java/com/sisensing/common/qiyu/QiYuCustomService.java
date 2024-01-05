//package com.sisensing.common.qiyu;
//
//import com.qiyukf.unicorn.api.UICustomization;
//import com.qiyukf.unicorn.api.YSFOptions;
//import com.qiyukf.unicorn.api.customization.title_bar.TitleBarConfig;
//
///**
// * @author y.xie
// * @date 2021/7/27 9:48
// * @desc
// */
//public class QiYuCustomService {
//
//    private static class Inner {
//        private static final QiYuCustomService INSTANCE = new QiYuCustomService();
//    }
//
//    public static QiYuCustomService getInstance() {
//        return QiYuCustomService.Inner.INSTANCE;
//    }
//    // 如果返回值为null，则全部使用默认参数。
//    public YSFOptions options() {
//        YSFOptions options = new YSFOptions();
//        options.uiCustomization = configUiCustom();
//        return options;
//    }
//
//    private UICustomization configUiCustom(){
//        UICustomization uiCustomization = new UICustomization();
//        //uiCustomization.leftAvatar = UriUtils.res2Uri(R.mipmap.ic_launcher_cgm+"").toString();
//        //在进入聊天界面时是否隐藏输入键盘
//        uiCustomization.hideKeyboardOnEnterConsult = true;
//        return uiCustomization;
//    }
//
//
//    /**
//     * 设置聊天界面右上角相关配置
//     * @return TitleBarConfig
//     */
//    private TitleBarConfig configTitleBar() {
//        TitleBarConfig titleBarConfig = new TitleBarConfig();
///*        //设置右上角退出会话按钮的图标
//        titleBarConfig.titleBarRightQuitBtnBack = R.drawable.ic_launcher;
//        //设置右上角转人工按钮的图标
//        titleBarConfig.titleBarRightHumanBtnBack = R.drawable.ic_launcher;
//        //设置右上角评价按钮的图标
//        titleBarConfig.titleBarRightEvaluatorBtnBack = R.drawable.ic_launcher;
//        //新增右上角按钮的图标
//        titleBarConfig.titleBarRightImg = R.drawable.ic_launcher;
//        //新增右上角按钮文字的颜色
//        titleBarConfig.titleBarRightTextColor = 0XFFDB7093;
//        //新增右上角按钮的文字
//        titleBarConfig.titleBarRightText = "进店";
//        titleBarConfig.onTitleBarRightBtnClickListener = new OnTitleBarRightBtnClickListener() {
//            @Override
//            public void onClick(Activity activity) {
//                //implement
//            }
//        };*/
//
//        return titleBarConfig;
//    }
//}
