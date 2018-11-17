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
import android.widget.Toast;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.model.DeleteListener;
import com.android.firstlearners.learners.model.DialogListener;
import com.android.firstlearners.learners.model.MypageApi;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
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


public class AccountDeleteDialog extends Dialog implements View.OnClickListener{
    @BindView(R.id.change_no)
    TextView change_no;
    @BindView(R.id.change_ok) TextView change_okay;
    MypageApi mypageApi;
    private DeleteListener deleteListener;

    public AccountDeleteDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_delete_dialog);
        ButterKnife.bind(this);


        //retrofit통신
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://54.180.69.136:3000/")
                .build();

        mypageApi = retrofit.create(MypageApi.class);
    }

    public void setDeleteListener(DeleteListener deleteListener){
        this.deleteListener = deleteListener;
    }



//    @OnClick(R.id.change_ok)
//    public void submit(){
//        dismiss();
//        Call<ResponseMessage> delete = mypageApi.deleteUser("");
//        delete.enqueue(new Callback<ResponseMessage>() {
//            @Override
//            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
//                ResponseMessage responseMessage = response.body();
//                Log.d("delete success",responseMessage.getMessage());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseMessage> call, Throwable t) {
//
//            }
//        });
//    }
    @OnClick(R.id.change_ok)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_ok:
                deleteListener.onDelete();
                dismiss();
                break;
            //아니오 눌렀을때도 처리해야함.
        }
    }
}
