package com.android.firstlearners.learners.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.contract.SignInContract;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.presenter.SignInPresenter;
import com.android.firstlearners.learners.presenter.SplashPresenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements SignInContract.View{
    @BindView(R.id.btn_signIn)
    TextView btn_signIn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SignInPresenter presenter;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private final int ACTIVITY_RESULT = 1;
    private final int RC_SIGN_IN = 2;
    private final String TAG = "Google SignIn Error";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        SharedPreferenceManager manager = new SharedPreferenceManager(this);
        NetworkService networkService = ((LearnersApplication)getApplicationContext()).getNetworkService();
        Repository repository = new Repository(manager, networkService);
        presenter = new SignInPresenter(repository, this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @OnClick(R.id.btn_signIn)
    public void OnClick(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void changeActivity(Class cls) {
        if(cls.getName().contains("MainActivity")){
            Intent intent = new Intent(this, cls);
            startActivity(intent);
            finish();
            return;
        }
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, ACTIVITY_RESULT);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        btn_signIn.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        btn_signIn.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_RESULT && resultCode == RESULT_OK){
            String phoneNumber = data.getStringExtra("phoneNumber");
            presenter.signUp(account, phoneNumber);
        }else if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            // 유저에 대한 데이터를 얻고 넘긴다.
            presenter.login(account);
        } catch (ApiException e) {

            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
