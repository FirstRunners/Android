package com.android.firstlearners.learners.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendanceDialog extends AppCompatActivity{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.accept)
    TextView accept;

    private AttendanceRecylcerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_attendance_dialog);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        List<Map<String,String>> item = (List<Map<String,String>>)(new Gson()).fromJson(intent.getStringExtra("item"),Object.class);

        adapter = new AttendanceRecylcerViewAdapter(item);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick(R.id.accept)
    public void OnClick(){
        finish();
    }
}
