package com.ljwx.baselogcheck.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ljwx.baselogcheck.R

class BaseLogAdapter : RecyclerView.Adapter<BaseLogAdapter.DisplayLogViewHolder>() {

    private val data = ArrayList<Any>()

    inner class DisplayLogViewHolder(private val view: View) : ViewHolder(view) {
        val content = view.findViewById<TextView>(R.id.content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayLogViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_base_log_display, parent, false)
        return DisplayLogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DisplayLogViewHolder, position: Int) {
        val item = data[position]
        val text = if (item is CharSequence) item else item.toString()
        holder.content.text = text
    }

    fun addData(data:List<Any>?) {
        if (!data.isNullOrEmpty()) {
            this.data.addAll(data)
            notifyDataSetChanged()
        }
    }

    fun submitData(data:List<Any>?) {
        if (data != null) {
            this.data.clear()
            this.data.addAll(data)
            notifyDataSetChanged()
        }
    }

}