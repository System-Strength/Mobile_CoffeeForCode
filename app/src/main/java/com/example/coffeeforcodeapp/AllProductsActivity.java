package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.Api.Category.AsyncCategory;
import com.example.coffeeforcodeapp.Api.Products.AsyncProducts;

public class AllProductsActivity extends AppCompatActivity {
    RecyclerView RecyclerCategory, RecyclerProducts;
    LottieAnimationView AnimationcategoryLoading, AnimationProductsLoading;
    int id_user, partner;
    String nm_user, email_user, phone_user, address_user, complement, img_user, cpf_user, partner_Startdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        RecyclerCategory = findViewById(R.id.RecyclerCategory);
        RecyclerProducts = findViewById(R.id.RecyclerProducts);
        AnimationcategoryLoading = findViewById(R.id.AnimationcategoryLoading);
        AnimationProductsLoading = findViewById(R.id.AnimationProductsLoading);

        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_user = bundle.getInt("id_user");
        email_user = bundle.getString("email_user");
        nm_user = bundle.getString("nm_user");
        cpf_user = bundle.getString("cpf_user");
        phone_user = bundle.getString("phone_user");
        address_user = bundle.getString("address_user");
        complement = bundle.getString("complement");
        img_user = bundle.getString("img_user");
        partner = bundle.getInt("partner");
        partner_Startdate = bundle.getString("partner_Startdate");

        loadProducts();
        loadCategorys();
    }

    private void loadProducts() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AllProductsActivity.this);
        RecyclerProducts.setLayoutManager(layoutManager);

        AsyncProducts asyncProducts = new AsyncProducts(RecyclerProducts, AnimationProductsLoading, AllProductsActivity.this);
        asyncProducts.execute();

    }

    private void loadCategorys() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        RecyclerCategory.setLayoutManager(layoutManager);

        AsyncCategory asyncCategory = new AsyncCategory(RecyclerCategory, AnimationcategoryLoading, AllProductsActivity.this);
        asyncCategory.execute();
    }

    @Override
    public void onBackPressed() {
        Intent GoTo_Main = new Intent(AllProductsActivity.this, MainActivity.class);
        GoTo_Main.putExtra("id_user", id_user);
        GoTo_Main.putExtra("email_user", email_user);
        GoTo_Main.putExtra("nm_user", nm_user);
        GoTo_Main.putExtra("cpf_user", cpf_user);
        GoTo_Main.putExtra("phone_user", phone_user);
        GoTo_Main.putExtra("address_user", address_user);
        GoTo_Main.putExtra("complement", complement);
        GoTo_Main.putExtra("img_user", img_user);
        GoTo_Main.putExtra("partner", partner);
        GoTo_Main.putExtra("partner_Startdate", partner_Startdate);
        startActivity(GoTo_Main);
        finish();

    }
}