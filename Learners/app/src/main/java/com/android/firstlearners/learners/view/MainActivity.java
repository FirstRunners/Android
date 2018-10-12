package com.android.firstlearners.learners.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity implements  MainContract.View{

    private MainContract.Action presenter;
    private RealmList<StudyUsers> studyUsers;
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
    @BindView(R.id.grade) TextView grade;
    @BindView(R.id.rankingBox) RecyclerView rankingBox;
    @BindView(R.id.study_day) TextView day;
    @BindView(R.id.study_goal) TextView goal;
    @BindView(R.id.refresh) ImageView refresh;
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
                presenter.takeStudy();
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.takeStudy();
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback);
    }

    @OnClick(value = {R.id.btn_make, R.id.refresh})
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.btn_make:
                Intent intent = new Intent(this, CreateStudyActivity.class);
                startActivity(intent);
                break;
            case R.id.refresh:
                presenter.takeStudy();
                break;
        }
    }

    @Override
    public void setStudyData(Study study) {
        dashboardContainer.setVisibility(View.VISIBLE);
        defaultContainer.setVisibility(View.INVISIBLE);
        networkContainer.setVisibility(View.INVISIBLE);

        studyUsers = study.study_users;
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
        for(StudyUsers studyUser : studyUsers){
            if(studyUser.user_idx == study.user_me){
                String gradeValue = "나의 등수는 " +(studyUser.user_idx+1)+"등입니다.";
                SpannableStringBuilder gradeBuilder = new SpannableStringBuilder(gradeValue);
                if(studyUser.user_idx < 10){
                    gradeBuilder.setSpan(new StyleSpan(Typeface.BOLD),7, 9,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else{
                    gradeBuilder.setSpan(new StyleSpan(Typeface.BOLD),7, 10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                grade.setText(gradeBuilder);
                break;
            }
        }
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
    protected void onStop() {
        super.onStop();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }
}
