package com.android.firstlearners.learners.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.firstlearners.learners.R;

public class AttendanceRecyclerViewHolder extends RankingRecyclerViewHolder {
    public ImageView profile;
    public TextView name;
    public TextView date;

    public AttendanceRecyclerViewHolder(View itemView) {
        super(itemView);

        profile = itemView.findViewById(R.id.profile);
        name = itemView.findViewById(R.id.name);
        date = itemView.findViewById(R.id.date);

    }
}
