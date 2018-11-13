package com.android.firstlearners.learners.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.contract.MainContract;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.etc.StudyProgressBar;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.model.data.Study;
import com.android.firstlearners.learners.model.data.StudyUsers;
import com.android.firstlearners.learners.presenter.MainPresenter;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity implements  MainContract.View{

    private MainContract.Action presenter;
    private RankingRecyclerViewAdapter adapter;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    @BindView(R.id.dashboard_container) View dashboardContainer;
    @BindView(R.id.default_container) View defaultContainer;
    @BindView(R.id.network_container) View networkContainer;
    @BindView(R.id.btn_make) TextView btn_make;
    @BindView(R.id.btn_profile) ImageView profile;
    @BindView(R.id.study_progress) StudyProgressBar progressBarStudy;
    @BindView(R.id.during) TextView during;
    @BindView(R.id.duringTitle) TextView duringTitle;
    @BindView(R.id.rankingBox) RecyclerView rankingBox;
    @BindView(R.id.study_day) TextView day;
    @BindView(R.id.study_goal) TextView goal;
    @BindView(R.id.refresh) ImageView refresh;
    @BindView(R.id.btn_attendance) FloatingActionButton btn_attendance;
    @BindView(R.id.first) ImageView first;
    @BindView(R.id.second) ImageView second;
    @BindView(R.id.third) ImageView third;
    @BindView(R.id.firstName) TextView firstName;
    @BindView(R.id.secondName) TextView secondName;
    @BindView(R.id.thirdName) TextView thirdName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        LearnersApplication context = (LearnersApplication) getApplicationContext();

        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context);
        NetworkService networkService = context.getNetworkService();
        Realm realm = context.getRealm();

        Repository repository = new Repository(sharedPreferenceManager, networkService, realm);
        presenter = new MainPresenter(repository, this);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(Network network) {
                presenter.isNetworkConnected();
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.isNetworkConnected();
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback);
    }

    @OnClick(value = {R.id.btn_make, R.id.refresh, R.id.btn_attendance})
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.btn_make:
                Intent intent = new Intent(this, CreateStudyActivity.class);
                startActivity(intent);
                break;
            case R.id.refresh:
                presenter.isNetworkConnected();
                break;
            case R.id.btn_attendance:
                presenter.clickAttendanceButton();
                break;
        }
    }

    @Override
    public void setStudyData(Study study) {
        dashboardContainer.setVisibility(View.VISIBLE);
        defaultContainer.setVisibility(View.INVISIBLE);
        networkContainer.setVisibility(View.INVISIBLE);

        RealmList<StudyUsers> studyUsers = study.study_users;

        if(studyUsers.size() > 2){
            thirdName.setText(studyUsers.get(2).user_name);
            third.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),IndividualDialog.class);
                    startActivity(intent);
                }
            });
            studyUsers.remove(2);
        }
        else if(studyUsers.size() > 1){
            secondName.setText(studyUsers.get(1).user_name);
            second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),IndividualDialog.class);
                    startActivity(intent);
                }
            });
            studyUsers.remove(1);
        }
        firstName.setText(studyUsers.get(0).user_name);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),IndividualDialog.class);
                startActivity(intent);
            }
        });

        studyUsers.remove(0);
        study.study_users = studyUsers;
        adapter = new RankingRecyclerViewAdapter(study);

        rankingBox.setAdapter(adapter);
        rankingBox.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);

        String start = format.format(calendar.getTime());

        int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, endDay);

        String end = format.format(calendar.getTime());

        int month = calendar.get(Calendar.MONTH) + 1;

        String string = "스터디 "+study.study_day+"일째 | 목표 달성까지 "+study.study_day_goal+"일";
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(string);
        int count = 1;
        int temp = study.study_day;

        while((temp = temp / 10) > 0){
            count++;
        }
        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED),4,4 + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        temp = study.study_day_goal;

        int count2 = 1;
        while((temp = temp / 10) > 0 ){
            count2++;
        }
        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED),17 + count, 17 + count + count2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        day.setText(stringBuilder);
        goal.setText(study.study_goal);
        during.setText(start+" ~ "+end);
        duringTitle.setText(month+"월 목표 달성률");
        //progressBarStudy.setProgress(study.study_persent);
        progressBarStudy.setProgress(30);
    }

    @Override
    public void setShownView(boolean flag) {
        if(flag){
            networkContainer.setVisibility(View.INVISIBLE);
            defaultContainer.setVisibility(View.VISIBLE);
        }
        else{
            networkContainer.setVisibility(View.VISIBLE);
            defaultContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showAttendanceDialog(boolean flag, Map<String, Object> result) {

        if(result != null){
            boolean check_flag = (Boolean) result.get("check_flag");

            Intent intent = new Intent(this, AttendanceDialog.class);
            String attend_users = (new Gson()).toJson( result.get("attend_users"));
            intent.putExtra("item", attend_users);

            if(check_flag){
                intent.putExtra("flag", true);
            }else{
                intent.putExtra("flag", false);
            }

            startActivity(intent);
        }
        else{
            Toast.makeText(this, "네트워크 연결을 확인해주세요.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void startInvitationActivity(String userName) {
        Intent intent = new Intent(this, InvitationActivity.class);
        intent.putExtra("userName",userName);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }


}
