package com.android.firstlearners.learners.contract;

public interface SplashContract {
    interface View{
        void changeActivity(Class cls);
    }

    interface Action{
        void login();
    }
}
