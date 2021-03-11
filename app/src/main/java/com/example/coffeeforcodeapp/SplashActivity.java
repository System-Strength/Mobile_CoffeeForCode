package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity{
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    CardView cardviewseemore, cardviewlogin;
    LinearLayout navbarmain, bodymain, linearanimationcoffeeconstant;
    LottieAnimationView animationcoffee, animationcoffeeconstant;
    int TIME_STARTLOADING;
    int TIMER_SHOWOPTION;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        navbarmain = findViewById(R.id.navbarmain);
        bodymain = findViewById(R.id.bodymain);
        animationcoffee = findViewById(R.id.animationcoffee);
        animationcoffeeconstant = findViewById(R.id.animationcoffeeconstant);
        linearanimationcoffeeconstant = findViewById(R.id.linearanimationcoffeeconstant);
        cardviewseemore = findViewById(R.id.cardviewseemore);
        cardviewlogin = findViewById(R.id.cardviewlogin);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null){
            TIME_STARTLOADING = 500;
            TIMER_SHOWOPTION = 3000;
        }else {
            TIME_STARTLOADING = bundle.getInt("novotimerstart");
            TIMER_SHOWOPTION = bundle.getInt("novotimershowoption");
        }

        //  Checking user information on preferences
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email") && sp.contains("pref_password"))
            GoToLogin();

        //  Set some thinks with gone and set background color
        navbarmain.setVisibility(View.GONE);
        animationcoffee.setVisibility(View.GONE);
        linearanimationcoffeeconstant.setVisibility(View.GONE);
        bodymain.setBackgroundResource(R.color.semeback);
        navbarmain.setBackgroundResource(R.color.semeback);

        timer.postDelayed(() -> {
            animationcoffee.setVisibility(View.VISIBLE);
            animationcoffee.playAnimation();
            timer.postDelayed(() -> {
                bodymain.setBackgroundResource(R.color.normalback);
                navbarmain.setBackgroundResource(R.color.normalback);
                animationcoffeeconstant.setVisibility(View.VISIBLE);
                navbarmain.setVisibility(View.VISIBLE);
                linearanimationcoffeeconstant.setVisibility(View.VISIBLE);
                animationcoffee.setVisibility(View.GONE);
                animationcoffeeconstant.playAnimation();
            },TIMER_SHOWOPTION);
        },TIME_STARTLOADING);

        //  WHen click here go to MoreActivity
        cardviewseemore.setOnClickListener(v -> {
            //  Soon
        });

        //  When click here will go to LoginActivity
        cardviewlogin.setOnClickListener(v -> GoToLogin());
    }

    private void GoToLogin() {
        Intent irparalogin = new Intent(SplashActivity.this, LoginActivity.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
        ActivityCompat.startActivity(SplashActivity.this,irparalogin, activityOptionsCompat.toBundle());
        finish();
    }
}