package com.android.firstlearners.learners.model;

import io.realm.Realm;

public class Repository {
    private SharedPreferenceManager sharedPreferenceManager;
    private NetworkService networkService;
    private Realm realm;

    public Repository(SharedPreferenceManager sharedPreferenceManager) {
        this.sharedPreferenceManager = sharedPreferenceManager;
        this.networkService = null;
    }

    public Repository(NetworkService networkService) {
        this.networkService = networkService;
        this.sharedPreferenceManager = null;
    }

    public Repository(Realm realm) {
        this.realm = realm;
    }

    public Repository(SharedPreferenceManager sharedPreferenceManager, NetworkService networkService) {
        this.sharedPreferenceManager = sharedPreferenceManager;
        this.networkService = networkService;
    }

    public Repository(NetworkService networkService, Realm realm) {
        this.networkService = networkService;
        this.realm = realm;
    }

    public Repository(SharedPreferenceManager sharedPreferenceManager, NetworkService networkService, Realm realm) {
        this.sharedPreferenceManager = sharedPreferenceManager;
        this.networkService = networkService;
        this.realm = realm;
    }

    public SharedPreferenceManager getSharedPreferenceManager() {
        return sharedPreferenceManager;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

    public Realm getRealm() {
        return realm;
    }
}
