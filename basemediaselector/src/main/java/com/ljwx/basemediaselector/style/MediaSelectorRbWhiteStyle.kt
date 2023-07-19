package com.ljwx.basemediaselector.style

import com.luck.picture.lib.config.PictureSelectionConfig.selectorStyle

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.ljwx.basemediaselector.R

import com.luck.picture.lib.style.SelectMainStyle

import com.luck.picture.lib.style.BottomNavBarStyle

import com.luck.picture.lib.style.TitleBarStyle


class MediaSelectorRbWhiteStyle {

    fun getStyle(context: Context): SelectMainStyle {
        val whiteTitleBarStyle = TitleBarStyle()
        whiteTitleBarStyle.titleBackgroundColor =
            ContextCompat.getColor(context, R.color.ps_color_white)
        whiteTitleBarStyle.titleDrawableRightResource = R.drawable.ic_orange_arrow_down
        whiteTitleBarStyle.titleLeftBackResource = R.drawable.ps_ic_black_back
        whiteTitleBarStyle.titleTextColor =
            ContextCompat.getColor(context, R.color.ps_color_black)
        whiteTitleBarStyle.titleCancelTextColor =
            ContextCompat.getColor(context, R.color.ps_color_53575e)
        whiteTitleBarStyle.isDisplayTitleBarLine = true

        val whiteBottomNavBarStyle = BottomNavBarStyle()
        whiteBottomNavBarStyle.bottomNarBarBackgroundColor = Color.parseColor("#EEEEEE")
        whiteBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(context, R.color.ps_color_53575e)

        whiteBottomNavBarStyle.bottomPreviewNormalTextColor =
            ContextCompat.getColor(context, R.color.ps_color_9b)
        whiteBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(context, R.color.ps_color_fa632d)
        whiteBottomNavBarStyle.isCompleteCountTips = false
        whiteBottomNavBarStyle.bottomEditorTextColor =
            ContextCompat.getColor(context, R.color.ps_color_53575e)
        whiteBottomNavBarStyle.bottomOriginalTextColor =
            ContextCompat.getColor(context, R.color.ps_color_53575e)

        val selectMainStyle = SelectMainStyle()
        selectMainStyle.statusBarColor =
            ContextCompat.getColor(context, R.color.ps_color_white)
        selectMainStyle.isDarkStatusBarBlack = true
        selectMainStyle.selectNormalTextColor =
            ContextCompat.getColor(context, R.color.ps_color_9b)
        selectMainStyle.selectTextColor =
            ContextCompat.getColor(context, R.color.ps_color_fa632d)
        selectMainStyle.previewSelectBackground = R.drawable.ps_demo_white_preview_selector
        selectMainStyle.selectBackground = R.drawable.ps_checkbox_selector
        selectMainStyle.selectText = context.getString(R.string.ps_done_front_num)
        selectMainStyle.mainListBackgroundColor =
            ContextCompat.getColor(context, R.color.ps_color_white)

        selectorStyle.titleBarStyle = whiteTitleBarStyle
        selectorStyle.bottomBarStyle = whiteBottomNavBarStyle
        selectorStyle.selectMainStyle = selectMainStyle

        return selectMainStyle
    }

}