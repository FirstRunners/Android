package com.android.firstlearners.learners.presenter;


import android.util.Log;

import com.android.firstlearners.learners.contract.MainContract;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.model.data.ResponseStudy;
import com.android.firstlearners.learners.model.data.Study;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContract.Action{
    private MainContract.View view;
    private SharedPreferenceManager sharedPreferenceManager;
    private NetworkService networkService;
    private Realm realm;

    public MainPresenter(Repository repository, MainContract.View view) {
        this.sharedPreferenceManager = repository.getSharedPreferenceManager();
        this.networkService = repository.getNetworkService();
        this.realm = repository.getRealm();
        this.view = view;
    }

    @Override
    public void takeStudy() {

        if(sharedPreferenceManager.getString("study_id") != null){
            if(networkService.isNetworkConnected()){
                isUserTokenEmpty();
            }else{
                takeStudyForDataBase();
            }
        }else{
            if(networkService.isNetworkConnected()){
                isUserTokenEmpty();
            }
            else{
                view.setShownView(false);
            }
        }
    }

    private void isUserTokenEmpty(){
        if(networkService.getUserToken() == null){

            String userName = sharedPreferenceManager.getString("user_name");
            String userEmail = sharedPreferenceManager.getString("user_email");

            Map<String, String> map = new HashMap<>();
            map.put("user_name",userName);
            map.put("user_email",userEmail);

            Call<Map<String, Object>> requestSignIn = networkService.getApi().requestSignIn(map);
            requestSignIn.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if(response.isSuccessful()) {
                        Map<String, Object> map = response.body();
                        Map<String, String> result = (Map<String, String>) map.get("result");
                        String token = result.get("user_token");
                        networkService.setUserToken(token);
                        Log.d("tttttttt","login");
                        takeStudyForNetwork();
                        }
                }
                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) { }
            });
        }
        else{
            Log.d("tttttttt","goStudy");
            takeStudyForNetwork();
        }
    }


    private void takeStudyForDataBase(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Study study = realm.where(Study.class).findFirst();
                if(study != null){
                    Log.d("test",study.study_goal);
                    view.setStudyData(study);
                }
            }
        });
    }

    private void takeStudyForNetwork(){
        Log.d("test","stduty");
        Call<ResponseStudy> responseStudy = networkService.getApi().requestStudy(networkService.getUserToken());
        responseStudy.enqueue(new Callback<ResponseStudy>() {
            @Override
            public void onResponse(Call<ResponseStudy> call, Response<ResponseStudy> response) {
                if(response.isSuccessful()){
                    if(response.body().status){
                        final Study result = response.body().result;
                        if(result != null){
                            sharedPreferenceManager.setString("study_id",String.valueOf(result.study_id));
                            //DB쿼리문
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    final Study study = realm.copyToRealmOrUpdate(result);
                                    view.setStudyData(study);
                                }
                            });
                        }else{
                            view.setShownView(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseStudy> call, Throwable t) {

            }
        });
    }

}
