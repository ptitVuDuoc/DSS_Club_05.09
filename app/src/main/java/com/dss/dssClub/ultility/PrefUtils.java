package com.dss.dssClub.ultility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Computer on 11/6/2017.
 */

public class PrefUtils {


    private static final String DEF_NAME_PREF = "com.love.counter.DEF_NAME_PREF";
    private static final int NONE_INT_VAL = 0;
    private static final long NONE_LONG_AL = 0;
    private static final boolean NONE_BOOLEAN_AL = false;

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(DEF_NAME_PREF, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getPrefs(Context context, String key_name) {
        return context.getSharedPreferences(key_name, Context.MODE_PRIVATE);
    }


    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, null);
    }

    public static void putString(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).apply();
    }


    public static int getInt(Context context, String key) {
        return getPrefs(context).getInt(key, NONE_INT_VAL);
    }

    public static void putInt(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).apply();
    }
    public static  void putLong(Context context, String key, long value){
        getPrefs(context).edit().putLong(key, value).apply();
    }
    public static  long getLong(Context context, String key){
        return getPrefs(context).getLong(key,NONE_LONG_AL);
    }
    public static  void putBoolean(Context context, String key, boolean value){
        getPrefs(context).edit().putBoolean(key, value).apply();
    }
    public static  boolean getBoolean(Context context, String key){
        return getPrefs(context).getBoolean(key,NONE_BOOLEAN_AL);
    }
}
