package com.android.firstlearners.learners.model;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.firstlearners.learners.model.data.ResponseStudy;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class NetworkService {
    private NetworkService.API api;
    private ConnectivityManager connectivityManager;
    private String userToken = null;
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

        @GET("main")
        Call<ResponseStudy> requestStudy(@Header("user_token") String user_token);
    }


}
