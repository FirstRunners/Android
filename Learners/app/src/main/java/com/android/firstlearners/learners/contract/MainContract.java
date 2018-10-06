package com.android.firstlearners.learners.contract;

import com.android.firstlearners.learners.model.data.Study;
import com.android.firstlearners.learners.presenter.MainPresenter;

public interface MainContract {
    interface View{
        void setPresenter(MainPresenter presenter);
        void setStudyData(Study study);
    }

    interface Action{
        void takeStudy();
    }
}
