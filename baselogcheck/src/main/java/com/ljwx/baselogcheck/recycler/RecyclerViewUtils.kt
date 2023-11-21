package com.ljwx.baselogcheck.recycler

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewUtils {

    fun createView(context: Context, placeView: View) :RecyclerView{
        val with = placeView.width
        val high = placeView.height
        val parent = placeView.parent as ViewGroup
        val recyclerView = RecyclerView(context)
        val params = ViewGroup.LayoutParams(with, high)
        parent.addView(recyclerView, params)
        recyclerView.bringToFront()
        return recyclerView
    }

}