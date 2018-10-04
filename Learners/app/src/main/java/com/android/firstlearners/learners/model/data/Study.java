package com.android.firstlearners.learners.model.data;


import io.realm.RealmList;
import io.realm.RealmObject;

public class Study extends RealmObject {
    public int study_id;
    public int study_day;
    public int study_day_goal;
    public int study_goal;
    public int study_persent;
    public int study_count;
    public RealmList<StudyUsers> study_users;
}
