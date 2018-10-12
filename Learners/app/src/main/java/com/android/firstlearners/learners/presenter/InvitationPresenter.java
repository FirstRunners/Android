package com.android.firstlearners.learners.presenter;

import com.android.firstlearners.learners.contract.InvitationContract;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationPresenter implements InvitationContract.Action {
    private InvitationContract.View view;
    private Repository repository;

    public InvitationPresenter(InvitationContract.View view, Repository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void acceptInvitation() {
        NetworkService networkService = repository.getNetworkService();

        if(networkService.isNetworkConnected()){
            if(networkService.getUserToken() != null){
                Call<Map<String, Object>> requestAccept = networkService.getApi().requestAccept(networkService.getUserToken());
                requestAccept.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        view.finishDialog();
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {

                    }
                });
            }
        }
    }
}
