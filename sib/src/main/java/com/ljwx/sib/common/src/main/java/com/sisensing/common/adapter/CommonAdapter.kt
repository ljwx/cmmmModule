package com.sisensing.common.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ljwx.recyclerview.quick.QuickSingleAdapter
import com.sisensing.common.R

object CommonAdapter {

    @JvmStatic
    fun getImage(context: Context) :QuickSingleAdapter<String>{
        val adapter = QuickSingleAdapter(String::class.java, R.layout.item_image_radiu)
        adapter.setOnItemBind { holder, s ->
            holder.itemView.findViewById<ImageView>(R.id.preview)?.let {
                Glide.with(context).load(s).into(it)
            }
        }
        return adapter
    }

}