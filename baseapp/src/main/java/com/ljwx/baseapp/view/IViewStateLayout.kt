package com.ljwx.baseapp.view

import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.ljwx.baseapp.constant.BaseLayoutStatus

interface IViewStateLayout {

    fun setStateView(@BaseLayoutStatus.LayoutStatus state: Int, view: View)

    fun showStateView(
        @BaseLayoutStatus.LayoutStatus state: Int,
        view: View? = null,
        tag: Any? = null
    )

    fun getView(): ViewGroup

    fun addClickListener(
        @BaseLayoutStatus.LayoutStatus state: Int,
        @IdRes id: Int,
        listener: OnClickListener
    )

}