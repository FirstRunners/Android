package com.android.firstlearners.learners.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.etc.CustomProgressBar;

public class RankingRecyclerViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout relativeLayout;
    public CustomProgressBar progressBar;
    public TextView name;
    public RankingRecyclerViewHolder(View itemView) {
        super(itemView);

        relativeLayout = itemView.findViewById(R.id.boxIndividual);
        progressBar = itemView.findViewById(R.id.progressBarIndividual);
        name = itemView.findViewById(R.id.infoName);
    }
}
