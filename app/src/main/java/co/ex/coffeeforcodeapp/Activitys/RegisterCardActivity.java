package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import co.ex.coffeeforcodeapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterCardActivity extends AppCompatActivity {
    CircleImageView icon_ProfileUser_registerCard;
    CardView btnGoBackPurchase;

    //  Cards Flags
    ImageView img_btnMasterCard, img_btnVisaCard, img_btnMaestroCard;
    ConstraintLayout cardBtn_MasterCard, cardBtnVisa, cardBtn_Maestro;

    //  Card Pre View
    ImageView imgFlag_Pre_view_Card;

    //  Flags Images
    String flag_visa = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fvisa.png?alt=media&token=e9dd2e2b-dd30-444e-b745-2a1dd2273db9";
    String flag_mastercard = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmastercard.png?alt=media&token=79df43fd-494c-4160-93f1-7194266f76b9";
    String flag_maestro = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmaestro.png?alt=media&token=28fd5789-f277-4027-8b0f-9ff1f38b2d5d";


    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_card);
        icon_ProfileUser_registerCard = findViewById(R.id.icon_ProfileUser_registerCard);
        btnGoBackPurchase = findViewById(R.id.btnGoBackPurchase);
        img_btnMasterCard = findViewById(R.id.img_btnMasterCard);
        img_btnVisaCard = findViewById(R.id.img_btnVisaCard);
        img_btnMaestroCard = findViewById(R.id.img_btnMaestroCard);
        cardBtn_MasterCard = findViewById(R.id.cardBtn_MasterCard);
        cardBtnVisa = findViewById(R.id.cardBtnVisa);
        cardBtn_Maestro = findViewById(R.id.cardBtn_Maestro);
        imgFlag_Pre_view_Card = findViewById(R.id.imgFlag_Pre_view_Card);

        @SuppressLint("UseCompatLoadingForDrawables") Drawable cardFlagSelected = getResources().getDrawable(R.drawable.card_flag_selected);

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
        Picasso.get().load(img_user).into(icon_ProfileUser_registerCard);
        Picasso.get().load(flag_mastercard).into(img_btnMasterCard);
        Picasso.get().load(flag_visa).into(img_btnVisaCard);
        Picasso.get().load(flag_maestro).into(img_btnMaestroCard);
        cardBtn_MasterCard.setBackground(cardFlagSelected);
        Picasso.get().load(flag_mastercard).into(imgFlag_Pre_view_Card);

        cardBtn_MasterCard.setOnClickListener(v -> {
            removeAllFlagSelected();
            cardBtn_MasterCard.setBackground(cardFlagSelected);
            Picasso.get().load(flag_mastercard).into(imgFlag_Pre_view_Card);
        });

        cardBtnVisa.setOnClickListener(v -> {
            removeAllFlagSelected();
            cardBtnVisa.setBackground(cardFlagSelected);
            Picasso.get().load(flag_visa).into(imgFlag_Pre_view_Card);
        });

        cardBtn_Maestro.setOnClickListener(v -> {
            removeAllFlagSelected();
            cardBtn_Maestro.setBackground(cardFlagSelected);
            Picasso.get().load(flag_maestro).into(imgFlag_Pre_view_Card);
        });

        btnGoBackPurchase.setOnClickListener(v -> goTo_main());

    }

    private void removeAllFlagSelected() {
        @SuppressLint("UseCompatLoadingForDrawables") Drawable cardFlagDefault = getResources().getDrawable(R.drawable.default_card_flag);
        cardBtn_MasterCard.setBackground(cardFlagDefault);
        cardBtnVisa.setBackground(cardFlagDefault);
        cardBtn_Maestro.setBackground(cardFlagDefault);
    }

    private void goTo_main() {
        Intent GoBack_ToMain = new Intent(RegisterCardActivity.this, MainActivity.class);
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
        goTo_main();
    }
}