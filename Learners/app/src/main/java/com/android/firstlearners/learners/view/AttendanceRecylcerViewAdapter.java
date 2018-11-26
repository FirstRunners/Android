package com.android.firstlearners.learners.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Map;

public class AttendanceRecylcerViewAdapter extends RecyclerView.Adapter<AttendanceRecyclerViewHolder> {
    List<Map<String,String>> item;
    Context contex;
    public AttendanceRecylcerViewAdapter(List<Map<String,String>> item, Context context) {
        this.item = item;
        this.contex = context;
    }

    @NonNull
    @Override
    public AttendanceRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance,parent,false);
        return new AttendanceRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceRecyclerViewHolder holder, int position) {


        if(item.get(position).get("user_img").equals("")) {
            Glide.with(contex)
                    .applyDefaultRequestOptions(RequestOptions.circleCropTransform())
                    .load(R.drawable.basic_profile).into(holder.profile);
        }
        else
        {
            Glide.with(contex)
                    .applyDefaultRequestOptions(RequestOptions.circleCropTransform())
                    .load(item.get(position).get("user_img")).into(holder.profile);
        }
        holder.name.setText(item.get(position).get("user_name"));
        holder.date.setText(item.get(position).get("attend_date") + ", "+item.get(position).get("attend_time"));
    }

    @Override
    public int getItemCount() {
        return item.size();
    }
}
