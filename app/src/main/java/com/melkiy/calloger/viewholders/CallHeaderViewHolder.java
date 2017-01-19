package com.melkiy.calloger.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.melkiy.calloger.R;

public class CallHeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView date;

    public CallHeaderViewHolder(View itemView) {
        super(itemView);

        date = (TextView) itemView.findViewById(R.id.date);
    }
}
