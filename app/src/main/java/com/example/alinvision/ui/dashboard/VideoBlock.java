package com.example.alinvision.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;

public class VideoBlock {
    private String caption;
    private Context context;
    private int position;
    private int selectedImageResId;
    private static final String PREFS_NAME = "VideoBlockPrefs";
    private static final String KEY_CAPTION = "caption";
    private static final String KEY_IMAGE_RES_ID = "imageResId";

    public VideoBlock(Context context, int position) {
        this.caption = loadCaptionFromPreferences(context, position);
        this.selectedImageResId = loadSelectedImageResIdFromPreferences(context, position);
        this.context = context;
        this.position = position;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        saveCaptionToPreferences(context, position);
    }

    public int getSelectedImageResId() {
        return selectedImageResId;
    }

    public void setSelectedImageResId(int selectedImageResId) {
        this.selectedImageResId = selectedImageResId;
        saveSelectedImageResIdToPreferences(context, position);
    }

    private void saveCaptionToPreferences(Context context, int position) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CAPTION + "_" + position, caption);
        editor.apply();
    }

    private String loadCaptionFromPreferences(Context context, int position) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_CAPTION + "_" + position, "");
    }

    private void saveSelectedImageResIdToPreferences(Context context, int position) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_IMAGE_RES_ID + "_" + position, selectedImageResId);
        editor.apply();
    }

    private int loadSelectedImageResIdFromPreferences(Context context, int position) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_IMAGE_RES_ID + "_" + position, 0);
    }
}
