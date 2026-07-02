package com.example.siakad;

import android.content.Context;
import android.content.SharedPreferences;

public final class ProfilePhotoManager {

    private static final String PREF_NAME = "profile_photo_preferences";
    private static final String KEY_PREFIX = "profile_photo_uri_";

    private ProfilePhotoManager() {
    }

    public static void saveProfilePhotoUri(Context context, String npm, String uri) {
        getPreferences(context)
                .edit()
                .putString(getKey(npm), uri)
                .apply();
    }

    public static String getProfilePhotoUri(Context context, String npm) {
        return getPreferences(context).getString(getKey(npm), "");
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private static String getKey(String npm) {
        return KEY_PREFIX + (npm == null ? "" : npm);
    }
}
