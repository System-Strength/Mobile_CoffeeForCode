package co.ex.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class SupportActivity extends AppCompatActivity {
    LottieAnimationView btngoback_knowmore_support;
    CardView btnMakeDonation_support, btnSeeAd_support;
    CardView cardDescSupport, cardview_wallet_support, cardview_ad_support;
    Handler timer = new Handler();
    Dialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        btngoback_knowmore_support = findViewById(R.id.btngoback_knowmore_support);
        btnMakeDonation_support = findViewById(R.id.btnMakeDonation_support);
        cardDescSupport = findViewById(R.id.cardDescSupport);
        cardview_wallet_support = findViewById(R.id.cardview_wallet_support);
        cardview_ad_support = findViewById(R.id.cardview_ad_support);
        btnSeeAd_support = findViewById(R.id.btnSeeAd_support);
        ad = new Dialog(this);
        cardDescSupport.setElevation(20);
        cardview_wallet_support.setElevation(20);
        cardview_ad_support.setElevation(20);
        btnMakeDonation_support.setElevation(20);
        btnSeeAd_support.setElevation(20);

        btnMakeDonation_support.setOnClickListener(v -> {
            btnMakeDonation_support.setElevation(0);
            String url = "https://www.paypal.com/donate?hosted_button_id=PRKZAKGHHKA7S";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            timer.postDelayed(() -> btnMakeDonation_support.setElevation(20),1500);
        });

        btnSeeAd_support.setOnClickListener(v -> {
            btnSeeAd_support.setElevation(0);
            ShowAdSorry();
            timer.postDelayed(() -> btnSeeAd_support.setElevation(20),1500);
        });

        btngoback_knowmore_support.setOnClickListener(v -> finish());
    }

    public void ShowAdSorry(){
        CardView btnSorryAd;
        ad.setContentView(R.layout.adapter_noad);
        btnSorryAd = ad.findViewById(R.id.btnSorryAd);

        btnSorryAd.setOnClickListener(v -> ad.dismiss());

        ad.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}