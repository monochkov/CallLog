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

package com.melkiy.calloger.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melkiy.calloger.R;
import com.melkiy.calloger.models.Call;
import com.melkiy.calloger.utils.InstantUtils;
import com.melkiy.calloger.viewholders.CallHeaderViewHolder;
import com.melkiy.calloger.viewholders.CallRecyclerViewHolder;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class CallRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnCallClickListener {

        void onCallClicked(Call call);
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CALL = 1;

    private OnCallClickListener onCallClickListener;

    private final List<Object> data = new ArrayList<>();

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof Call) {
            return TYPE_CALL;
        } else {
            return TYPE_HEADER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_CALL) {
            View view = inflater.inflate(R.layout.item_call, parent, false);
            return new CallRecyclerViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_call_header, parent, false);
            return new CallHeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CallRecyclerViewHolder) {
            Call call = (Call) data.get(position);
            CallRecyclerViewHolder viewHolder = (CallRecyclerViewHolder) holder;
            viewHolder.name.setText(call.getName() == null ? call.getNumber() : call.getName());

            int hours = call.getDate().get(DateTimeFieldType.hourOfDay());
            int minutes = call.getDate().get(DateTimeFieldType.minuteOfHour());
            viewHolder.date.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes));

            switch (call.getType()) {
                case INCOMING:
                    viewHolder.icon.setImageResource(R.drawable.ic_call_received_24dp);
                    break;
                case OUTGOING:
                    viewHolder.icon.setImageResource(R.drawable.ic_call_made_24dp);
                    break;
                case MISSED:
                    viewHolder.icon.setImageResource(R.drawable.ic_call_missed_24dp);
                    break;
                case DISMISSED:
                    viewHolder.icon.setImageResource(R.drawable.ic_do_not_disturb_24dp);
                    break;
            }

            viewHolder.itemView.setOnClickListener(v -> {
                notifyCallClicked(call);
            });

        } else {
            Instant instant = (Instant) data.get(position);
            Instant now = Instant.now();
            CallHeaderViewHolder viewHolder = (CallHeaderViewHolder) holder;
            if ((now.get(DateTimeFieldType.dayOfYear()) == instant.get(DateTimeFieldType.dayOfYear())) &&
                    (now.get(DateTimeFieldType.year()) == instant.get(DateTimeFieldType.year()))) {
                viewHolder.date.setText(R.string.label_today);
            } else if ((now.get(DateTimeFieldType.dayOfYear()) - 1 == instant.get(DateTimeFieldType.dayOfYear())) &&
                    (now.get(DateTimeFieldType.year()) == instant.get(DateTimeFieldType.year()))) {
                viewHolder.date.setText(R.string.label_yestarday);
            } else {
                DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMM yyyy");
                String date = formatter.print(instant);
                viewHolder.date.setText(date);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setCalls(List<Call> calls) {
        data.clear();
        if (!calls.isEmpty()) {
            Call firstCall = calls.get(0);
            if (InstantUtils.isToday(firstCall.getDate())) {
                data.add(firstCall.getDate());
            }
            data.add(firstCall);
            for (int i = 1; i < calls.size(); i++) {
                Call call = calls.get(i);
                if (InstantUtils.isDayEquals(call.getDate(), calls.get(i - 1).getDate())) {
                    data.add(call.getDate());
                }
                data.add(call);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnCallClickListener(OnCallClickListener listener) {
        onCallClickListener = listener;
    }

    private void notifyCallClicked(Call call) {
        if (onCallClickListener != null) {
            onCallClickListener.onCallClicked(call);
        }
    }
}
