package com.android.firstlearners.learners.presenter;

import com.android.firstlearners.learners.contract.SplashContract;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.view.MainActivity;
import com.android.firstlearners.learners.view.SignInActivity;

public class SplashPresenter implements SplashContract.Action{
    private Repository repository;
    private SplashContract.View view;

    public SplashPresenter(Repository repository, SplashContract.View view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void login() {
        SharedPreferenceManager manager = repository.getSharedPreferenceManager();
        NetworkService networkService = repository.getNetworkService();

        String userId = manager.getString("Id");
        String userPassword = manager.getString("password");
        if(userId == null){
            view.changeActivity(SignInActivity.class);
        }else {
            //네트워크가 연결되었는지, 연결되었으면 로그인 시도
            //연결이 안되어있어도, 일단 페이지를 넘깁니다.(데이터 베이스에 있는 것을 끌어오기)
            view.changeActivity(MainActivity.class);
        }
    }


    private void connectNetwork(){

    }
}
