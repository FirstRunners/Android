package com.android.firstlearners.learners.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.contract.IndiviualDialogContract;
import com.android.firstlearners.learners.model.data.Study;

import butterknife.ButterKnife;

public class IndividualDialog extends AppCompatActivity implements IndiviualDialogContract.View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_indiviual_dialog);
        ButterKnife.bind(this);
    }

    @Override
    public void setData(Study study) {

    }
}
