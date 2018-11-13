package com.android.firstlearners.learners.contract;

import com.android.firstlearners.learners.model.data.Study;

public interface IndiviualDialogContract {

    interface View{
        void setData(Study study);
    }

    interface Action{
        void getData();
    }

}
