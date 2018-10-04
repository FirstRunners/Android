package com.android.firstlearners.learners.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.contract.MainContract;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragment extends Fragment implements MainContract.View {
    private Repository repository;
    private MainPresenter presenter;

    @BindView(R.id.dashboard_container)
    View dashboardContainer;
    @BindView(R.id.default_container)
    View defaultContainer;

    @BindView(R.id.btn_make)
    TextView btn_make;

    MainActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_main,container, false);
        ButterKnife.bind(this,fragment);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity)getActivity();
    }


    @OnClick(R.id.btn_make)
    public void makeStudy(){
        activity.startCreateStudyActivity();
    }
}
