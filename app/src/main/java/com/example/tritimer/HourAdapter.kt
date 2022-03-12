package com.example.tritimer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


// MyFilterHolder Class adapts the RecyclerView.ViewHolder(filterView) Class
class MyHourHolder(inflater: View, parent: ViewGroup) : RecyclerView.ViewHolder(inflater)
{
    var hourHolder: TextView? = null

    init{
        hourHolder = itemView.findViewById(R.id.hour_view_id2)
    }
}

class HourAdapter (val hourArray: ArrayList<Int>) : RecyclerView.Adapter<MyHourHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHourHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.hour, parent, false)
        return MyHourHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyHourHolder, position: Int) {
        holder.hourHolder?.text = hourArray[position].toString()
    }
    override fun getItemCount(): Int = hourArray.size
}
