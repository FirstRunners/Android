package com.android.firstlearners.learners.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.firstlearners.learners.R;

public class MainActivity extends AppCompatActivity{

    private FragmentManager fragmentManager;
    private static final int CREATE_STUDY_ACTIVITY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

    }

    public void changeFragment(Fragment fragment){

    }

    public void startCreateStudyActivity(){
        Intent intent = new Intent(this, CreateStudyActivity.class);
        startActivityForResult(intent, CREATE_STUDY_ACTIVITY);
    }
}
