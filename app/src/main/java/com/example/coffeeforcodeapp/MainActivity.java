package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeforcodeapp.Api.PopularProducts.AsyncPopularProducts;
import com.example.coffeeforcodeapp.Api.UserImage.AsyncUserImage;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MainActivity extends AppCompatActivity {
    //  User img
    CircleImageView icon_ProfileUser_principal;
    TextView txt_Name_user;
    CardView cardview_notPartner, card_Be_Partner, card_See_Cards, card_Shopping_Cart, AnimationLoading_PopularProducts;
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    Dialog warning_address;
    RecyclerView recyclerPopularProducts;
    private BottomSheetDialog bottomSheetDialog;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";
    int id_user, partner;
    String nm_user, email_user, phone_user, address_user, complement, img_user, cpf_user, Show_warning_address, partner_Startdate;
    final Retrofit Productsretrofit = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/products/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_Name_user = findViewById(R.id.txt_Name_user);
        recyclerPopularProducts = findViewById(R.id.recyclerPopularProducts);
        AnimationLoading_PopularProducts = findViewById(R.id.AnimationLoading_PopularProducts);
        cardview_notPartner = findViewById(R.id.cardview_notPartner);
        card_Be_Partner = findViewById(R.id.card_Be_Partner);
        card_See_Cards = findViewById(R.id.card_See_Cards);
        card_Shopping_Cart = findViewById(R.id.card_Shopping_Cart);
        icon_ProfileUser_principal = findViewById(R.id.icon_ProfileUser_principal);
        warning_address = new Dialog(this);


        //  Get information for login of client
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null){
            Show_warning_address = "ativado";
        }else {
            Show_warning_address = bundle.getString("statusavisoend");
            email_user  = bundle.getString("email_user");
            id_user  = bundle.getInt("id_user");
            nm_user  = bundle.getString("nm_user");
            email_user  = bundle.getString("email_user");
            phone_user  = bundle.getString("phone_user");
            address_user  = bundle.getString("address_user");
            complement  = bundle.getString("complement");
            img_user  = bundle.getString("img_user");
            cpf_user  = bundle.getString("cpf_user");
            partner  = bundle.getInt("partner");
            partner_Startdate  = bundle.getString("partner_Startdate");
        }

        icon_ProfileUser_principal.setVisibility(View.VISIBLE);

        if (img_user.length() > 1){
            loadPopularProducts();
            loadProfileImage();
        }else {
            loadPopularProducts();
        }


        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //  Set somethings with gone and visible

        recebendodadosiniciaisdocliente();


        //  When click in this card user will to SejaParceiroActivity
        card_Be_Partner.setOnClickListener(v -> {
            Intent irparavirarparceiro = new Intent(MainActivity.this,SejaParceiroActivity.class);
            //irparavirarparceiro.putExtra("emailuser",emaillogado);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerdarapido, R.anim.mover_direitarapido);
            ActivityCompat.startActivity(MainActivity.this,irparavirarparceiro, activityOptionsCompat.toBundle());
            finish();
        });

        //  When click in this card user will to CartoesActivity
        card_See_Cards.setOnClickListener(v -> {
            Intent irparacartoes = new Intent(MainActivity.this,CartoesActivity.class);
            //irparacartoes.putExtra("emailuser",emaillogado);
            startActivity(irparacartoes);
            finish();
        });

        //  When click in this card user will to cardvercarrinhodecompra
        card_Shopping_Cart.setOnClickListener(v -> {
            Intent vercarrinhodecompra = new Intent(MainActivity.this,CarrinhoDeCompraActivity.class);
            //vercarrinhodecompra.putExtra("emailuser", emaillogado);
            startActivity(vercarrinhodecompra);
            finish();

        });

        //  When click here will show info_perfil
        icon_ProfileUser_principal.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetTheme);

            View sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_sheet,
                    findViewById(R.id.menu_sheet_principal));

            //  When click in this linear will to profile information
            sheetview.findViewById(R.id.btnperfil).setOnClickListener(v1 -> {
                Intent GoTo_profile = new Intent(MainActivity.this, ProfileActivity.class);
                GoTo_profile.putExtra("id_user", id_user);
                GoTo_profile.putExtra("email_user", email_user);
                GoTo_profile.putExtra("nm_user", nm_user);
                GoTo_profile.putExtra("cpf_user", cpf_user);
                GoTo_profile.putExtra("phone_user", phone_user);
                GoTo_profile.putExtra("address_user", address_user);
                GoTo_profile.putExtra("complement", complement);
                GoTo_profile.putExtra("img_user", img_user);
                GoTo_profile.putExtra("partner", partner);
                GoTo_profile.putExtra("partner_Startdate", partner_Startdate);
                startActivity(GoTo_profile);
                finish();
                bottomSheetDialog.dismiss();
            });

            //  When click in this linear will to app information
            sheetview.findViewById(R.id.btninfoapp).setOnClickListener(v1 -> {
                Toast.makeText(MainActivity.this, "Em desenvolvimento!!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

            //  When click in this linear will to configuration of app
            sheetview.findViewById(R.id.btnconfig).setOnClickListener(v1 -> {
                Toast.makeText(MainActivity.this, "Em desenvolvimento!!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

            //  When click in this linear will to LoginActivity
            sheetview.findViewById(R.id.btnlogout).setOnClickListener(v1 -> {
                AlertDialog.Builder warning_alert = new AlertDialog.Builder(MainActivity.this);
                warning_alert.setTitle("Deslogar");
                warning_alert.setMessage("Deseja realmente deslogar?");
                warning_alert.setPositiveButton("Sim", (dialog, which) -> {
                    mPrefs.edit().clear().apply();
                    Intent voltaraologin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(voltaraologin);
                    finish();
                });
                warning_alert.setNegativeButton("Não", null);
                warning_alert.show();
            });

            TextView name_user_menuSheet;
            name_user_menuSheet = sheetview.findViewById(R.id.name_user_menuSheet);
            String[]  UserName = nm_user.split(" ");
            String firstName = UserName[0];
            String secondName = UserName[1];
            name_user_menuSheet.setText(firstName + " " + secondName);

            bottomSheetDialog.setContentView(sheetview);
            bottomSheetDialog.show();
        });
    }

    private void loadProfileImage() {
        AsyncUserImage loadimage = new AsyncUserImage(img_user, icon_ProfileUser_principal);
        loadimage.execute();
    }

    private void loadPopularProducts() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerPopularProducts.setLayoutManager(layoutManager);

        AsyncPopularProducts asyncPopularProducts = new AsyncPopularProducts(recyclerPopularProducts, AnimationLoading_PopularProducts, MainActivity.this);
        asyncPopularProducts.execute();
    }

    //  Create method to check customer first things
    private void recebendodadosiniciaisdocliente() {
        String completeName_user = nm_user;
        String[]  Name_user = completeName_user.split(" ");
        String firstName_user = Name_user[0];

        //  Set first name of client
        txt_Name_user.setText(firstName_user);

        if (partner == 0){
            animationNotPartner();
        }else {
            cardview_notPartner.setVisibility(View.GONE);
        }

        if (address_user == null || address_user.equals("")){
            if (Show_warning_address.equals("desativado")){
                warning_address.dismiss();
            }else {
                mostraavisoend();
            }
        }else{
            warning_address.dismiss();
        }
    }

    //  Create Method for show alert of no adress register
    private void mostraavisoend(){
        ConstraintLayout btncadastraragoraend, btncadastrardepoisend;
        warning_address.setContentView(R.layout.aviso_sem_endereco_cadatrado);
        warning_address.setCancelable(true);
        btncadastraragoraend = warning_address.findViewById(R.id.btncadastraragoraend);
        btncadastrardepoisend = warning_address.findViewById(R.id.btncadastrardepoisend);

        btncadastraragoraend.setOnClickListener(v -> {
            Intent irpara_perfil = new Intent(MainActivity.this, RegisterAddresssActivity.class);
            irpara_perfil.putExtra("id_user", id_user);
            irpara_perfil.putExtra("email_user", email_user);
            irpara_perfil.putExtra("nm_user", nm_user);
            irpara_perfil.putExtra("cpf_user", cpf_user);
            irpara_perfil.putExtra("phone_user", phone_user);
            irpara_perfil.putExtra("address_user", address_user);
            irpara_perfil.putExtra("img_user", img_user);
            irpara_perfil.putExtra("partner", partner);
            irpara_perfil.putExtra("partner_Startdate", partner_Startdate);
            startActivity(irpara_perfil);
            finish();
        });

        btncadastrardepoisend.setOnClickListener(v -> warning_address.dismiss());

        warning_address.show();
    }

    //  Create Method for do animation in Not Partner Card
    private void animationNotPartner(){
        ConstraintLayout animationnotpartner, constraintdescnotpartner;
        animationnotpartner = findViewById(R.id.animationnotpartner);
        constraintdescnotpartner = findViewById(R.id.constraintdescnotpartner);

        cardview_notPartner.setVisibility(View.VISIBLE);
        animationnotpartner.setVisibility(View.VISIBLE);
        constraintdescnotpartner.setVisibility(View.GONE);
        timer.postDelayed(() -> {
            animationnotpartner.setVisibility(View.GONE);
            constraintdescnotpartner.setVisibility(View.VISIBLE);
        },2950);
    }

}