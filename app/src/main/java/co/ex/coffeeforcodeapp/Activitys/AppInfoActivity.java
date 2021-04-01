package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import co.ex.coffeeforcodeapp.R;

public class AppInfoActivity extends AppCompatActivity {
    CardView btnMakeDonation_support_info , btnSeeAd_support_info;
    ConstraintLayout btnviewportfolio_kaua_info, btnviewportfolio_matheus_info;
    LottieAnimationView btngoInfo;
    Handler timer = new Handler();
    Dialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        btngoInfo = findViewById(R.id.btngoInfo);
        btnviewportfolio_kaua_info = findViewById(R.id.btnviewportfolio_kaua_info);
        btnviewportfolio_matheus_info = findViewById(R.id.btnviewportfolio_matheus_info);
        btnMakeDonation_support_info = findViewById(R.id.btnMakeDonation_support_info);
        btnSeeAd_support_info = findViewById(R.id.btnSeeAd_support_info);
        ad = new Dialog(this);
        btnMakeDonation_support_info.setElevation(20);
        btnSeeAd_support_info.setElevation(20);

        //  When click in this card will to Kauã WebSite
        btnviewportfolio_kaua_info.setOnClickListener(v -> {
            String url = "https://www.kauavitorio.com/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        btnviewportfolio_matheus_info.setOnClickListener(v -> Toast.makeText(this, R.string.under_development, Toast.LENGTH_SHORT).show());

        btnMakeDonation_support_info.setOnClickListener(v -> {
            btnMakeDonation_support_info.setElevation(0);
            String url = "https://www.paypal.com/donate?hosted_button_id=PRKZAKGHHKA7S";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            timer.postDelayed(() -> btnMakeDonation_support_info.setElevation(20),1500);
        });

        btnSeeAd_support_info.setOnClickListener(v -> {
            btnSeeAd_support_info.setElevation(0);
            ShowAdSorry();
            timer.postDelayed(() -> btnSeeAd_support_info.setElevation(20),1500);
        });

        btngoInfo.setOnClickListener(v -> finish());
    }

    public void ShowAdSorry(){
        CardView btnSorryAd;
        ad.setContentView(R.layout.adapter_noad);
        btnSorryAd = ad.findViewById(R.id.btnSorryAd);

        btnSorryAd.setOnClickListener(v -> ad.dismiss());

        ad.show();
    }
}