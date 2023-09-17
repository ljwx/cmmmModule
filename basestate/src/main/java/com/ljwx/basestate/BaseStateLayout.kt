package com.ljwx.basestate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.ljwx.baseapp.business.IBaseStateLayout
import com.ljwx.basestate.czy.StateLayout

open class BaseStateLayout(context: Context) : StateLayout(context), IBaseStateLayout {
    override fun showStateLayout(state: Int, layout: Int) {

    }

    override fun setStateLayout(state: Int, layout: Int) {

    }

//    override fun setContent(view: View) {
//
//    }

    override fun showContent(tag: Any?) {

    }

    override fun showLoading(tag: Any?) {

    }

    override fun showEmpty(tag: Any?) {

    }

    override fun showError(tag: Any?) {

    }

    override fun showOffline(tag: Any?) {

    }

    override fun setLayoutContent(layout: Int) {
        super.setContent(LayoutInflater.from(context).inflate(layout,null, false))
    }

    override fun setLayoutLoading(layout: Int) {

    }

    override fun setLayoutEmpty(layout: Int) {

    }

    override fun setLayoutError(layout: Int) {

    }

    override fun setLayoutOffline(layout: Int) {

    }

    override fun setClickListener(id: Int, listener: View.OnClickListener) {

    }
}