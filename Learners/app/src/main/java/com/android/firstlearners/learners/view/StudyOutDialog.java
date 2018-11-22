package com.android.firstlearners.learners.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.model.DeleteListener;
import com.android.firstlearners.learners.model.MypageApi;
import com.android.firstlearners.learners.model.StudyOutListener;
import com.android.firstlearners.learners.model.data.ResponseMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StudyOutDialog  extends Dialog implements  View.OnClickListener{
    @BindView(R.id.change_no)
    TextView change_no;
    @BindView(R.id.change_ok) TextView change_okay;
    MypageApi mypageApi;
    private StudyOutListener studyOutListener;
    public StudyOutDialog() {super(null);}
    public StudyOutDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.study_out_dialog);
        ButterKnife.bind(this);

        //retrofit통신
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://54.180.69.136:3000/")
                .build();

        mypageApi = retrofit.create(MypageApi.class);
    }
    public void setStudyOutListener(StudyOutListener studyOutListener){
        this.studyOutListener = studyOutListener;
    }

    @OnClick(value= {R.id.change_ok, R.id.change_no})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_ok:
                studyOutListener.studyOut();
                dismiss();
                break;
            case R.id.change_no:
                dismiss();
                break;
        }
    }
}
