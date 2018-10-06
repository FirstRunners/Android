package com.android.firstlearners.learners.presenter;


import android.util.Log;

import com.android.firstlearners.learners.contract.MainContract;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.model.data.ResponseStudy;
import com.android.firstlearners.learners.model.data.Study;
import com.android.firstlearners.learners.model.data.StudyUsers;

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

        this.view.setPresenter(this);
    }

    @Override
    public void takeStudy() {

        if(sharedPreferenceManager.getString("study_id") != null){
            view.setShownView(true);
        }else{
            view.setShownView(false);
        }

        if(networkService.isNetworkConnected()){

            if(networkService.getUserToken() !=null){
                takeStudyForNetwork();
            }else{
                takeStudyForDataBase();
            }

        }else{
            takeStudyForDataBase();
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
        final Call<ResponseStudy> responseStudy = networkService.getApi().requestStudy(networkService.getUserToken());
        responseStudy.enqueue(new Callback<ResponseStudy>() {
            @Override
            public void onResponse(Call<ResponseStudy> call, Response<ResponseStudy> response) {
                if(response.isSuccessful()){
                    if(response.body().status){
                        final Study result = response.body().result;
                        Log.d("test","network_ok");
                        if(result != null){
                            sharedPreferenceManager.setString("study_id",String.valueOf(result.study_id));
                            Log.d("test","study_id");
                            //DB쿼리문
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Log.d("test",String.valueOf(result.study_goal));
                                    Study study = realm.copyToRealmOrUpdate(result);
                                    Log.d("test",String.valueOf(study.study_goal));
                                    //Study study = realm.where(Study.class).findFirst();
                                    /*
                                    if(study != null){
                                        study.study_id = result.study_id;
                                        study.study_day = result.study_day;
                                        study.study_day_goal = result.study_day_goal;
                                        study.study_goal = result.study_goal;
                                        study.study_persent = result.study_persent;
                                        study.study_count = result.study_count;

                                        for(int i=0 ; i < result.study_users.size(); i++){
                                            study.study_users.get(i).user_idx = result.study_users.get(i).user_idx;
                                            study.study_users.get(i).user_name = result.study_users.get(i).user_name;
                                            study.study_users.get(i).user_att_cnt = result.study_users.get(i).user_att_cnt;
                                            study.study_users.get(i).user_hw_cnt = result.study_users.get(i).user_hw_cnt;
                                        }
                                    }else{
                                        study = realm.copyToRealm(result);
                                    }*/

                                    view.setStudyData(study);
                                }
                            });
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
