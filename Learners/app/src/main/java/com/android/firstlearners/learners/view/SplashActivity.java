package com.android.firstlearners.learners.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.contract.SplashContract;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.presenter.SplashPresenter;

public class SplashActivity extends AppCompatActivity implements SplashContract.View{

    private SplashContract.Action presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferenceManager manager = new SharedPreferenceManager(this);
        NetworkService networkService = ((LearnersApplication)getApplicationContext()).getNetworkService();
        Repository repository = new Repository(manager, networkService);
        presenter = new SplashPresenter(repository, this);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                presenter.login();
            }
        }, 1500);

    }

    @Override
    public void changeActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }
}
