package com.android.firstlearners.learners.presenter;

import android.util.Log;

import com.android.firstlearners.learners.contract.SplashContract;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.view.MainActivity;
import com.android.firstlearners.learners.view.SignInActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashPresenter implements SplashContract.Action{
    private Repository repository;
    private SplashContract.View view;

    public SplashPresenter(Repository repository, SplashContract.View view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void login() {
        final SharedPreferenceManager manager = repository.getSharedPreferenceManager();
        final NetworkService networkService = repository.getNetworkService();

        String userName = manager.getString("user_name");
        Log.d("test",userName);
        String userEmail = manager.getString("user_email");
        if(userEmail == null){
            view.changeActivity(SignInActivity.class);
        }else {

            Map<String, String> map = new HashMap<>();
            map.put("user_name",userName);
            map.put("user_email",userEmail);

            //네트워크가 연결되었는지, 연결되었으면 로그인 시도
            //연결이 안되어있어도, 일단 페이지를 넘깁니다.(데이터 베이스에 있는 것을 끌어오기)
            if(networkService.isNetworkConnected()){
                Call<Map<String, Object>> requestSignIn = networkService.getApi().requestSignIn(map);
                requestSignIn.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if(response.isSuccessful()) {
                            Map<String, Object> map = response.body();
                            Map<String, String> result = (Map<String, String>) map.get("result");
                            String token = result.get("user_token");
                            networkService.setUserToken(token);
                            view.changeActivity(MainActivity.class);
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) { }
                });
            }else{
                view.changeActivity(MainActivity.class);
            }
        }
    }
}
