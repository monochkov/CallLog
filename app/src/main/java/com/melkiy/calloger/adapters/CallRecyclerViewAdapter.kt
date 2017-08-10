/*
    Copyright (C) 2015 Ihor Monochkov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.melkiy.calloger.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.melkiy.calloger.R
import com.melkiy.calloger.models.Call
import com.melkiy.calloger.utils.InstantUtils
import com.melkiy.calloger.viewholders.CallHeaderViewHolder
import com.melkiy.calloger.viewholders.CallRecyclerViewHolder
import org.joda.time.DateTimeFieldType
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat
import java.util.*

class CallRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnCallClickListener {

        fun onCallClicked(call: Call)
    }

    private var onCallClickListener: OnCallClickListener? = null

    private val data = ArrayList<Any>()

    override fun getItemViewType(position: Int): Int {
        if (data[position] is Call) {
            return TYPE_CALL
        } else {
            return TYPE_HEADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == TYPE_CALL) {
            val view = inflater.inflate(R.layout.item_call, parent, false)
            return CallRecyclerViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_call_header, parent, false)
            return CallHeaderViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CallRecyclerViewHolder) {
            val call = data[position] as Call
            holder.name.text = if (call.name == null) call.number else call.name

            val hours = call.date?.get(DateTimeFieldType.hourOfDay())
            val minutes = call.date?.get(DateTimeFieldType.minuteOfHour())
            holder.date.text = String.format("%02d", hours) + ":" + String.format("%02d", minutes)

            when (call.type) {
                Call.Type.INCOMING -> holder.icon.setImageResource(R.drawable.ic_call_received_24dp)
                Call.Type.OUTGOING -> holder.icon.setImageResource(R.drawable.ic_call_made_24dp)
                Call.Type.MISSED -> holder.icon.setImageResource(R.drawable.ic_call_missed_24dp)
                Call.Type.DISMISSED -> holder.icon.setImageResource(R.drawable.ic_do_not_disturb_24dp)
            }

            holder.itemView.setOnClickListener { _ -> notifyCallClicked(call) }

        } else if (holder is CallHeaderViewHolder) {
            val instant = data[position] as Instant
            val now = Instant.now()
            if (now.get(DateTimeFieldType.dayOfYear()) == instant.get(DateTimeFieldType.dayOfYear()) && now.get(DateTimeFieldType.year()) == instant.get(DateTimeFieldType.year())) {
                holder.date.setText(R.string.label_today)
            } else if (now.get(DateTimeFieldType.dayOfYear()) - 1 == instant.get(DateTimeFieldType.dayOfYear()) && now.get(DateTimeFieldType.year()) == instant.get(DateTimeFieldType.year())) {
                holder.date.setText(R.string.label_yestarday)
            } else {
                val formatter = DateTimeFormat.forPattern("dd MMM yyyy")
                val date = formatter.print(instant)
                holder.date.text = date
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setCalls(calls: List<Call>) {
        data.clear()
        if (calls.isNotEmpty()) {
            val firstCall = calls[0]
            if (InstantUtils.isToday(firstCall.date)) {
                data.add(firstCall.date as Any)
            }
            data.add(firstCall)
            for (i in 1..calls.size - 1) {
                val call = calls[i]
                if (InstantUtils.isDayEquals(call.date, calls[i - 1].date)) {
                    data.add(call.date as Any)
                }
                data.add(call)
            }
        }
        notifyDataSetChanged()
    }

    fun setOnCallClickListener(listener: OnCallClickListener) {
        onCallClickListener = listener
    }

    private fun notifyCallClicked(call: Call) {
        onCallClickListener!!.onCallClicked(call)
    }

    companion object {

        private val TYPE_HEADER = 0
        private val TYPE_CALL = 1
    }
}
