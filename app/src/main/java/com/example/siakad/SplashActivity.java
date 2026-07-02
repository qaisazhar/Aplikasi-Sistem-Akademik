package com.example.siakad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000;

    private ImageView imgLogo;
    private TextView tvAppName;
    private TextView tvUnivName;
    private ProgressBar progressBar;
    private Handler splashHandler;

    private final Runnable goToLoginRunnable = new Runnable() {
        @Override
        public void run() {
            navigateToLogin();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashHandler = new Handler(Looper.getMainLooper());

        initViews();

        startSplashAnimations();

        splashHandler.postDelayed(goToLoginRunnable, SPLASH_DELAY);
    }

    private void initViews() {
        imgLogo    = findViewById(R.id.imgLogo);
        tvAppName  = findViewById(R.id.tvAppName);
        tvUnivName = findViewById(R.id.tvUnivName);
        progressBar = findViewById(R.id.progressBar);
    }

    private void startSplashAnimations() {
        Animation scaleBounce = AnimationUtils.loadAnimation(this, R.anim.scale_bounce);
        imgLogo.startAnimation(scaleBounce);

        Animation slideUpFadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in);
        tvAppName.startAnimation(slideUpFadeIn);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeIn.setStartOffset(700);
        tvUnivName.startAnimation(fadeIn);

        Animation fadeInProgress = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeInProgress.setStartOffset(1000);
        progressBar.startAnimation(fadeInProgress);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashHandler != null) {
            splashHandler.removeCallbacks(goToLoginRunnable);
        }
    }
}