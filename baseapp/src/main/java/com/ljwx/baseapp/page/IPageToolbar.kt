package com.ljwx.baseapp.page

import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import com.ljwx.baseapp.R

interface IPageToolbar {

    /**
     * 初始化Toolbar
     *
     * @param toolbarId toolbar控件的id
     */
    fun initToolbar(@IdRes toolbarId: Int = R.id.base_app_toolbar): Toolbar?

    fun initToolbar(toolbar: Toolbar?): Toolbar?

    /**
     * 设置Toolbar标题
     *
     * @param title 标题内容
     */
    fun setToolbarTitle(title: CharSequence)

}