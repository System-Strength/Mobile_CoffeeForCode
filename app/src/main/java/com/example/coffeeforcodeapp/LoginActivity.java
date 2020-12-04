package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;

public class LoginActivity extends AppCompatActivity {
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    LottieAnimationView btnvoltarlogin;
    EditText edittextemail, edittextsenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnvoltarlogin = findViewById(R.id.btnvoltarlogin);
        edittextemail = findViewById(R.id.edittextemail);
        edittextsenha = findViewById(R.id.edittextsenha);

        //  Set timer to play animation
        timer.postDelayed(() -> btnvoltarlogin.playAnimation(),500);

        //  When click here will go back to MainActivity
        btnvoltarlogin.setOnClickListener(v -> {
            Intent voltaramain = new Intent(LoginActivity.this,MainActivity.class);
            voltaramain.putExtra("novotimerstart",200);
            voltaramain.putExtra("novotimershowoption",200);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerda, R.anim.mover_direita);
            ActivityCompat.startActivity(LoginActivity.this,voltaramain, activityOptionsCompat.toBundle());
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        Intent voltaramain = new Intent(LoginActivity.this,MainActivity.class);
        voltaramain.putExtra("novotimerstart",1);
        voltaramain.putExtra("novotimershowoption",200);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerda, R.anim.mover_direita);
        ActivityCompat.startActivity(LoginActivity.this,voltaramain, activityOptionsCompat.toBundle());
        finish();
    }
}