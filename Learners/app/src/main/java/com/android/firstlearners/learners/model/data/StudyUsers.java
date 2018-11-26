package com.android.firstlearners.learners.model.data;

import io.realm.RealmObject;

public class StudyUsers extends RealmObject {
    public int user_idx;
    public String user_name;
    public int user_att_cnt;
    public int user_hw_cnt;
    public String user_img;
}
