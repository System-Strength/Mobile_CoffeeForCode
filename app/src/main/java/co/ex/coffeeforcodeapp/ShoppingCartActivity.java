package co.ex.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import co.ex.coffeeforcodeapp.Api.ShoppingCart.AsyncShoppingCart;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.DtoShoppingCart;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.ShoppingCartService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ShoppingCartActivity extends AppCompatActivity{
    RecyclerView recycler_shoppingcart;
    TextView txt_total;
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    CardView infoCartSize_shoppingcart;
    TextView txtCartSize_shoppingcart;

    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;

    //  Retrofit's
    final Retrofit retrofitShoppingCart = new Retrofit.Builder()
            .baseUrl( baseurl + "shoppingcart/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);
        recycler_shoppingcart = findViewById(R.id.recycler_shoppingcart);
        txt_total = findViewById(R.id.txt_total);
        infoCartSize_shoppingcart = findViewById(R.id.infoCartSize_shoppingcart);
        txtCartSize_shoppingcart = findViewById(R.id.txtCartSize_shoppingcart);

        // get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email_user = bundle.getString("email_user");

        loadCart();
        GetCartSize();
    }

    private void loadCart() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShoppingCartActivity.this);
        recycler_shoppingcart.setLayoutManager(layoutManager);
        AsyncShoppingCart asyncShoppingCart = new AsyncShoppingCart(recycler_shoppingcart, email_user, txt_total, ShoppingCartActivity.this);
        asyncShoppingCart.execute();
    }

    private void GetCartSize() {
        infoCartSize_shoppingcart.setVisibility(View.GONE);
        ShoppingCartService shoppingCartService = retrofitShoppingCart.create(ShoppingCartService.class);
        Call<DtoShoppingCart> cartCall = shoppingCartService.getCartInfomration(email_user);
        cartCall.enqueue(new Callback<DtoShoppingCart>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DtoShoppingCart> call, @NotNull Response<DtoShoppingCart> response) {
                if (response.code() == 412){
                    infoCartSize_shoppingcart.setVisibility(View.GONE);
                    txtCartSize_shoppingcart.setText("");
                }else if(response.code() == 200){
                    assert response.body() != null;
                    txtCartSize_shoppingcart.setText(response.body().getLength() + "");
                    infoCartSize_shoppingcart.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoShoppingCart> call, @NotNull Throwable t) {
                Toast.makeText(ShoppingCartActivity.this, R.string.error_to_find_your_cart, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}