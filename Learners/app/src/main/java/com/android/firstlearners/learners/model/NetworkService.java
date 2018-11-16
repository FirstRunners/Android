package com.android.firstlearners.learners.model;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.util.Log;

import com.android.firstlearners.learners.model.data.ResponseStudy;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class NetworkService {
    private NetworkService.API api;
    private ConnectivityManager connectivityManager;
    private String userToken = null;
    private boolean flag = true;
    public NetworkService(Context context, API api) {
        this.api = api;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    public boolean isNetworkConnected(){
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null){
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

    public API getApi() {
        return api;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserToken(){
        return userToken;
    }
    public interface API{
        @POST("user/signin")
        Call<Map<String, Object>> requestSignIn(@Body Map<String, String> map);

        @POST("user/signup")
        Call<Map<String, Object>> requestSignUp(@Body Map<String, String> map);

        @GET("main/graph")
        Call<ResponseStudy> requestStudy(@Header("user_token") String user_token);

        @POST("study/new")
        Call<Map<String, Object>> requestCreateStudy(@Header("user_token") String user_token,@Body Map<String, Object> map);

        @POST("study/invite/:{study_id}")
        Call<Map<String, Object>> requestInvite(@Header("user_token") String user_token, @Path("study_id") int study_id, @Body Map<String, Object> map);

        @POST("study/invite/accept")
        Call<Map<String, Object>> requestAccept(@Header("user_token") String user_token);

        @POST("study/invite/check")
        Call<Map<String, Object>> requestCheck(@Header("user_token") String user_token);

        @POST("/main/check/{study_id}")
        Call<Map<String,Object>>  requestAttendance(@Header("user_token") String user_token, @Path("study_id") int study_id);

    }


}
