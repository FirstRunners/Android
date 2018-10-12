package com.android.firstlearners.learners.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.firstlearners.learners.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class CreateStudyActivity extends AppCompatActivity {
    @BindView(R.id.create_name) EditText name;
    @BindView(R.id.create_goal) EditText goal;
    @BindView(R.id.numOfMeet) EditText meet;
    @BindView(R.id.create_total) EditText total;
    @BindView(R.id.dayOfStart) TextView start;
    @BindView(R.id.dayOfEnd) TextView end;
    @BindView(R.id.btn_next) TextView next;

    private static final int NAME = 0b000001;
    private static final int GOAL = 0b000010;
    private static final int MEET = 0b000100;
    private static final int TOTAL = 0b001000;
    private static final int START = 0b010000;
    private static final int END = 0b100000;

    private static final int INVITE = 100;

    private DatePickerDialog setDayOfStart;
    private DatePickerDialog setDayOfEnd;
    private SimpleDateFormat format;
    private int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study);
        ButterKnife.bind(this);

        flag = 0b000000;

        int year, month, day;
        format = new SimpleDateFormat("yy.MM.dd",Locale.KOREAN);

        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        setDayOfStart = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDate(year, month, dayOfMonth, start);
                flag = flag | START;
                buttonEnable();
            }
        },year, month, day);

        start.setText(format.format(calendar.getTime()));

        calendar.add(Calendar.MONTH,1);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        setDayOfEnd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDate(year, month, dayOfMonth, end);
                flag = flag | END;
                buttonEnable();
            }
        },year, month, day);

        Log.d("test",String.valueOf(calendar.getTimeInMillis()));
        end.setText(format.format(calendar.getTime()));
    }

    @OnTextChanged(value = R.id.create_name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputName(){
        if(name.length() > 0){
            flag = flag | NAME;
        }else {
            flag = flag ^ NAME;
        }
        buttonEnable();
    }

    @OnTextChanged(value =  R.id.create_goal, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputGoal(){
        if(goal.length() > 0){
            flag = flag | GOAL;
        }else{
            flag = flag ^ GOAL;
        }
        buttonEnable();
    }

    @OnTextChanged(value =  R.id.create_total, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputTotal(){
        if(total.length() > 0){
            flag = flag | TOTAL;
        }else{
            flag = flag ^ TOTAL;
        }
        buttonEnable();
    }

    @OnTextChanged(value =  R.id.numOfMeet, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputMeet(){
        if(meet.length() > 0){
            flag = flag | MEET;
        }else{
            flag = flag ^ MEET;
        }
        buttonEnable();
    }

    @OnClick(value = {R.id.dayOfStart, R.id.dayOfEnd,R.id.btn_next})
    void OnClick(View view){
        switch (view.getId()){
            case R.id.dayOfStart:
                setDayOfStart.show();
                break;
            case R.id.dayOfEnd:
                setDayOfEnd.show();
                break;
            case R.id.btn_next:
                Intent intent = new Intent(this, InviteActivity.class);
                intent.putExtra("activity",100);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("goal",goal.getText().toString());
                intent.putExtra("total",total.getText().toString());
                intent.putExtra("meet",meet.getText().toString());
                intent.putExtra("start",start.getText().toString());
                intent.putExtra("end",end.getText().toString());
                startActivity(intent);
                break;
        }
    }


    private void buttonEnable(){
        if(flag == 0b111111){
            next.setEnabled(true);
        }else{
            next.setEnabled(false);
        }
    }

    private void setDate(int year, int month, int dayOfMonth, TextView view){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd",Locale.KOREAN);
        String date = format.format(calendar.getTime());
        view.setText(date);
        view.setTextColor(Color.parseColor("#0f1016"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("startDate",start.toString());
        outState.putString("endDate",end.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            start.setText(savedInstanceState.getString("startDate"));
            end.setText(savedInstanceState.getString("endDate"));
        }
    }
}
