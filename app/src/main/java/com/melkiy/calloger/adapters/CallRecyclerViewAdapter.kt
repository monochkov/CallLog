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
import com.melkiy.calloger.extensions.isDayNotEquals
import com.melkiy.calloger.extensions.isNotToday
import com.melkiy.calloger.listeners.OnCallClickListener
import com.melkiy.calloger.models.Call
import com.melkiy.calloger.viewholders.CallHeaderViewHolder
import com.melkiy.calloger.viewholders.CallRecyclerViewHolder
import org.joda.time.DateTimeFieldType
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat
import java.util.*

class CallRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onCallClickListener: OnCallClickListener? = null

    private val data = ArrayList<Any>()

    override fun getItemViewType(position: Int): Int =
            if (data[position] is Call) {
                ItemType.TYPE_CALL.value
            } else {
                ItemType.TYPE_HEADER.value
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ItemType.TYPE_CALL.value -> CallRecyclerViewHolder(inflater.inflate(R.layout.item_call, parent, false))
            else -> CallHeaderViewHolder(inflater.inflate(R.layout.item_call_header, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CallRecyclerViewHolder) {
            holder.let {
                val call = data[position] as Call
                it.name.text = call.name ?: call.number

                val hours = call.date.get(DateTimeFieldType.hourOfDay())
                val minutes = call.date.get(DateTimeFieldType.minuteOfHour())
                it.date.text = String.format("%02d", hours) + ":" + String.format("%02d", minutes)

                when (call.type) {
                    Call.Type.INCOMING -> it.icon.setImageResource(R.drawable.ic_call_received_24dp)
                    Call.Type.OUTGOING -> it.icon.setImageResource(R.drawable.ic_call_made_24dp)
                    Call.Type.MISSED -> it.icon.setImageResource(R.drawable.ic_call_missed_24dp)
                    Call.Type.DISMISSED -> it.icon.setImageResource(R.drawable.ic_do_not_disturb_24dp)
                }

                it.itemView.setOnClickListener { notifyCallClicked(call) }
            }

        } else if (holder is CallHeaderViewHolder) {
            holder.let {
                val instant = data[position] as Instant
                val now = Instant.now()
                if (now.get(DateTimeFieldType.dayOfYear()) == instant.get(DateTimeFieldType.dayOfYear()) && now.get(DateTimeFieldType.year()) == instant.get(DateTimeFieldType.year())) {
                    it.date.setText(R.string.label_today)
                } else if (now.get(DateTimeFieldType.dayOfYear()) - 1 == instant.get(DateTimeFieldType.dayOfYear()) && now.get(DateTimeFieldType.year()) == instant.get(DateTimeFieldType.year())) {
                    it.date.setText(R.string.label_yestarday)
                } else {
                    val formatter = DateTimeFormat.forPattern("dd MMM yyyy")
                    val date = formatter.print(instant)
                    it.date.text = date
                }
            }
        }
    }

    override fun getItemCount() = data.size

    fun setCalls(calls: List<Call>) {
        data.clear()
        if (calls.isNotEmpty()) {
            val firstCall = calls.first()
            if (firstCall.date.isNotToday()) {
                data.add(firstCall.date as Any)
            }
            data.add(firstCall)
            for (i in 1..calls.size - 1) {
                val call = calls[i]
                if (call.date.isDayNotEquals(calls[i - 1].date)) {
                    data.add(call.date as Any)
                }
                data.add(call)
            }
        }
        notifyDataSetChanged()
    }

    private fun notifyCallClicked(call: Call) = onCallClickListener?.onCallClicked(call)

    enum class ItemType(val value: Int) {
        TYPE_HEADER(0), TYPE_CALL(1)
    }
}
