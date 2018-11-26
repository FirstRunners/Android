package com.android.firstlearners.learners.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.contract.IndiviualDialogContract;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.model.data.Study;
import com.android.firstlearners.learners.presenter.IndiviualDialogPresenter;
import com.android.firstlearners.learners.presenter.MainPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class IndividualDialog extends AppCompatActivity implements IndiviualDialogContract.View {
    @BindView(R.id.profile)
    ImageView profile;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.isfirst)
    TextView first;
    @BindView(R.id.numOfAttendance)
    TextView att;
    @BindView(R.id.btn_ok)
    TextView btn_ok;

    private IndiviualDialogContract.Action presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_indiviual_dialog);
        ButterKnife.bind(this);

        LearnersApplication context = (LearnersApplication) getApplicationContext();
        Realm realm = context.getRealm();
        Repository repository = new Repository(realm);

        presenter = new IndiviualDialogPresenter(repository, this);
        presenter.getData();
    }

    @OnClick(R.id.btn_ok)
    public void OnClick() {
        finish();
    }


    @Override
    public void setData(Study study) {
        Intent intent = getIntent();
        int user_idx = intent.getIntExtra("user_idx", -1);

        name.setText(study.study_users.get(user_idx).user_name);
        att.setText(String.valueOf(study.study_users.get(user_idx).user_att_cnt));

        if (user_idx == 0) {
            first.setVisibility(View.VISIBLE);
        }
        //userimage = NULL
        if(study.study_users.get(user_idx).user_img.equals("")) {
            Glide.with(getApplicationContext())
                    .applyDefaultRequestOptions(RequestOptions.circleCropTransform())
                    .load(R.drawable.basic_profile).into(profile);
        }
        else
        {
            Glide.with(getApplicationContext())
                    .applyDefaultRequestOptions(RequestOptions.circleCropTransform())
                    .load(study.study_users.get(user_idx).user_img).into(profile);
        }
    }


}
