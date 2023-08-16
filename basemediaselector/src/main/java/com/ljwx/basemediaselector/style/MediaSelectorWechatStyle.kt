package com.ljwx.basemediaselector.style

import android.content.Context
import com.luck.picture.lib.config.PictureSelectionConfig.selectorStyle

import androidx.core.content.ContextCompat

import com.luck.picture.lib.style.BottomNavBarStyle

import com.luck.picture.lib.style.TitleBarStyle

import com.luck.picture.lib.utils.DensityUtil

import com.luck.picture.lib.style.SelectMainStyle


class MediaSelectorWechatStyle {

    fun getStyle(context: Context) {
        val numberSelectMainStyle = SelectMainStyle()
        numberSelectMainStyle.isSelectNumberStyle = true
        numberSelectMainStyle.isPreviewSelectNumberStyle = false
        numberSelectMainStyle.isPreviewDisplaySelectGallery = true
        numberSelectMainStyle.selectBackground = com.luck.picture.lib.R.drawable.ps_default_num_selector
        numberSelectMainStyle.previewSelectBackground = com.luck.picture.lib.R.drawable.ps_preview_checkbox_selector
        numberSelectMainStyle.selectNormalBackgroundResources =
            com.luck.picture.lib.R.drawable.ps_select_complete_normal_bg
        numberSelectMainStyle.selectNormalTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_53575e)
        numberSelectMainStyle.selectNormalText = context.getString(com.luck.picture.lib.R.string.ps_send)
        numberSelectMainStyle.adapterPreviewGalleryBackgroundResource =
            com.luck.picture.lib.R.drawable.ps_preview_gallery_bg
        numberSelectMainStyle.adapterPreviewGalleryItemSize = DensityUtil.dip2px(context, 52f)
        numberSelectMainStyle.previewSelectText = context.getString(com.luck.picture.lib.R.string.ps_select)
        numberSelectMainStyle.previewSelectTextSize = 14
        numberSelectMainStyle.previewSelectTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white)
        numberSelectMainStyle.previewSelectMarginRight = DensityUtil.dip2px(context, 6f)
        numberSelectMainStyle.selectBackgroundResources = com.luck.picture.lib.R.drawable.ps_select_complete_bg
        numberSelectMainStyle.selectText = context.getString(com.luck.picture.lib.R.string.ps_send_num)
        numberSelectMainStyle.selectTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white)
        numberSelectMainStyle.mainListBackgroundColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_black)
        numberSelectMainStyle.isCompleteSelectRelativeTop = true
        numberSelectMainStyle.isPreviewSelectRelativeBottom = true
        numberSelectMainStyle.isAdapterItemIncludeEdge = false

        // 头部TitleBar 风格

        // 头部TitleBar 风格
        val numberTitleBarStyle = TitleBarStyle()
        numberTitleBarStyle.isHideCancelButton = true
        numberTitleBarStyle.isAlbumTitleRelativeLeft = true
//        if (cb_only_dir.isChecked()) {
//            numberTitleBarStyle.titleAlbumBackgroundResource = R.drawable.ps_demo_only_album_bg
//        } else {
            numberTitleBarStyle.titleAlbumBackgroundResource = com.luck.picture.lib.R.drawable.ps_album_bg
//        }
        numberTitleBarStyle.titleDrawableRightResource = com.luck.picture.lib.R.drawable.ps_ic_grey_arrow
        numberTitleBarStyle.previewTitleLeftBackResource = com.luck.picture.lib.R.drawable.ps_ic_normal_back

        // 底部NavBar 风格

        // 底部NavBar 风格
        val numberBottomNavBarStyle = BottomNavBarStyle()
        numberBottomNavBarStyle.bottomPreviewNarBarBackgroundColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_half_grey)
        numberBottomNavBarStyle.bottomPreviewNormalText = context.getString(com.luck.picture.lib.R.string.ps_preview)
        numberBottomNavBarStyle.bottomPreviewNormalTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_9b)
        numberBottomNavBarStyle.bottomPreviewNormalTextSize = 16
        numberBottomNavBarStyle.isCompleteCountTips = false
        numberBottomNavBarStyle.bottomPreviewSelectText = context.getString(com.luck.picture.lib.R.string.ps_preview_num)
        numberBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white)


        selectorStyle.titleBarStyle = numberTitleBarStyle
        selectorStyle.bottomBarStyle = numberBottomNavBarStyle
        selectorStyle.selectMainStyle = numberSelectMainStyle
    }

}