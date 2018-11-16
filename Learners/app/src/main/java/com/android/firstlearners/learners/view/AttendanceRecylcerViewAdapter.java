package com.android.firstlearners.learners.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.firstlearners.learners.R;

import java.util.List;
import java.util.Map;

public class AttendanceRecylcerViewAdapter extends RecyclerView.Adapter<AttendanceRecyclerViewHolder> {
    List<Map<String,String>> item;

    public AttendanceRecylcerViewAdapter(List<Map<String,String>> item) {
        this.item = item;
    }

    @NonNull
    @Override
    public AttendanceRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance,parent,false);
        return new AttendanceRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceRecyclerViewHolder holder, int position) {
        holder.name.setText(item.get(position).get("user_name"));
        holder.date.setText(item.get(position).get("attend_date") + ", "+item.get(position).get("attend_time"));
    }

    @Override
    public int getItemCount() {
        return item.size();
    }
}
