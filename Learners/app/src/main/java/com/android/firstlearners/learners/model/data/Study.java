package com.android.firstlearners.learners.model.data;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Study extends RealmObject {
    @PrimaryKey
    public int study_id;
    public int study_day;
    public int study_day_goal;
    public String study_goal;
    public int study_percent;
    public int study_count;
    public RealmList<StudyUsers> study_users;
    public int user_me;
    public String user_img;
}
