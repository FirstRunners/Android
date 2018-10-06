package com.android.firstlearners.learners.contract;

import com.android.firstlearners.learners.etc.Address;

import java.util.List;

public interface InviteContract {
    interface View{
        void finishActivity();
    }

    interface Action{
        void createStudy(String name, String goal, String numOfPerson, String numOfMeet, String start, String end,
                         List<Address> selectedItems);
        void invite(List<Address> selectedItems);
    }
}
