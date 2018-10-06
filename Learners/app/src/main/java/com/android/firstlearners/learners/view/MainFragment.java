package com.android.firstlearners.learners.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.contract.MainContract;
import com.android.firstlearners.learners.model.data.Study;
import com.android.firstlearners.learners.model.data.StudyUsers;
import com.android.firstlearners.learners.presenter.MainPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;

public class MainFragment extends Fragment implements MainContract.View {
    private MainPresenter presenter;
    private RealmList<StudyUsers> studyUsers;
    private MainActivity activity;
    private RankingRecyclerViewAdapter adapter;

    @BindView(R.id.dashboard_container) View dashboardContainer;
    @BindView(R.id.default_container) View defaultContainer;
    @BindView(R.id.btn_make) TextView btn_make;
    @BindView(R.id.btn_profile) ImageView profile;
    @BindView(R.id.progressStudy) ProgressBar progressBarStudy;
    @BindView(R.id.during) TextView during;
    @BindView(R.id.duringTitle) TextView duringTitle;
    @BindView(R.id.grade) TextView grade;
    @BindView(R.id.rankingBox) RecyclerView rankingBox;
    @BindView(R.id.study_day) TextView day;
    @BindView(R.id.study_goal) TextView goal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_main,container, false);
        ButterKnife.bind(this,fragment);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.takeStudy();
    }

    @OnClick(R.id.btn_make)
    public void makeStudy(){
        activity.startCreateStudyActivity();
    }

    @Override
    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setStudyData(Study study) {
        dashboardContainer.setVisibility(View.VISIBLE);
        defaultContainer.setVisibility(View.INVISIBLE);

        studyUsers = study.study_users;
        adapter = new RankingRecyclerViewAdapter(study);

        rankingBox.setAdapter(adapter);
        rankingBox.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);

        String start = format.format(calendar.getTime());

        int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, endDay);

        String end = format.format(calendar.getTime());

        int month = calendar.get(Calendar.MONTH);

        day.setText("스터디 "+study.study_day+"일째 | 목표 달성까지"+study.study_day_goal+"일");
        goal.setText(study.study_goal+"");
        during.setText(start+" ~ "+end);
        duringTitle.setText(month+"월 목표 달성률");
        progressBarStudy.setProgress(study.study_persent);
    }

}
