package com.android.firstlearners.learners.view;

import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.presenter.MainPresenter;
import com.android.firstlearners.learners.view.schedule.ScheduleFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity{
    private FragmentManager fragmentManager;
    private static final int CREATE_STUDY_ACTIVITY = 100;

    @BindView(R.id.btn_goal) ImageView btn_goal;
    @BindView(R.id.btn_schedule) ImageView btn_schedule;
    @BindView(R.id.btn_attendance) ImageView btn_attendance;

    private Repository repository;
    private boolean flag_goal;
    private boolean flag_schedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        LearnersApplication context = (LearnersApplication) getApplicationContext();

        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context);
        NetworkService networkService = context.getNetworkService();
        Realm realm = context.getRealm();

        repository = new Repository(sharedPreferenceManager, networkService, realm);
        MainFragment mainFragment = new MainFragment();
        MainPresenter mainPresenter = new MainPresenter(repository, mainFragment);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_container,mainFragment).commit();
        flag_goal = false;
        flag_schedule = true;
        btn_goal.setEnabled(false);
    }

    public void changeFragment(Fragment fragment){
        if(fragment instanceof MainFragment){
            MainPresenter mainPresenter = new MainPresenter(repository, (MainFragment) fragment);
        }else{
            //스케줄 프레젠터 등록
        }
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.main_container,fragment).commit();
    }

    public void startCreateStudyActivity(){
        Intent intent = new Intent(this, CreateStudyActivity.class);
        startActivityForResult(intent, CREATE_STUDY_ACTIVITY);
    }

    @OnClick(value = {R.id.btn_goal, R.id.btn_schedule,R.id.btn_attendance})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_goal:
                changeFragment(new MainFragment());
                btn_goal.setImageResource(R.drawable.goal_bar_red);
                btn_schedule.setImageResource(R.drawable.scedule_bar_gray);

                flag_goal = false;
                flag_schedule = true;

                btn_goal.setEnabled(flag_goal);
                btn_schedule.setEnabled(flag_schedule);

                break;

            case R.id.btn_schedule:
                changeFragment(new ScheduleFragment());

                btn_goal.setImageResource(R.drawable.goal_bar_gray);
                btn_schedule.setImageResource(R.drawable.scedule_bar_red);

                flag_goal = true;
                flag_schedule = false;

                btn_goal.setEnabled(flag_goal);
                btn_schedule.setEnabled(flag_schedule);


                break;
        }
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        Log.d("test",String.valueOf(count));
        if(count > 0){
            if(flag_goal){
                flag_goal = false;
                flag_schedule = true;
                btn_goal.setEnabled(flag_goal);
                btn_schedule.setEnabled(flag_schedule);

                btn_goal.setImageResource(R.drawable.goal_bar_red);
                btn_schedule.setImageResource(R.drawable.scedule_bar_gray);
                Log.d("test","goal");
            }
            else{
                flag_goal = true;
                flag_schedule = false;
                btn_goal.setEnabled(flag_goal);
                btn_schedule.setEnabled(flag_schedule);

                btn_goal.setImageResource(R.drawable.goal_bar_gray);
                btn_schedule.setImageResource(R.drawable.scedule_bar_red);
                Log.d("test","schedule");

            }
        }
        super.onBackPressed();
    }
}
