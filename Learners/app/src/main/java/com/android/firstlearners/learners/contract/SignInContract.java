package com.android.firstlearners.learners.contract;

public interface SignInContract {
    interface View{
        void changeActivity(Class cls);
        void hideProgress();
        void showProgress();
    }

    interface Action{
        void login();
        void signUp(String phoneNumber);
    }
}
