package com.android.firstlearners.learners.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.model.DialogListener;
import com.android.firstlearners.learners.model.MypageApi;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.model.StudyOutListener;
import com.android.firstlearners.learners.model.data.ResponseMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//public class ManageStudyActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_study);
//    }
//}

public class ManageStudyActivity extends AppCompatActivity implements View.OnClickListener {
    ChangeCountDialog countDialog;
    StudyOutDialog studyOutDialog;
    private NetworkService networkService;
    MypageApi mypageApi;
    @BindView(R.id.study_duration)
    TextView study_duration;
    @BindView(R.id.study_out) TextView study_out;
    @BindView(R.id.study_invite) TextView study_invite;
    @BindView(R.id.study_count) TextView study_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_study);
        countDialog = new ChangeCountDialog(this);
        studyOutDialog = new StudyOutDialog(this);
        ButterKnife.bind(this);
        LearnersApplication context = (LearnersApplication) getApplicationContext();

        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context);
        networkService = context.getNetworkService();
        Realm realm = context.getRealm();

        Repository repository = new Repository(sharedPreferenceManager, networkService, realm);
        //retrofit통신
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://54.180.69.136:3000/")
                .build();

        mypageApi = retrofit.create(MypageApi.class);
    }
    //
//    @OnClick(value = {R.id.study_duration, R.id.study_out})
//    void OnClick(View view){
//        switch (view.getId()){
//            //스터디 관리 페이지로이동
//            case R.id.study_duration:
//                countDialog.show();
//                break;
//            case R.id.study_out:
//                break;
//
//        }
//    }
    @OnClick(value = {R.id.study_duration, R.id.study_out, R.id.study_invite, R.id.study_count})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //스터디 관리 페이지로이동
            case R.id.study_invite:
                Intent intent = new Intent(this, InviteActivity.class);
                // intent.putExtra("user_token",networkService.getUserToken());
                this.startActivity(intent);
                break;
            case R.id.study_duration:
                countDialog.show();
                countDialog.setDialogListener(new DialogListener() {
                    @Override
                    public void onPositive(int count) {
                        Call<ResponseMessage> change = mypageApi.changeCount(networkService.getUserToken(), count);
                        change.enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                ResponseMessage responseMessage = response.body();
                                Log.d("okay", responseMessage.getMessage());

                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {

                            }
                        });
                    }

                });
                break;
            case R.id.study_out:
                studyOutDialog.show();
                studyOutDialog.setStudyOutListener(new StudyOutListener() {
                    @Override
                    public void studyOut() {
                        Call<ResponseMessage> delete = mypageApi.studyOut(networkService.getUserToken());
                        delete.enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                ResponseMessage responseMessage = response.body();
                                Log.d("delete success",responseMessage.getMessage());
                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {

                            }
                        });
                    }
                });
                break;

            //스터디 기간 변경
            case R.id.study_count:
                Intent intent1 = new Intent(this, StudyDateDialog.class);
                intent1.putExtra("user_token",networkService.getUserToken());
                this.startActivity(intent1);
                break;
        }
    }
}

