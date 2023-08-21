package com.ljwx.baselog.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ljwx.baselog.R
import com.ljwx.baselog.display.FixSizeVector

class DisplayLogAdapter(private val data: FixSizeVector<String>) :
    RecyclerView.Adapter<DisplayLogAdapter.DisplayLogViewHolder>() {


    inner class DisplayLogViewHolder(private val view: View) : ViewHolder(view) {
        val content = view.findViewById<TextView>(R.id.content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayLogViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.log_display_item, parent, false)
        return DisplayLogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DisplayLogViewHolder, position: Int) {
        holder.content.text = data[position]
    }

}