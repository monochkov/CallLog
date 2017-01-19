package com.melkiy.calloger.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.melkiy.calloger.R;

public class CallRecyclerViewHolder extends RecyclerView.ViewHolder {

    public final TextView name;
    public final TextView date;
    public final ImageView icon;

    public CallRecyclerViewHolder(View view) {
        super(view);

        name = (TextView) view.findViewById(R.id.name);
        date = (TextView) view.findViewById(R.id.date);
        icon = (ImageView) view.findViewById(R.id.type);
    }
}
