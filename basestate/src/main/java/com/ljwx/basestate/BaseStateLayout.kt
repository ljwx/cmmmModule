package com.ljwx.basestate

import android.content.Context
import android.view.View
import com.ljwx.baseapp.constant.BaseLayoutStatus
import com.ljwx.baseapp.view.IViewStateLayout
import com.ljwx.basestate.czy.StateLayout

open class BaseStateLayout(context: Context) : StateLayout(context), IViewStateLayout {
    override fun setStateView(state: Int, view: View) {

    }

    override fun showStateView(state: Int, tag: Any?) {

    }

    override fun addClickListener(
        @BaseLayoutStatus.LayoutStatus state: Int,
        id: Int,
        listener: OnClickListener
    ) {

    }


}