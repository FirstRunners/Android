package com.android.firstlearners.learners.presenter;

import com.android.firstlearners.learners.contract.SignInContract;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.view.MainActivity;
import com.android.firstlearners.learners.view.SignUpActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class SignInPresenter implements SignInContract.Action{
    private Repository repository;
    private SignInContract.View view;

    public SignInPresenter(Repository repository, SignInContract.View view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void login() {
        view.showProgress();
        //네트워크 작업
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        view.hideProgress();

        //성공시(내부 저장소에 아이디, 비번 저장)
        view.changeActivity(MainActivity.class);

        //실패시(네트워크 연결이 안될경우)

        //실패시(회원가입을 먼저해야할 경우)
        view.changeActivity(SignUpActivity.class);
    }

    @Override
    public void signUp(String phoneNumber) {
        view.showProgress();

        //네트워크 작업

        view.hideProgress();
    }
}
