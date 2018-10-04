package com.android.firstlearners.learners.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.firstlearners.learners.contract.SignInContract;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.view.MainActivity;
import com.android.firstlearners.learners.view.SignUpActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.util.CrashUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInPresenter implements SignInContract.Action{
    private Repository repository;
    private SignInContract.View view;

    public SignInPresenter(Repository repository, SignInContract.View view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void login(GoogleSignInAccount account) {
        view.showProgress();
        //네트워크 작업

        final SharedPreferenceManager manager = repository.getSharedPreferenceManager();
        final NetworkService networkService = repository.getNetworkService();

        final String name = account.getFamilyName() + "" + account.getGivenName();
        final String email = account.getEmail();

        Map<String, String> map = new HashMap<>();
        map.put("user_name",name);
        map.put("user_email",email);

        if(networkService.isNetworkConnected()) {
            final Call<Map<String, Object>> requestSignIn = networkService.getApi().requestSignIn(map);
            requestSignIn.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if(response.isSuccessful()){
                        Map<String, Object> map = response.body();
                        boolean status = (boolean)map.get("status");
                        if(status){
                            Log.d("test","ok");
                            Map<String, String> result = (Map<String, String>) map.get("result");
                            String token = result.get("user_token");
                            networkService.setUserToken(token);

                            manager.setString("user_name",name);
                            manager.setString("user_email",email);

                            view.hideProgress();
                            view.changeActivity(MainActivity.class);
                        }else {
                            Log.d("test","change");
                            view.hideProgress();
                            view.changeActivity(SignUpActivity.class);
                        }
                    }else{
                        view.hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Log.d("test",t.getMessage());
                    view.hideProgress();
                }
            });
        }else{

            //네트워크 연결이 안됬을때
        }
    }

    @Override
    public void signUp(GoogleSignInAccount account, String phoneNumber) {
        view.showProgress();

        final SharedPreferenceManager manager = repository.getSharedPreferenceManager();
        final NetworkService networkService = repository.getNetworkService();

        final String name = account.getFamilyName() + "" + account.getGivenName();
        final String email = account.getEmail();

        Map<String, String> map = new HashMap<>();
        map.put("user_name",name);
        map.put("user_email",email);
        map.put("user_phone",phoneNumber);

        if(networkService.isNetworkConnected()) {
            Call<Map<String, Object>> requestSignUp = networkService.getApi().requestSignUp(map);
            requestSignUp.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        Map<String, Object> map = response.body();
                        Map<String, String> result = (Map<String, String>) map.get("result");
                        String token = result.get("user_token");
                        networkService.setUserToken(token);

                        manager.setString("user_name",name);
                        manager.setString("user_email",email);

                        view.hideProgress();
                        view.changeActivity(MainActivity.class);
                    }else{
                        view.hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    view.hideProgress();
                }
            });
        }else{

            //네트워크 연결이 안됬을때
        }
    }
}
