package com.android.firstlearners.learners.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.contract.InvitationContract;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.presenter.InvitationPresenter;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvitationActivity extends AppCompatActivity implements InvitationContract.View{
    @BindView(R.id.accept) TextView accept;
    private InvitationContract.Action presenter;
    private Timer timer;
    private TimerTask task;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        ButterKnife.bind(this);

        LearnersApplication context = (LearnersApplication)getApplicationContext();
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context);
        NetworkService networkService = context.getNetworkService();

        Repository repository = new Repository(sharedPreferenceManager, networkService);
        presenter = new InvitationPresenter(this, repository);

        String userName = getIntent().getStringExtra("userName");
        accept.setText(userName);

        task = new TimerTask() {
            @Override
            public void run() {
                if(count > 0)
                    count--;
                else
                    timer.cancel();
            }
        };

        timer = new Timer();
    }

    @OnClick(R.id.accept)
    void onClick(){
        presenter.acceptInvitation();
    }

    @Override
    public void onBackPressed() {
        //취소에 관련된거
        if(count == 0){
            count = 7;
            timer.schedule(task,1000);
            Toast.makeText(this, "뒤로가기를 한번 더 누르시면 취소가 됩니다.",Toast.LENGTH_LONG).show();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

}
