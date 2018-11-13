package com.android.firstlearners.learners.contract;

import com.android.firstlearners.learners.model.data.Study;
import com.android.firstlearners.learners.presenter.MainPresenter;

import java.util.Map;

public interface MainContract {
    interface View{
        void setStudyData(Study study);
        void setShownView(boolean flag);
        void showAttendanceDialog(boolean flag, Map<String, Object> result);
        void startInvitationActivity(String userName);
    }

    interface Action{
        void isNetworkConnected();
        void clickAttendanceButton();
    }
}
