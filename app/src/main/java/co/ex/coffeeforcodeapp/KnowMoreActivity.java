package co.ex.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;

import org.jetbrains.annotations.NotNull;

import co.ex.coffeeforcodeapp.Api.Products.DtoMenuById;
import co.ex.coffeeforcodeapp.Api.Products.MenuService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class KnowMoreActivity extends AppCompatActivity {
    LottieAnimationView btngoback_splash;
    CardView btnMembers, btnSupport;
    Handler timer = new Handler();
    final Retrofit retrofitPreLoad = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/products/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more);
        btngoback_splash = findViewById(R.id.btngoback_splash);
        btnMembers = findViewById(R.id.btnMembers);
        btnSupport = findViewById(R.id.btnSupport);
        btnMembers.setElevation(20);
        btnSupport.setElevation(20);
        PreloadServer();

        btnMembers.setOnClickListener(v -> {
            btnMembers.setElevation(0);
            btnSupport.setElevation(20);
            Intent goTo_Members = new Intent(KnowMoreActivity.this, MembersActivity.class);
            startActivity(goTo_Members);
            timer.postDelayed(() -> btnMembers.setElevation(20),1500);
        });

        btnSupport.setOnClickListener(v -> {
            btnMembers.setElevation(20);
            btnSupport.setElevation(0);
            Intent goto_Support = new Intent(KnowMoreActivity.this, SupportActivity.class);
            startActivity(goto_Support);
            timer.postDelayed(() -> btnSupport.setElevation(20),1500);
        });

        btngoback_splash.setOnClickListener(v -> goBack_Splash());

    }

    private void PreloadServer() {
        int cd_prod = 14;
        MenuService menuService = retrofitPreLoad.create(MenuService.class);
        Call<DtoMenuById> call = menuService.getProductByCd(cd_prod);
        call.enqueue(new Callback<DtoMenuById>() {
            @Override
            public void onResponse(@NotNull Call<DtoMenuById> call, @NotNull Response<DtoMenuById> response) {
                Log.d("PreLoadStatus", "Server ON");
            }
            @Override
            public void onFailure(@NotNull Call<DtoMenuById> call, @NotNull Throwable t) {
                //Toast.makeText(CreateAccountActivity.this, "We have a problem with our servers, please try again later !!\nEstamos com problema em nossos servidores tente novamente mais tarde!!", Toast.LENGTH_SHORT).show();
                Log.d("ServerError", t.getMessage());
            }
        });
    }

    private void goBack_Splash() {
        Intent goBack_to_splash = new Intent(KnowMoreActivity.this, SplashActivity.class);
        goBack_to_splash.putExtra("new_time_to_start",20);
        goBack_to_splash.putExtra("new_time_to_showOptions",20);
        startActivity(goBack_to_splash);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack_Splash();
    }
}