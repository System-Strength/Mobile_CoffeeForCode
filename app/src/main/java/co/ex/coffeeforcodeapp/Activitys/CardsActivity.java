package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.Card.AsyncCards;
import co.ex.coffeeforcodeapp.Api.Card.CardService;
import co.ex.coffeeforcodeapp.Api.Card.DtoCard;
import co.ex.coffeeforcodeapp.R;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CardsActivity extends AppCompatActivity {
    CardView btnGoBackMain_cards, cardBtn_RegisteraCard_main;
    CircleImageView icon_ProfileUser_allcards;
    ConstraintLayout container_noCard;
    RecyclerView recyclerCards;

    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;

    //  Retrofit
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "card/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    LoadingDialog loadingDialog = new LoadingDialog(CardsActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        btnGoBackMain_cards = findViewById(R.id.btnGoBackMain_cards);
        icon_ProfileUser_allcards = findViewById(R.id.icon_ProfileUser_allcards);
        container_noCard = findViewById(R.id.container_noCard);
        recyclerCards = findViewById(R.id.recyclerCards);
        cardBtn_RegisteraCard_main = findViewById(R.id.cardBtn_RegisteraCard_main);
        cardBtn_RegisteraCard_main.setElevation(20);
        container_noCard.setElevation(20);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_user = bundle.getInt("id_user");
        email_user = bundle.getString("email_user");
        nm_user = bundle.getString("nm_user");
        cpf_user = bundle.getString("cpf_user");
        phone_user = bundle.getString("phone_user");
        zipcode = bundle.getString("zipcode");
        address_user = bundle.getString("address_user");
        complement = bundle.getString("complement");
        img_user = bundle.getString("img_user");
        partner = bundle.getInt("partner");
        partner_Startdate = bundle.getString("partner_Startdate");
        if (img_user == null || img_user.equals(" ") || img_user.equals("")){
            Log.d("ProfileImageStatus", "no image to load");
        }else {
            Picasso.get().load(img_user).into(icon_ProfileUser_allcards);
            Log.d("ProfileImageStatus", "loading image");
        }

        getCardsInformation();

        icon_ProfileUser_allcards.setOnClickListener(v -> {
            Intent GoTo_profile = new Intent(CardsActivity.this, ProfileActivity.class);
            GoTo_profile.putExtra("id_user", id_user);
            GoTo_profile.putExtra("email_user", email_user);
            GoTo_profile.putExtra("nm_user", nm_user);
            GoTo_profile.putExtra("cpf_user", cpf_user);
            GoTo_profile.putExtra("phone_user", phone_user);
            GoTo_profile.putExtra("zipcode", zipcode);
            GoTo_profile.putExtra("address_user", address_user);
            GoTo_profile.putExtra("complement", complement);
            GoTo_profile.putExtra("img_user", img_user);
            GoTo_profile.putExtra("partner", partner);
            GoTo_profile.putExtra("partner_Startdate", partner_Startdate);
            startActivity(GoTo_profile);
            finish();
        });

        btnGoBackMain_cards.setOnClickListener(v -> GoBack_toMain());

        cardBtn_RegisteraCard_main.setOnClickListener(v -> goto_card_resgister());

        container_noCard.setOnClickListener(v -> goto_card_resgister());
    }

    private void goto_card_resgister() {
        cardBtn_RegisteraCard_main.setElevation(0);
        container_noCard.setElevation(0);
        Intent goto_registercard = new Intent(CardsActivity.this, RegisterCardActivity.class);
        goto_registercard.putExtra("id_user", id_user);
        goto_registercard.putExtra("email_user", email_user);
        goto_registercard.putExtra("nm_user", nm_user);
        goto_registercard.putExtra("cpf_user", cpf_user);
        goto_registercard.putExtra("phone_user", phone_user);
        goto_registercard.putExtra("zipcode", zipcode);
        goto_registercard.putExtra("address_user", address_user);
        goto_registercard.putExtra("complement", complement);
        goto_registercard.putExtra("img_user", img_user);
        goto_registercard.putExtra("partner", partner);
        goto_registercard.putExtra("partner_Startdate", partner_Startdate);
        startActivity(goto_registercard);
        finish();
    }

    private void getCardsInformation() {
        loadingDialog.startLoading();
        CardService cardService = retrofitCard.create(CardService.class);
        Call<DtoCard> cardCall = cardService.getCardsOfUser(email_user);
        cardCall.enqueue(new Callback<DtoCard>() {
            @Override
            public void onResponse(@NotNull Call<DtoCard> call, @NotNull Response<DtoCard> response) {
                loadingDialog.dimissDialog();
                if (response.code() == 412){
                    container_noCard.setVisibility(View.VISIBLE);
                    recyclerCards.setVisibility(View.GONE);
                }else if(response.code() == 200){
                    assert response.body() != null;
                    LinearLayoutManager linearLayout = new LinearLayoutManager(CardsActivity.this);
                    recyclerCards.setLayoutManager(linearLayout);
                    AsyncCards asyncCards = new AsyncCards(recyclerCards, container_noCard, CardsActivity.this, email_user);
                    asyncCards.execute();
                    if (response.body().getLength() < 3){
                        cardBtn_RegisteraCard_main.setVisibility(View.VISIBLE);
                    }else{
                        cardBtn_RegisteraCard_main.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<DtoCard> call, @NotNull Throwable t) {
                loadingDialog.dimissDialog();
                Toast.makeText(CardsActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoBack_toMain(){
        Intent GoBack_ToMain = new Intent(CardsActivity.this, MainActivity.class);
        GoBack_ToMain.putExtra("id_user", id_user);
        GoBack_ToMain.putExtra("nm_user", nm_user);
        GoBack_ToMain.putExtra("email_user", email_user);
        GoBack_ToMain.putExtra("phone_user", phone_user);
        GoBack_ToMain.putExtra("zipcode", zipcode);
        GoBack_ToMain.putExtra("address_user", address_user);
        GoBack_ToMain.putExtra("complement", complement);
        GoBack_ToMain.putExtra("img_user", img_user);
        GoBack_ToMain.putExtra("address_user", address_user);
        GoBack_ToMain.putExtra("cpf_user", cpf_user);
        GoBack_ToMain.putExtra("partner", partner);
        GoBack_ToMain.putExtra("partner_Startdate", partner_Startdate);
        GoBack_ToMain.putExtra("statusavisoend","desativado");
        startActivity(GoBack_ToMain);
        finish();
    }

    @Override
    public void onBackPressed() {
        GoBack_toMain();
    }
}