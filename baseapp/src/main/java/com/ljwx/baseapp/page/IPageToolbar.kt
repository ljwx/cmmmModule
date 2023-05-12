package com.ljwx.baseapp.page

import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar

interface IPageToolbar {

    /**
     * 初始化Toolbar
     *
     * @param toolbarId toolbar控件的id
     */
    fun initToolbar(@IdRes toolbarId: Int? = null): Toolbar?

    /**
     * 设置Toolbar标题
     *
     * @param title 标题内容
     */
    fun setToolbarTitle(title: CharSequence)

}