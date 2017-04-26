package com.apps.vithursan.inboxme;

import android.content.Context;
import android.content.SharedPreferences;

public class SearchFriendsHandler {
    private static SearchFriendsHandler mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "search-sp";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ID = "id";
    private static final String KEY_ACTION = "action";

    private SearchFriendsHandler(Context context) {
        mCtx = context;
    }

    public static synchronized SearchFriendsHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SearchFriendsHandler(context);
        }
        return mInstance;
    }

    public boolean getData(int id, String action, String username){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_ACTION, action);

        editor.apply();

        return true;
    }

    public int getUserID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, 0);
    }

    public String getUsername(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }
    public String getKeyAction(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACTION, null);
    }
    public boolean clearData(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
