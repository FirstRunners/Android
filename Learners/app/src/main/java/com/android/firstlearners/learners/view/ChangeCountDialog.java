package com.android.firstlearners.learners.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.model.DialogListener;
import com.android.firstlearners.learners.model.MypageApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChangeCountDialog extends Dialog implements View.OnClickListener{
    @BindView(R.id.change_cnt)
    EditText change_cnt;
    @BindView(R.id.change_no)
    TextView change_no;
    @BindView(R.id.change_ok) TextView change_okay;
    MypageApi mypageApi;
    private DialogListener dialogListener;

    public ChangeCountDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //   getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.change_count_dialog);
        ButterKnife.bind(this);

        //retrofit통신
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://54.180.69.136:3000/")
                .build();

        mypageApi = retrofit.create(MypageApi.class);
    }

    public ChangeCountDialog(){
        super(null);
    }

    public void setDialogListener(DialogListener dialogListener){
        this.dialogListener = dialogListener;
    }

    @OnClick(value={R.id.change_ok, R.id.change_no})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_ok:
                String out = change_cnt.getText().toString();
                if(out=="최소 1회") {
                    Toast.makeText(getContext(), "값을 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else{
                    int cnt = Integer.parseInt(out);
                    dialogListener.onPositive(cnt);
                    dismiss();
                    change_cnt.setText("");
                    change_cnt.setHint("최소 1회");
                }
                break;
            // "취소"버튼
            case R.id.change_no:
                dismiss();
                break;
        }
    }
}
