package com.moviedirectory.devsoftware.moviedirectory.Util;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Manthan.Shah on 05-01-2018.
 */

public class Prefs {

    SharedPreferences preferences;

    public Prefs(Activity activity)
    {
        preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void setSearch(String search)
    {
        preferences.edit().putString("search", search).commit();
    }
    public String getSearch(){
        return preferences.getString("search", "batman");
    }

}
