package com.android.firstlearners.learners.contract;

import com.android.firstlearners.learners.etc.Address;

import java.util.List;

public interface InvitationContract {
    interface View{
        void finishDialog();
    }

    interface Action{
        void acceptInvitation();
    }
}
