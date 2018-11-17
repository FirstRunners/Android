package com.android.firstlearners.learners.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.model.MypageApi;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.model.data.ResponseMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyDateDialog extends Activity {
    @BindView(R.id.dayOfStart)
    TextView start;
    @BindView(R.id.dayOfEnd) TextView end;
    @BindView(R.id.canceal) TextView canceal;
    @BindView(R.id.apply) TextView apply;
    private DatePickerDialog setDayOfStart;
    private DatePickerDialog setDayOfEnd;
    private SimpleDateFormat format;
    private int flag;
    private NetworkService networkService;
    private MypageApi mypageApi;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.study_date_dialog);
        LearnersApplication context = (LearnersApplication) getApplicationContext();

        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context);
        networkService = context.getNetworkService();
        Realm realm = context.getRealm();

        Repository repository = new Repository(sharedPreferenceManager, networkService, realm);
        //retrofit통신
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://54.180.69.136:3000/")
                .build();

        mypageApi = retrofit.create(MypageApi.class);
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

            }
        },year, month, day);

        Log.d("test",String.valueOf(calendar.getTimeInMillis()));
        end.setText(format.format(calendar.getTime()));
    }

    @OnClick(value = {R.id.dayOfStart, R.id.dayOfEnd,R.id.apply, R.id.canceal})
    void OnClick(View view){
        switch (view.getId()){
            case R.id.dayOfStart:
                setDayOfStart.show();
                break;
            case R.id.dayOfEnd:
                setDayOfEnd.show();
                break;
            case R.id.apply:
                Call<ResponseMessage> period = mypageApi.studyPeriod(networkService.getUserToken(), start.toString(), end.toString());
                period.enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        ResponseMessage responseMessage = response.body();
                        Log.d("period change success",responseMessage.getMessage());
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {

                    }
                });
                finish();
                break;
            case R.id.canceal:
                finish();
                break;
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
