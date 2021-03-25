package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import co.ex.coffeeforcodeapp.R;

public class MembersActivity extends AppCompatActivity {
    CardView cardKauaMember, cardMathuesMember;
    ConstraintLayout btnviewportfolio_kaua, btnviewportfolio_matheus;
    LottieAnimationView btngoback_knowmore_memebers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        cardKauaMember = findViewById(R.id.cardKauaMember);
        cardMathuesMember = findViewById(R.id.cardMathuesMember);
        btnviewportfolio_kaua = findViewById(R.id.btnviewportfolio_kaua);
        btnviewportfolio_matheus = findViewById(R.id.btnviewportfolio_matheus);
        btngoback_knowmore_memebers = findViewById(R.id.btngoback_knowmore_memebers);
        //cardKauaMember.setElevation(20);
        cardMathuesMember.setElevation(20);

        //  When click in this card will to Kauã WebSite
        btnviewportfolio_kaua.setOnClickListener(v -> {
            String url = "https://www.kauavitorio.com/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        btnviewportfolio_matheus.setOnClickListener(v -> Toast.makeText(this, R.string.under_development, Toast.LENGTH_SHORT).show());

        btngoback_knowmore_memebers.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}