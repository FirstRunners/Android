package com.android.firstlearners.learners.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.firstlearners.learners.R;

public class RankingRecyclerViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout relativeLayout;
    public ProgressBar progressBar;
    public TextView textView;
    public TextView ranking;
    public RankingRecyclerViewHolder(View itemView) {
        super(itemView);

        relativeLayout = itemView.findViewById(R.id.boxIndividual);
        progressBar = itemView.findViewById(R.id.progressBarIndividual);
        textView = itemView.findViewById(R.id.infoIndividual);
        ranking = itemView.findViewById(R.id.ranking);
    }
}
