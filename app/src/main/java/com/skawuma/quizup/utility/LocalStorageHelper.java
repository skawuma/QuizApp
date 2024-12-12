package com.skawuma.quizup.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorageHelper {
    private static final String PREF_NAME = "LocalStorage";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public LocalStorageHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveData(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getData(String key) {
        return sharedPreferences.getString(key, null);
    }


    public void updateData(String key, String newValue) {
        if (sharedPreferences.contains(key)) {
            editor.putString(key, newValue);
            editor.apply();
        } else {
            throw new IllegalArgumentException("Key not found: " + key);
        }
    }

    public void deleteData(String key) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key);
            editor.apply();
        } else {
            throw new IllegalArgumentException("Key not found: " + key);
        }
    }

    public void clearAllData() {
        editor.clear();
        editor.apply();
    }
}

