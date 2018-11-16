package com.android.firstlearners.learners.contract;

import com.android.firstlearners.learners.model.data.Study;

public interface AttendanceDialogContract {
    interface View{
        void setData();
    }

    interface Action{
        void getData();
    }

}
