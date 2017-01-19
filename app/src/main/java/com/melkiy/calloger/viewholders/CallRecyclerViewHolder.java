package com.melkiy.calloger.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.melkiy.calloger.R;

public class CallRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name, date;
    public ImageView icon;

    private ItemClickListener clickListener;

    public CallRecyclerViewHolder(View view) {
        super(view);

        name = (TextView) view.findViewById(R.id.name);
        date = (TextView) view.findViewById(R.id.date);
        icon = (ImageView) view.findViewById(R.id.type);

        view.setOnClickListener(this);
    }

    public void setClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onItemClick();
    }

    public interface ItemClickListener {
        void onItemClick();
    }
}
