package com.android.firstlearners.learners.presenter;

import android.telephony.SmsManager;
import com.android.firstlearners.learners.contract.InviteContract;
import com.android.firstlearners.learners.etc.Address;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitePresenter implements InviteContract.Action {
    private Repository repository;
    private InviteContract.View view;
    private SharedPreferenceManager manager;
    private NetworkService networkService;
    public InvitePresenter(Repository repository, InviteContract.View view) {
        this.repository = repository;
        manager = repository.getSharedPreferenceManager();
        networkService = repository.getNetworkService();
        this.view = view;
    }

    @Override
    public void createStudy(String name, String goal, String numOfPerson, String numOfMeet, String start, String end, final List<Address> selectedItems) {
        if(networkService.isNetworkConnected()){
            if(networkService.getUserToken() != null){
                Map<String, Object> map = new HashMap<>();
                map.put("study_name",name);
                map.put("study_goal",goal);
                map.put("study_inwon",Integer.valueOf(numOfPerson));
                map.put("study_start",start);
                map.put("study_end",end);
                map.put("study_count",Integer.valueOf(numOfMeet));
                Call<Map<String, Object>> requestStudy = networkService.getApi().requestCreateStudy(networkService.getUserToken(),map);
                requestStudy.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if(response.isSuccessful()){
                            if((boolean)response.body().get("status")){
                                Map<String, Object> map = (Map<String, Object>)response.body().get("result");
                                int study_id = ((Double) map.get("study_id")).intValue();
                                manager.setString("study_id",String.valueOf(study_id));
                                invite(selectedItems);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {

                    }
                });
            }
        }
    }

    @Override
    public void invite(final List<Address> selectedItems) {
        int study_id = Integer.valueOf(manager.getString("study_id"));

        if(networkService.isNetworkConnected()){
            if(networkService.getUserToken() != null){
                Map<String, Object> map = new HashMap<>();
                List<Map<String, String>> list = new ArrayList<>();

                for(int i = 0 ; i < selectedItems.size() ; i++){
                    Map<String, String> subMap = new HashMap<>();
                    subMap.put("user_name",selectedItems.get(i).name);
                    subMap.put("user_phone",selectedItems.get(i).phoneNumber);
                    list.add(subMap);
                }

                map.put("study_users",list);

                Call<Map<String, Object>> requestInvite = networkService.getApi().requestInvite(networkService.getUserToken(),study_id, map);
                requestInvite.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if(response.isSuccessful()){
                            if((boolean)response.body().get("status")){
                                // 문자 시스템 하고
                                // 액티비티를 종료합니다.
                                SmsManager smsManager = SmsManager.getDefault();
                                String string = "스터디에 초대합니다.\n[스터디 초대 링크]";
                                for(int i = 0; i< selectedItems.size(); i++){
                                    smsManager.sendTextMessage(selectedItems.get(i).phoneNumber,null,string,null,null);
                                }
                                view.finishActivity();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {

                    }
                });
            }
        }
    }


}
