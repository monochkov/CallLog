package com.melkiy.calloger.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melkiy.calloger.R;
import com.melkiy.calloger.activities.CallActivity;
import com.melkiy.calloger.models.Call;
import com.melkiy.calloger.viewholders.CallHeaderViewHolder;
import com.melkiy.calloger.viewholders.CallRecyclerViewHolder;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class CallRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CALL = 1;

    private Context context;
    private List<Object> calls = new ArrayList<>();

    public CallRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (calls.get(position) instanceof Call) {
            return TYPE_CALL;
        } else {
            return TYPE_HEADER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == TYPE_CALL) {
            view = inflater.inflate(R.layout.item_call, parent, false);
            return new CallRecyclerViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_call_header, parent, false);
            return new CallHeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CallRecyclerViewHolder) {
            Call call = (Call) calls.get(position);
            CallRecyclerViewHolder viewHolder = (CallRecyclerViewHolder) holder;
            viewHolder.name.setText(call.getName() == null ? call.getNumber() : call.getName());

            int hours = call.getDate().get(DateTimeFieldType.hourOfDay());
            int minutes = call.getDate().get(DateTimeFieldType.minuteOfHour());
            viewHolder.date.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes));

            switch (call.getType()) {
                case 1:
                    viewHolder.icon.setImageResource(R.drawable.ic_call_received_24dp);
                    break;
                case 2:
                    viewHolder.icon.setImageResource(R.drawable.ic_call_made_24dp);
                    break;
                case 3:
                    viewHolder.icon.setImageResource(R.drawable.ic_call_missed_24dp);
                    break;
                case 5:
                    viewHolder.icon.setImageResource(R.drawable.ic_do_not_disturb_24dp);
            }

            //item click
            viewHolder.setClickListener(() -> {
                Intent intent = new Intent(context, CallActivity.class);
                intent.putExtra("Call", call);
                context.startActivity(intent);
            });

        } else {
            Instant instant = (Instant) calls.get(position);
            Instant now = Instant.now();
            CallHeaderViewHolder viewHolder = (CallHeaderViewHolder) holder;
            if ((now.get(DateTimeFieldType.dayOfYear()) == instant.get(DateTimeFieldType.dayOfYear())) &&
                    (now.get(DateTimeFieldType.year()) == instant.get(DateTimeFieldType.year()))) {
                viewHolder.date.setText("Today");
            } else if ((now.get(DateTimeFieldType.dayOfYear()) - 1 == instant.get(DateTimeFieldType.dayOfYear())) &&
                    (now.get(DateTimeFieldType.year()) == instant.get(DateTimeFieldType.year()))) {
                viewHolder.date.setText("Yesterday");
            } else {
                DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMM yyyy");
                String date = formatter.print(instant);
                viewHolder.date.setText(date);
            }
        }
    }

    @Override
    public int getItemCount() {
        return calls.size();
    }

    public void setCalls(List<Object> calls) {
        this.calls = calls;
        notifyDataSetChanged();
    }
}
