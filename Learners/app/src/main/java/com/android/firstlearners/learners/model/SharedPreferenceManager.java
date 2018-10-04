package com.android.firstlearners.learners.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public SharedPreferenceManager(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences("Learners",Context.MODE_PRIVATE);
    }


    //일단은 String

    public String getString(String key){
        String value = null;
        if(sharedPreferences != null){
            value = sharedPreferences.getString(key, null);
        }
        return value;
    }

    public void setString(String key, String value){
        if(sharedPreferences != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

}
