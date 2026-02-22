package com.mainscreen.ui.countdown;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.monkeyladder.R;

public class CountdownActivity extends AppCompatActivity {
    public static final String EXTRA_TARGET_ACTIVITY = "extra_target_activity";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_SECONDS = "extra_seconds";
    public static final String EXTRA_ICON_RES_ID = "extra_icon_res_id";

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_circle);

        TextView title = findViewById(R.id.countdownTitle);
        TextView number = findViewById(R.id.countdownNumber);
        ProgressBar ring = findViewById(R.id.countdownRing);
        ImageView icon = findViewById(R.id.countdownIcon);

        Intent intent = getIntent();
        String titleText = intent.getStringExtra(EXTRA_TITLE);
        int seconds = intent.getIntExtra(EXTRA_SECONDS, 3);
        int iconResId = intent.getIntExtra(EXTRA_ICON_RES_ID, 0);
        final String targetActivityName = intent.getStringExtra(EXTRA_TARGET_ACTIVITY);

        if (titleText != null) {
            title.setText(titleText);
        }
        if (iconResId != 0) {
            icon.setImageResource(iconResId);
        }

        final long totalMs = seconds * 1000L;
        ring.setMax((int) totalMs);
        ring.setProgress(0);
        number.setText(String.valueOf(seconds));

        timer = new CountDownTimer(totalMs, 50L) {
            @Override
            public void onTick(long millisUntilFinished) {
                long elapsed = totalMs - millisUntilFinished;
                ring.setProgress((int) elapsed);
                int display = (int) Math.ceil(millisUntilFinished / 1000.0);
                number.setText(String.valueOf(Math.max(display, 1)));
            }

            @Override
            public void onFinish() {
                ring.setProgress((int) totalMs);
                if (targetActivityName != null) {
                    try {
                        Class<?> clazz = Class.forName(targetActivityName);
                        startActivity(new Intent(CountdownActivity.this, clazz));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                finish();
            }
        };

        timer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) timer.cancel();
    }
}
