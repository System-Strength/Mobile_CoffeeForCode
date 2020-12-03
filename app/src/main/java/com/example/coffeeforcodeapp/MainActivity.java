package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity{
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    LinearLayout navbarmain, bodymain, linearanimationcoffeeconstant;
    LottieAnimationView animationcoffee, animationcoffeeconstant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navbarmain = findViewById(R.id.navbarmain);
        bodymain = findViewById(R.id.bodymain);
        animationcoffee = findViewById(R.id.animationcoffee);
        animationcoffeeconstant = findViewById(R.id.animationcoffeeconstant);
        linearanimationcoffeeconstant = findViewById(R.id.linearanimationcoffeeconstant);

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
            },2500);
        },700);
    }
}