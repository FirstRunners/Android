package com.android.firstlearners.learners.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.firstlearners.learners.R;

public class InviteViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView name;
    public TextView phoneNumber;
    public ImageView isClicked;
    public InviteViewHolder(View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.container);
        name = itemView.findViewById(R.id.name);
        phoneNumber = itemView.findViewById(R.id.phoneNumber);
        isClicked = itemView.findViewById(R.id.isClicked);
    }
}
