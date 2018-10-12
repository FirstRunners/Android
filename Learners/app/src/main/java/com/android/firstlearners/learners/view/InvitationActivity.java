package com.android.firstlearners.learners.view;

import android.net.Network;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.contract.InvitationContract;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.presenter.InvitationPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvitationActivity extends AppCompatActivity implements InvitationContract.View{
    @BindView(R.id.accept) TextView accept;
    private InvitationContract.Action presenter;
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
    }

    @OnClick(R.id.accept)
    void onClick(){
        presenter.acceptInvitation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //취소에 관련된거
    }

    @Override
    public void finishDialog() {
        finish();
    }
}
