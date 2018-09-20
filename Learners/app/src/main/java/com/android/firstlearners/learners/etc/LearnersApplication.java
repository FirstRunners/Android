package com.android.firstlearners.learners.etc;

import android.app.Application;

import com.android.firstlearners.learners.model.NetworkService;


import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LearnersApplication extends Application {
    private NetworkService networkService;
    private Realm realm;
    @Override
    public void onCreate() {
        super.onCreate();
        //setNetworkService();
        setDataBase();
    }

    private void setNetworkService(){
        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);
        networkService.setConnectivityManager(this);
    }

    private void setDataBase(){
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

    public Realm getRealm(){
        return realm;
    }
}
