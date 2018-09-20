package com.android.firstlearners.learners.presenter;

import com.android.firstlearners.learners.contract.MainContract;
import com.android.firstlearners.learners.model.Repository;

public class MainPresenter implements MainContract{
    private Repository repository;
    private MainContract.View view;

    public MainPresenter(Repository repository, View view) {
        this.repository = repository;
        this.view = view;
    }
}
