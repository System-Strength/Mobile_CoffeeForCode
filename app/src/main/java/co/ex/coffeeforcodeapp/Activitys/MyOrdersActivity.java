package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.Orders.AsyncOrders;
import co.ex.coffeeforcodeapp.Api.Orders.DtoOrders;
import co.ex.coffeeforcodeapp.Api.Orders.OrdersServices;
import co.ex.coffeeforcodeapp.R;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyOrdersActivity extends AppCompatActivity {
    CircleImageView icon_ProfileUser_myorders;
    RecyclerView recyclerOrders;
    CardView btnGoBackMain_myorders, cardNoOrder;
    SwipeRefreshLayout swipeRefreshOrders;
    LoadingDialog loadingDialog = new LoadingDialog(MyOrdersActivity.this);


    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;


    //  Retrofit's
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    final Retrofit retrofitOrder = new Retrofit.Builder()
            .baseUrl( baseurl + "orders/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        icon_ProfileUser_myorders = findViewById(R.id.icon_ProfileUser_myorders);
        recyclerOrders = findViewById(R.id.recyclerOrders);
        swipeRefreshOrders = findViewById(R.id.swipeRefreshOrders);
        btnGoBackMain_myorders = findViewById(R.id.btnGoBackMain_myorders);
        cardNoOrder = findViewById(R.id.cardNoOrder);
        btnGoBackMain_myorders.setElevation(20);

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
            Log.d("ProfileImageStatus", "No Image found");
            getOrders();
        }else{
            Picasso.get().load(img_user).into(icon_ProfileUser_myorders);
            getOrders();
            Log.d("ProfileImageStatus", "loading image");
        }

        icon_ProfileUser_myorders.setOnClickListener(v -> {
            Intent GoTo_profile = new Intent(MyOrdersActivity.this, ProfileActivity.class);
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

        btnGoBackMain_myorders.setOnClickListener(v -> goTo_main());

        swipeRefreshOrders.setOnRefreshListener(this::getOrders);

    }

    private void getOrders() {
        loadingDialog.startLoading();
        OrdersServices ordersServices = retrofitOrder.create(OrdersServices.class);
        Call<DtoOrders> ordersCall = ordersServices.getOrdersbyUser(email_user);
        ordersCall.enqueue(new Callback<DtoOrders>() {
            @Override
            public void onResponse(@NotNull Call<DtoOrders> call, @NotNull Response<DtoOrders> response) {
                if (response.code() == 200){
                    if (response.body().getLength() <= 0){
                        loadingDialog.dimissDialog();
                        cardNoOrder.setVisibility(View.VISIBLE);
                        swipeRefreshOrders.setVisibility(View.GONE);
                    }else{
                        loadingDialog.dimissDialog();
                        cardNoOrder.setVisibility(View.GONE);
                        swipeRefreshOrders.setVisibility(View.VISIBLE);
                        loadOrders();
                    }
                }else if (response.code() == 410){
                    loadingDialog.dimissDialog();
                    cardNoOrder.setVisibility(View.VISIBLE);
                    swipeRefreshOrders.setVisibility(View.GONE);
                }else{
                    loadingDialog.dimissDialog();
                    Toast.makeText(MyOrdersActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DtoOrders> call, @NotNull Throwable t) {
                loadingDialog.dimissDialog();
                Toast.makeText(MyOrdersActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrders() {
        LinearLayoutManager linearLayout = new LinearLayoutManager(MyOrdersActivity.this);
        recyclerOrders.setLayoutManager(linearLayout);
        AsyncOrders asyncOrders = new AsyncOrders(recyclerOrders, MyOrdersActivity.this, swipeRefreshOrders, email_user);
        asyncOrders.execute();
    }

    private void goTo_main() {
        btnGoBackMain_myorders.setElevation(0);
        Intent GoBack_ToMain = new Intent(MyOrdersActivity.this, MainActivity.class);
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
        GoBack_ToMain.putExtra("statusavisoend", "desativado");
        startActivity(GoBack_ToMain);
        finish();
    }

    @Override
    public void onBackPressed() {
        goTo_main();
    }

}