package com.android.firstlearners.learners.contract;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface SignInContract {
    interface View{
        void changeActivity(Class cls);
        void hideProgress();
        void showProgress();
    }

    interface Action{
        void login(GoogleSignInAccount account);
        void signUp(GoogleSignInAccount account, String phoneNumber);
    }
}
