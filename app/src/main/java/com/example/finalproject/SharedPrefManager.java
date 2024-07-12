package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.finalproject.models.AdminModel;
import com.example.finalproject.models.StudentModel;
import com.google.gson.Gson;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_USER = "key_user";
    private static final String KEY_USER_TYPE = "key_user_type";
    private static final String TYPE_STUDENT = "student";
    private static final String TYPE_ADMIN = "admin";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPrefManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save User (Student or Admin)
    public void saveUser(Object user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(KEY_USER, json);

        if (user instanceof StudentModel) {
            editor.putString(KEY_USER_TYPE, TYPE_STUDENT);
        } else if (user instanceof AdminModel) {
            editor.putString(KEY_USER_TYPE, TYPE_ADMIN);
        }

        editor.apply();
    }

    // Get User (Student or Admin)
    public Object getUser() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_USER, null);
        String userType = sharedPreferences.getString(KEY_USER_TYPE, null);

        if (json != null && userType != null) {
            if (userType.equals(TYPE_STUDENT)) {
                return gson.fromJson(json, StudentModel.class);
            } else if (userType.equals(TYPE_ADMIN)) {
                return gson.fromJson(json, AdminModel.class);
            }
        }
        return null;
    }

    // Clear User
    public void clearUser() {
        editor.remove(KEY_USER);
        editor.remove(KEY_USER_TYPE);
        editor.apply();
    }
}
