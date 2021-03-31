package co.ex.coffeeforcodeapp.Activitys;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import co.ex.coffeeforcodeapp.Api.Card.CardService;
import co.ex.coffeeforcodeapp.Api.Card.DtoCard;
import co.ex.coffeeforcodeapp.Api.Mobile.DtoMobile;
import co.ex.coffeeforcodeapp.Api.Mobile.MobileService;
import co.ex.coffeeforcodeapp.Api.PopularProducts.AsyncPopularProducts;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.DtoShoppingCart;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.ShoppingCartService;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import co.ex.coffeeforcodeapp.BuildConfig;
import co.ex.coffeeforcodeapp.R;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MainActivity extends AppCompatActivity {
    //  User img
    CircleImageView icon_ProfileUser_principal;

    TextView txt_Name_user, txtQt_cards;
    CardView cardview_notPartner, card_Be_Partner, card_See_Cards, card_Shopping_Cart, AnimationLoading_PopularProducts,
            btnSee_AllProducts, btnSee_allorders;
    Handler timer = new Handler();
    Dialog warning_address, warning_update;
    RecyclerView recyclerPopularProducts;
    private BottomSheetDialog bottomSheetDialog;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    //  Mobile Information
    int versionCode = BuildConfig.VERSION_CODE;

    //  Shopping Cart
    ConstraintLayout baseQTProd_ShoopingCart;
    TextView txtQtProdMain;
    LottieAnimationView animationcart;

    // Shortcuts Cards
    CardView cardshopcafe, cardRefreshersMain, cardhamburguerandsandwiches, cardcandys ;

    int id_user, partner, cd_cat;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, Show_warning_address, partner_Startdate;

    String baseurl = "https://coffeeforcode.herokuapp.com/";
    final Retrofit retrofitShoppingCart = new Retrofit.Builder()
            .baseUrl( baseurl + "shoppingcart/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "card/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //  Firebase
    private FirebaseAnalytics mFirebaseAnalytics;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Finds_Ids();
        CheckMobileVersion();
        warning_address = new Dialog(this);
        warning_update = new Dialog(this);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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
            zipcode  = bundle.getString("zipcode");
            address_user  = bundle.getString("address_user");
            complement  = bundle.getString("complement");
            img_user  = bundle.getString("img_user");
            cpf_user  = bundle.getString("cpf_user");
            partner  = bundle.getInt("partner");
            partner_Startdate  = bundle.getString("partner_Startdate");
        }

        icon_ProfileUser_principal.setVisibility(View.VISIBLE);

        if (img_user == null || img_user.equals(" ") || img_user.equals("")){
            loadPopularProducts();
            GetCartSize();
            GetCards();
        }else {
            loadProfileImage();
            loadPopularProducts();
            GetCartSize();
            GetCards();
            Log.d("ProfileImageStatus", "loading image");
        }


        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //  Set somethings with gone and visible

        Get_UserInformation();

        cardshopcafe.setOnClickListener(v -> {
            cd_cat = 34;
            GoTo_AllProductsWithCDCAT(cd_cat);
        });

        cardRefreshersMain.setOnClickListener(v -> {
            cd_cat = 44;
            GoTo_AllProductsWithCDCAT(cd_cat);
        });

        cardhamburguerandsandwiches.setOnClickListener(v -> {
            cd_cat = 74;
            GoTo_AllProductsWithCDCAT(cd_cat);
        });

        cardcandys.setOnClickListener(v -> {
            cd_cat = 84;
            GoTo_AllProductsWithCDCAT(cd_cat);
        });


        //  When click in this card user will to SejaParceiroActivity
        card_Be_Partner.setOnClickListener(v -> {
            Intent GoTo_BePartner = new Intent(MainActivity.this, SejaParceiroActivity.class);
            GoTo_BePartner.putExtra("id_user", id_user);
            GoTo_BePartner.putExtra("email_user", email_user);
            GoTo_BePartner.putExtra("nm_user", nm_user);
            GoTo_BePartner.putExtra("cpf_user", cpf_user);
            GoTo_BePartner.putExtra("phone_user", phone_user);
            GoTo_BePartner.putExtra("zipcode", zipcode);
            GoTo_BePartner.putExtra("address_user", address_user);
            GoTo_BePartner.putExtra("complement", complement);
            GoTo_BePartner.putExtra("img_user", img_user);
            GoTo_BePartner.putExtra("partner", partner);
            GoTo_BePartner.putExtra("partner_Startdate", partner_Startdate);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerdarapido, R.anim.mover_direitarapido);
            ActivityCompat.startActivity(MainActivity.this,GoTo_BePartner, activityOptionsCompat.toBundle());
            finish();
        });

        btnSee_AllProducts.setOnClickListener(v -> {
            Intent GoTo_AllProducts = new Intent(MainActivity.this,AllProductsActivity.class);
            GoTo_AllProducts.putExtra("id_user", id_user);
            GoTo_AllProducts.putExtra("email_user", email_user);
            GoTo_AllProducts.putExtra("nm_user", nm_user);
            GoTo_AllProducts.putExtra("cpf_user", cpf_user);
            GoTo_AllProducts.putExtra("phone_user", phone_user);
            GoTo_AllProducts.putExtra("zipcode", zipcode);
            GoTo_AllProducts.putExtra("address_user", address_user);
            GoTo_AllProducts.putExtra("complement", complement);
            GoTo_AllProducts.putExtra("img_user", img_user);
            GoTo_AllProducts.putExtra("partner", partner);
            GoTo_AllProducts.putExtra("partner_Startdate", partner_Startdate);
            startActivity(GoTo_AllProducts);
            Bundle bundlefirebase = new Bundle();
            bundlefirebase.putString(FirebaseAnalytics.Param.SCREEN_NAME, "MainActivity");
            bundlefirebase.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "AllProductsActivity");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundlefirebase);
            finish();
        });

        //  When click in this card user will to CardActivity
        card_See_Cards.setOnClickListener(v -> {
            Intent goToCards = new Intent(MainActivity.this, CardsActivity.class);
            goToCards.putExtra("id_user", id_user);
            goToCards.putExtra("nm_user", nm_user);
            goToCards.putExtra("email_user", email_user);
            goToCards.putExtra("phone_user", phone_user);
            goToCards.putExtra("zipcode", zipcode);
            goToCards.putExtra("address_user", address_user);
            goToCards.putExtra("complement", complement);
            goToCards.putExtra("img_user", img_user);
            goToCards.putExtra("address_user", address_user);
            goToCards.putExtra("cpf_user", cpf_user);
            goToCards.putExtra("partner", partner);
            goToCards.putExtra("partner_Startdate", partner_Startdate);
            goToCards.putExtra("statusavisoend","desativado");
            startActivity(goToCards);
            finish();
        });

        //  When click in this card user will to cardvercarrinhodecompra
        card_Shopping_Cart.setOnClickListener(v -> {
            Intent goTo_cart = new Intent(MainActivity.this, ShoppingCartActivity.class);
            goTo_cart.putExtra("id_user", id_user);
            goTo_cart.putExtra("email_user", email_user);
            goTo_cart.putExtra("nm_user", nm_user);
            goTo_cart.putExtra("cpf_user", cpf_user);
            goTo_cart.putExtra("phone_user", phone_user);
            goTo_cart.putExtra("zipcode", zipcode);
            goTo_cart.putExtra("address_user", address_user);
            goTo_cart.putExtra("complement", complement);
            goTo_cart.putExtra("img_user", img_user);
            goTo_cart.putExtra("partner", partner);
            goTo_cart.putExtra("partner_Startdate", partner_Startdate);
            startActivity(goTo_cart);
            finish();
        });

        btnSee_allorders.setOnClickListener(v -> {
            Intent goTo_Orders = new Intent(MainActivity.this, MyOrdersActivity.class);
            goTo_Orders.putExtra("id_user", id_user);
            goTo_Orders.putExtra("nm_user", nm_user);
            goTo_Orders.putExtra("email_user", email_user);
            goTo_Orders.putExtra("phone_user", phone_user);
            goTo_Orders.putExtra("zipcode", zipcode);
            goTo_Orders.putExtra("address_user", address_user);
            goTo_Orders.putExtra("complement", complement);
            goTo_Orders.putExtra("img_user", img_user);
            goTo_Orders.putExtra("address_user", address_user);
            goTo_Orders.putExtra("cpf_user", cpf_user);
            goTo_Orders.putExtra("partner", partner);
            goTo_Orders.putExtra("partner_Startdate", partner_Startdate);
            goTo_Orders.putExtra("statusavisoend","desativado");
            startActivity(goTo_Orders);
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
                GoTo_profile.putExtra("zipcode", zipcode);
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

    public void CheckMobileVersion(){
        final Retrofit retrofitMobile = new Retrofit.Builder()
                .baseUrl( baseurl + "mobile/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MobileService mobileService = retrofitMobile.create(MobileService.class);
        Call<DtoMobile> mobileCall = mobileService.getMobileVersion();
        mobileCall.enqueue(new Callback<DtoMobile>() {
            @Override
            public void onResponse(@NotNull Call<DtoMobile> call, @NotNull Response<DtoMobile> response) {
                switch (response.code()){
                    case 401:
                        Toast.makeText(MainActivity.this, R.string.versionnotfound, Toast.LENGTH_SHORT).show();
                        break;
                    case 200:
                        assert response.body() != null;
                        int versionCodeGetApi = response.body().getVersionCode();
                        if (versionCodeGetApi <= versionCode){
                            Log.d("MobileVersion", "OK: " + versionCode);
                        }else{
                            ConstraintLayout btnUpdateNow, btnUpdateLater;
                            warning_update.setContentView(R.layout.adapter_x_appneedupdate);
                            warning_update.setCancelable(false);
                            btnUpdateNow = warning_update.findViewById(R.id.btnUpdateNow);
                            btnUpdateLater = warning_update.findViewById(R.id.btnUpdateLater);

                            btnUpdateNow.setOnClickListener(v -> {
                                String url = "https://play.google.com/store/apps/details?id=co.ex.coffeeforcodeapp";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                warning_update.dismiss();
                            });

                            btnUpdateLater.setOnClickListener(v -> warning_update.dismiss());
                            warning_update.show();
                            Log.d("MobileVersion", "Need update: " + versionCode);
                        }
                        break;
                    case 500:
                        Toast.makeText(MainActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoMobile> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetCards(){
        CardService cardService = retrofitCard.create(CardService.class);
        Call<DtoCard> cardCall = cardService.getCardsOfUser(email_user);
        cardCall.enqueue(new Callback<DtoCard>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DtoCard> call, @NotNull Response<DtoCard> response) {
                if (response.code() == 412){
                    txtQt_cards.setText("0");
                }else if(response.code() == 200){
                    assert response.body() != null;
                    txtQt_cards.setText(response.body().getLength() + "");
                }
            }

            @Override
            public void onFailure(@NotNull Call<DtoCard> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetCartSize() {
        ShoppingCartService shoppingCartService = retrofitShoppingCart.create(ShoppingCartService.class);
        Call<DtoShoppingCart> cartCall = shoppingCartService.getCartInfomration(email_user);
        cartCall.enqueue(new Callback<DtoShoppingCart>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DtoShoppingCart> call, @NotNull Response<DtoShoppingCart> response) {
                if (response.code() == 412){
                    animationcart.setVisibility(View.VISIBLE);
                    baseQTProd_ShoopingCart.setVisibility(View.GONE);
                }else if(response.code() == 200){
                    animationcart.setVisibility(View.GONE);
                    baseQTProd_ShoopingCart.setVisibility(View.VISIBLE);
                    assert response.body() != null;
                    txtQtProdMain.setText("Você tem " + response.body().getLength() + " produtos em seu carrinho");
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoShoppingCart> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.this, "Erro to find your cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GoTo_AllProductsWithCDCAT(int cd_cat) {
        Intent GoToAllProducts_With_CatCd = new Intent(MainActivity.this, AllProductsActivity.class);
        GoToAllProducts_With_CatCd.putExtra("cd_cat", cd_cat);
        GoToAllProducts_With_CatCd.putExtra("id_user", id_user);
        GoToAllProducts_With_CatCd.putExtra("email_user", email_user);
        GoToAllProducts_With_CatCd.putExtra("nm_user", nm_user);
        GoToAllProducts_With_CatCd.putExtra("cpf_user", cpf_user);
        GoToAllProducts_With_CatCd.putExtra("phone_user", phone_user);
        GoToAllProducts_With_CatCd.putExtra("zipcode", zipcode);
        GoToAllProducts_With_CatCd.putExtra("address_user", address_user);
        GoToAllProducts_With_CatCd.putExtra("complement", complement);
        GoToAllProducts_With_CatCd.putExtra("img_user", img_user);
        GoToAllProducts_With_CatCd.putExtra("partner", partner);
        GoToAllProducts_With_CatCd.putExtra("partner_Startdate", partner_Startdate);
        startActivity(GoToAllProducts_With_CatCd);
        finish();
    }

    private void Finds_Ids() {
        txt_Name_user = findViewById(R.id.txt_Name_user);
        recyclerPopularProducts = findViewById(R.id.recyclerPopularProducts);
        AnimationLoading_PopularProducts = findViewById(R.id.AnimationLoading_PopularProducts);
        cardview_notPartner = findViewById(R.id.cardview_notPartner);
        card_Be_Partner = findViewById(R.id.card_Be_Partner);
        card_See_Cards = findViewById(R.id.card_See_Cards);
        card_Shopping_Cart = findViewById(R.id.card_Shopping_Cart);
        btnSee_AllProducts = findViewById(R.id.btnSee_AllProducts);
        icon_ProfileUser_principal = findViewById(R.id.icon_ProfileUser_principal);
        txtQt_cards = findViewById(R.id.txtQt_cards);
        btnSee_allorders = findViewById(R.id.btnSee_allorders);

        //  Shopping Cart
        baseQTProd_ShoopingCart = findViewById(R.id.baseQTProd_ShoopingCart);
        txtQtProdMain = findViewById(R.id.txtQtProdMain);
        animationcart = findViewById(R.id.animationcart);

        //  Shortcuts Ids
        cardshopcafe = findViewById(R.id.cardshopcafe);
        cardRefreshersMain = findViewById(R.id.cardRefreshersMain);
        cardhamburguerandsandwiches = findViewById(R.id.cardhamburguerandsandwiches);
        cardcandys = findViewById(R.id.cardcandys);
    }

    private void loadProfileImage() {
        Picasso.get().load(img_user).into(icon_ProfileUser_principal);
        //AsyncUserImage loadimage = new AsyncUserImage(img_user, icon_ProfileUser_principal);
        //loadimage.execute();
    }

    private void loadPopularProducts() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerPopularProducts.setLayoutManager(layoutManager);

        AsyncPopularProducts asyncPopularProducts = new AsyncPopularProducts(recyclerPopularProducts, AnimationLoading_PopularProducts, email_user, MainActivity.this);
        asyncPopularProducts.execute();
    }

    //  Create method to check customer first things
    private void Get_UserInformation() {
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
                Show_AddressWarning();
            }
        }else{
            warning_address.dismiss();
        }
    }

    //  Create Method for show alert of no address register
    private void Show_AddressWarning(){
        ConstraintLayout btnRegisterAddressNow, btnRegisterAddressLater;
        warning_address.setContentView(R.layout.aviso_sem_endereco_cadatrado);
        warning_address.setCancelable(true);
        btnRegisterAddressNow = warning_address.findViewById(R.id.btnRegisterAddressNow);
        btnRegisterAddressLater = warning_address.findViewById(R.id.btnRegisterAddressLater);

        btnRegisterAddressNow.setOnClickListener(v -> {
            Intent irpara_perfil = new Intent(MainActivity.this, RegisterAddressActivity.class);
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

        btnRegisterAddressLater.setOnClickListener(v -> warning_address.dismiss());

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