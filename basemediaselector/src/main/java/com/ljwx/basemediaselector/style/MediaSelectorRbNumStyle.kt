package com.ljwx.basemediaselector.style

import android.content.Context
import com.luck.picture.lib.config.PictureSelectionConfig.selectorStyle

import androidx.core.content.ContextCompat

import com.luck.picture.lib.style.SelectMainStyle

import com.luck.picture.lib.style.BottomNavBarStyle

import com.luck.picture.lib.style.TitleBarStyle


class MediaSelectorRbNumStyle {

    fun getStyle(context: Context) {
        val blueTitleBarStyle = TitleBarStyle()
        blueTitleBarStyle.titleBackgroundColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_blue)

        val numberBlueBottomNavBarStyle = BottomNavBarStyle()
        numberBlueBottomNavBarStyle.bottomPreviewNormalTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_9b)
        numberBlueBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_blue)
        numberBlueBottomNavBarStyle.bottomNarBarBackgroundColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white)
        numberBlueBottomNavBarStyle.bottomSelectNumResources = com.ljwx.basemediaselect.R.drawable.ps_demo_blue_num_selected
        numberBlueBottomNavBarStyle.bottomEditorTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_53575e)
        numberBlueBottomNavBarStyle.bottomOriginalTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_53575e)


        val numberBlueSelectMainStyle = SelectMainStyle()
        numberBlueSelectMainStyle.statusBarColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_blue)
        numberBlueSelectMainStyle.isSelectNumberStyle = true
        numberBlueSelectMainStyle.isPreviewSelectNumberStyle = true
        numberBlueSelectMainStyle.selectBackground = com.ljwx.basemediaselect.R.drawable.ps_demo_blue_num_selector
        numberBlueSelectMainStyle.mainListBackgroundColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white)
        numberBlueSelectMainStyle.previewSelectBackground =
            com.ljwx.basemediaselect.R.drawable.ps_demo_preview_blue_num_selector

        numberBlueSelectMainStyle.selectNormalTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_9b)
        numberBlueSelectMainStyle.selectTextColor =
            ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_blue)
        numberBlueSelectMainStyle.selectText = context.getString(com.luck.picture.lib.R.string.ps_completed)

        selectorStyle.titleBarStyle = blueTitleBarStyle
        selectorStyle.bottomBarStyle = numberBlueBottomNavBarStyle
        selectorStyle.selectMainStyle = numberBlueSelectMainStyle
    }

}