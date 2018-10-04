package com.android.firstlearners.learners.presenter;


import com.android.firstlearners.learners.contract.MainContract;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.data.ResponseStudy;
import com.android.firstlearners.learners.model.data.Study;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContract.Action{
    private MainContract.View view;
    private NetworkService networkService;
    private Realm realm;

    public MainPresenter(Repository repository, MainContract.View view) {
        this.networkService = repository.getNetworkService();
        this.realm = repository.getRealm();
        this.view = view;
    }

    @Override
    public void takeStudy() {
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
                //뷰로 넘기기
            }
        });
    }

    private void takeStudyForNetwork(){
        Call<ResponseStudy> responseStudy = networkService.getApi().requestStudy(networkService.getUserToken());
        responseStudy.enqueue(new Callback<ResponseStudy>() {
            @Override
            public void onResponse(Call<ResponseStudy> call, Response<ResponseStudy> response) {
                if(response.isSuccessful()){
                    if(response.body().status){
                        final Study result = response.body().result;
                        //DB쿼리문
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Study study = realm.where(Study.class).findFirst();
                                study = result;
                                //뷰로 넘기기
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseStudy> call, Throwable t) {

            }
        });
    }

}
