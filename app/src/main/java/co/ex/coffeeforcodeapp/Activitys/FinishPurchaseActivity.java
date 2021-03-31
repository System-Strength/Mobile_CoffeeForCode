package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.Card.AsyncCardsPurchase;
import co.ex.coffeeforcodeapp.Api.Card.CardService;
import co.ex.coffeeforcodeapp.Api.Card.DtoCard;
import co.ex.coffeeforcodeapp.Api.Orders.DtoOrders;
import co.ex.coffeeforcodeapp.Api.Orders.OrdersServices;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.DtoShoppingCart;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.ShoppingCartService;
import co.ex.coffeeforcodeapp.Api.User.DtoUsers;
import co.ex.coffeeforcodeapp.Api.User.UsersService;
import co.ex.coffeeforcodeapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FinishPurchaseActivity extends AppCompatActivity {
    TextView txtAddress_purchase, txtTotal_purchase, txtCheckout_purchase;
    TextView txtCartSize_purchase;
    CardView infoCartSize_purchase, btnGoBackPurchase, btnCheckout_purchase, btnregistercart_purchase;
    ConstraintLayout btnSeeCart_purchase;
    RecyclerView recycler_cards_pruchase;
    CardView card_noCard_purchase;
    String status;
    String txttranslate;
    String PayFormat_user = "Cartão de Credito";
    Handler timer = new Handler();
    int Cd_orderAfter;

    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;
    LoadingDialog loadingDialog = new LoadingDialog(FinishPurchaseActivity.this);

    //  Retrofit's
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    final Retrofit retrofitShoppingCart = new Retrofit.Builder()
            .baseUrl( baseurl + "shoppingcart/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/info/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "card/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final Retrofit retrofitOrder = new Retrofit.Builder()
            .baseUrl( baseurl + "orders/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_finish_purchase);
        txtAddress_purchase = findViewById(R.id.txtAddress_purchase);
        txtCartSize_purchase = findViewById(R.id.txtCartSize_purchase);
        infoCartSize_purchase = findViewById(R.id.infoCartSize_purchase);
        btnGoBackPurchase = findViewById(R.id.btnGoBackPurchase);
        txtTotal_purchase = findViewById(R.id.txtTotal_purchase);
        txtCheckout_purchase = findViewById(R.id.txtCheckout_purchase);
        btnCheckout_purchase = findViewById(R.id.btnCheckout_purchase);
        btnSeeCart_purchase = findViewById(R.id.btnSeeCart_purchase);
        btnregistercart_purchase = findViewById(R.id.btnregistercart_purchase);
        recycler_cards_pruchase = findViewById(R.id.recycler_cards_pruchase);
        card_noCard_purchase = findViewById(R.id.card_noCard_purchase);
        btnCheckout_purchase.setElevation(20);

        // get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email_user = bundle.getString("email_user");
        txtTotal_purchase.setText(bundle.getString("price"));

        String txtTotalBase = bundle.getString("price") + " ";
        String[]  totalBase = txtTotalBase.split(" ");
        String valueTotal = totalBase[2];

        //  Set txt on checkout button
        txttranslate = txtCheckout_purchase.getText().toString();
        if (txttranslate.equals("CHECKOUT")){
            txtCheckout_purchase.setText("CHECKOUT " + "(R$" + valueTotal + ")");

        }else{
            txtCheckout_purchase.setText("COMPRAR " + "(R$" + valueTotal + ")");
        }

        loadUserInformation();
        GetCards();
        GetCartSize();

        btnregistercart_purchase.setOnClickListener(v -> {
            Intent goto_registercard = new Intent(FinishPurchaseActivity.this, RegisterCardActivity.class);
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
        });

        btnCheckout_purchase.setOnClickListener(v -> {
            btnCheckout_purchase.setElevation(0);
            loadingDialog.startLoading();
            status = "Pendente";
            OrdersServices ordersServices = retrofitOrder.create(OrdersServices.class);
            Call<DtoOrders> ordersCall = ordersServices.postOrder(email_user, zipcode, address_user, complement, PayFormat_user, status);
            ordersCall.enqueue(new Callback<DtoOrders>() {
                @Override
                public void onResponse(@NotNull Call<DtoOrders> call, @NotNull Response<DtoOrders> response) {
                    if (response.code() == 201){
                        Cd_orderAfter = response.body().getCd_order();
                        StatusUpdateState1();
                        loadingDialog.dimissDialog();
                        Toast.makeText(FinishPurchaseActivity.this, R.string.order_successful, Toast.LENGTH_SHORT).show();
                        Intent goTo_Orders = new Intent(FinishPurchaseActivity.this, MyOrdersActivity.class);
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
                    }else if(response.code() == 409){
                        loadingDialog.dimissDialog();
                        Toast.makeText(FinishPurchaseActivity.this, R.string.sorry_no_stock, Toast.LENGTH_SHORT).show();
                    }else{
                        loadingDialog.dimissDialog();
                        Toast.makeText(FinishPurchaseActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                        goTo_main();
                        Log.d("OrderPostStstus", response.body().toString());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DtoOrders> call, @NotNull Throwable t) {
                    loadingDialog.dimissDialog();
                    Toast.makeText(FinishPurchaseActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                    goTo_main();
                    Log.d("OrderPostStstus", t.getMessage());
                }
            });
        });

        //  When click go to ShoppingCart
        btnSeeCart_purchase.setOnClickListener(v -> {
            Intent goTo_cart = new Intent(FinishPurchaseActivity.this, ShoppingCartActivity.class);
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

        //  When click will return to main activity
        btnGoBackPurchase.setOnClickListener(v -> goTo_main());
    }

    private void GetCards(){
        CardService cardService = retrofitCard.create(CardService.class);
        Call<DtoCard> cardCall = cardService.getCardsOfUser(email_user);
        cardCall.enqueue(new Callback<DtoCard>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DtoCard> call, @NotNull Response<DtoCard> response) {
                if (response.code() == 412){
                    card_noCard_purchase.setVisibility(View.VISIBLE);
                    recycler_cards_pruchase.setVisibility(View.GONE);
                }else if(response.code() == 200){
                    card_noCard_purchase.setVisibility(View.GONE);
                    recycler_cards_pruchase.setVisibility(View.VISIBLE);
                    LinearLayoutManager linearLayout = new LinearLayoutManager(FinishPurchaseActivity.this);
                    recycler_cards_pruchase.setLayoutManager(linearLayout);
                    AsyncCardsPurchase asyncCards = new AsyncCardsPurchase(recycler_cards_pruchase, card_noCard_purchase, FinishPurchaseActivity.this, email_user);
                    asyncCards.execute();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DtoCard> call, @NotNull Throwable t) {
                Toast.makeText(FinishPurchaseActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadUserInformation(){
        loadingDialog.startLoading();
        UsersService usersService = retrofitUser.create(UsersService.class);
        Call<DtoUsers> call = usersService.infoUser(email_user);
        call.enqueue(new Callback<DtoUsers>() {
            @Override
            public void onResponse(@NotNull Call<DtoUsers> call, @NotNull Response<DtoUsers> response) {
                if (response.code() == 200){
                    loadingDialog.dimissDialog();
                    id_user = response.body().getId_user();
                    nm_user = response.body().getNm_user();
                    phone_user = response.body().getPhone_user();
                    cpf_user = response.body().getCpf_user();
                    partner = response.body().getPartner();
                    partner_Startdate = response.body().getPartner_Startdate();
                    address_user = response.body().getAddress_user();
                    complement = response.body().getComplement();
                    zipcode = response.body().getZipcode();
                    img_user = response.body().getImg_user();
                    if (address_user == null || address_user.equals(" ") || address_user.length() < 3){
                        AlertDialog.Builder alertAddress = new AlertDialog.Builder(FinishPurchaseActivity.this)
                                .setTitle(R.string.addres_no_registred)
                                .setCancelable(false)
                                .setMessage(R.string.desc_purchase_no_address)
                                .setPositiveButton(R.string.singup, (dialog, which) -> {
                                    Intent irpara_perfil = new Intent(FinishPurchaseActivity.this, RegisterAddressActivity.class);
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
                                })
                                .setNeutralButton(R.string.register_later, (dialog, which) -> goTo_main());
                        alertAddress.show();

                    }else{
                        String fullAddress = address_user + ", " + complement + ", " + zipcode;
                        txtAddress_purchase.setText(fullAddress);
                    }
                }else{
                    loadingDialog.dimissDialog();
                    Toast.makeText(FinishPurchaseActivity.this, "Error" + response.body() + "\n" + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<DtoUsers> call, @NotNull Throwable t) {
                loadingDialog.dimissDialog();
                Toast.makeText(FinishPurchaseActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void StatusUpdateState1(){
        timer.postDelayed(() -> {
            OrdersServices ordersServices = retrofitOrder.create(OrdersServices.class);
            status = "Em preparo";
            Call<DtoOrders> ordersCall = ordersServices.updateStatus(Cd_orderAfter, status);
            ordersCall.enqueue(new Callback<DtoOrders>() {
                @Override
                public void onResponse(@NotNull Call<DtoOrders> call, @NotNull Response<DtoOrders> response) {
                    Log.d("OrderStatus", "Updated");
                }
                @Override
                public void onFailure(@NotNull Call<DtoOrders> call, @NotNull Throwable t) {
                    Log.d("OrderStatus", "Not update: " + t.getMessage());

                }
            });
        },300000);
    }

    private void goTo_main() {
        Intent GoBack_ToMain = new Intent(FinishPurchaseActivity.this, MainActivity.class);
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

    private void GetCartSize() {
        infoCartSize_purchase.setVisibility(View.GONE);
        ShoppingCartService shoppingCartService = retrofitShoppingCart.create(ShoppingCartService.class);
        Call<DtoShoppingCart> cartCall = shoppingCartService.getCartInfomration(email_user);
        cartCall.enqueue(new Callback<DtoShoppingCart>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DtoShoppingCart> call, @NotNull Response<DtoShoppingCart> response) {
                if (response.code() == 412){
                    infoCartSize_purchase.setVisibility(View.GONE);
                    txtCartSize_purchase.setText("");
                }else if(response.code() == 200){
                    assert response.body() != null;
                    txtCartSize_purchase.setText(response.body().getLength() + "");
                    infoCartSize_purchase.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoShoppingCart> call, @NotNull Throwable t) {
                Toast.makeText(FinishPurchaseActivity.this, R.string.error_to_find_your_cart, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goTo_main();
    }
}