package co.ex.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashActivity extends AppCompatActivity{
    Handler timer = new Handler();
    ConstraintLayout base_splash, base_itens_splash;
    CardView cardviewseemore, cardviewlogin;
    int TIME_STARTLOADING;
    int TIMER_SHOWOPTION;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        base_splash = findViewById(R.id.base_splash);
        base_itens_splash = findViewById(R.id.base_itens_splash);
        cardviewseemore = findViewById(R.id.cardviewsee_more);
        cardviewlogin = findViewById(R.id.cardviewlogin);
        base_splash.setVisibility(View.VISIBLE);
        base_itens_splash.setVisibility(View.GONE);
        cardviewseemore.setElevation(20);
        cardviewlogin.setElevation(20);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null){
            TIME_STARTLOADING = 500;
            TIMER_SHOWOPTION = 3000;
        }else {
            TIME_STARTLOADING = bundle.getInt("novotimerstart");
            TIMER_SHOWOPTION = bundle.getInt("novotimershowoption");
        }

        //  Verification of user preference information
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email") && sp.contains("pref_password"))
            GoToLogin();

        //  Set some thinks with gone and set background color

        timer.postDelayed(() -> {
            base_splash.setVisibility(View.VISIBLE);
            timer.postDelayed(() -> {
                base_splash.setVisibility(View.GONE);
                base_itens_splash.setVisibility(View.VISIBLE);
            },TIMER_SHOWOPTION);
        },TIME_STARTLOADING);

        //  WHen click here go to KnowMoreActivity
        cardviewseemore.setOnClickListener(v -> {
            cardviewseemore.setElevation(0);
            cardviewseemore.setEnabled(false);
            Intent goto_know_more = new Intent(SplashActivity.this, KnowMoreActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
            ActivityCompat.startActivity(SplashActivity.this, goto_know_more, activityOptionsCompat.toBundle());
            finish();
        });

        //  When click here will go to LoginActivity
        cardviewlogin.setOnClickListener(v -> {
            cardviewlogin.setElevation(0);
            cardviewseemore.setEnabled(false);
            GoToLogin();
        });
    }

    private void GoToLogin() {
        Intent goto_login = new Intent(SplashActivity.this, LoginActivity.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
        ActivityCompat.startActivity(SplashActivity.this, goto_login, activityOptionsCompat.toBundle());
        finish();
    }
}