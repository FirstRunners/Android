package com.android.firstlearners.learners.presenter;


import com.android.firstlearners.learners.contract.AttendanceDialogContract;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceDialogPresenter implements AttendanceDialogContract.Action{
    private NetworkService networkService;
    private SharedPreferenceManager sharedPreferenceManager;
    private AttendanceDialogContract.View view;
    public AttendanceDialogPresenter(Repository repository, AttendanceDialogContract.View view) {
        networkService = repository.getNetworkService();
        sharedPreferenceManager = repository.getSharedPreferenceManager();
        this.view = view;
    }

    @Override
    public void getData() {
        int study_id = Integer.valueOf(sharedPreferenceManager.getString("study_id"));
        Call<Map<String, Object>> requestAttendance = networkService.getApi().requestAttendance(networkService.getUserToken(),study_id);
        requestAttendance.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if(response.isSuccessful()){
                    boolean status = (Boolean) response.body().get("status");
                    if(status){

                    }
                    else{

                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });
    }
}
