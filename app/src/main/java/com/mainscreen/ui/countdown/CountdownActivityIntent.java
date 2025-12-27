package com.mainscreen.ui.countdown;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class CountdownActivityIntent {
    private final Intent intent;
    private final AppCompatActivity activity;

    public CountdownActivityIntent(AppCompatActivity activity) {
        this.activity = activity;
        this.intent = new Intent(activity, CountdownActivity.class);
    }

    public CountdownActivityIntent setTitle(String title) {
        intent.putExtra(CountdownActivity.EXTRA_TITLE, title);
        return this;
    }

    public CountdownActivityIntent setSeconds(int seconds) {
        intent.putExtra(CountdownActivity.EXTRA_SECONDS, seconds);
        return this;
    }

    public CountdownActivityIntent setIconResId(int iconResId) {
        intent.putExtra(CountdownActivity.EXTRA_ICON_RES_ID, iconResId);
        return this;
    }

    public CountdownActivityIntent setTargetActivity(String fullyQualifiedActivityName) {
        intent.putExtra(CountdownActivity.EXTRA_TARGET_ACTIVITY, fullyQualifiedActivityName);
        return this;
    }

    public void startActivity() {
        activity.startActivity(intent);
    }
}
