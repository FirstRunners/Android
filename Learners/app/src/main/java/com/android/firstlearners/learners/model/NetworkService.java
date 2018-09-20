package com.android.firstlearners.learners.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.http.GET;
import retrofit2.http.POST;

public abstract class NetworkService {
    private ConnectivityManager connectivityManager;

    public void setConnectivityManager(Context context){
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

    /*
    @GET

    @POST
    */
}
