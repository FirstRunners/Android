package com.android.firstlearners.learners.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.model.data.Study;
import com.android.firstlearners.learners.model.data.StudyUsers;

import io.realm.RealmList;

public class RankingRecyclerViewAdapter extends RecyclerView.Adapter<RankingRecyclerViewHolder> {
    private RealmList<StudyUsers> studyUsers;
    private int study_count;
    private Context mContext;

    public RankingRecyclerViewAdapter(Study study) {
        this.studyUsers = study.study_users;
        study_count = study.study_count;
    }

    @NonNull
    @Override
    public RankingRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_graph,parent, false);
        mContext = parent.getContext();
        return new RankingRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingRecyclerViewHolder holder, int position) {
        String name = studyUsers.get(position).user_name;
        int att = studyUsers.get(position).user_att_cnt;
        int hw = studyUsers.get(position).user_hw_cnt;
        int percent = att / study_count * 100;
        holder.progressBar.setProgress(50);
        holder.name.setText(name);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, IndividualDialog.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studyUsers.size();
    }

    public void addItems(RealmList<StudyUsers> studyUsers){
        this.studyUsers = studyUsers;
    }
}
