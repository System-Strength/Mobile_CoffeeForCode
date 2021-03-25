package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

import co.ex.coffeeforcodeapp.Api.Category.AsyncCategory;
import co.ex.coffeeforcodeapp.Api.Products.AsyncProdCategory;
import co.ex.coffeeforcodeapp.Api.Products.AsyncProducts;
import co.ex.coffeeforcodeapp.Api.Products.DtoMenu;
import co.ex.coffeeforcodeapp.Api.Products.MenuService;
import co.ex.coffeeforcodeapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AllProductsActivity extends AppCompatActivity {
    RecyclerView RecyclerCategory, RecyclerProducts;
    LottieAnimationView AnimationcategoryLoading, AnimationProductsLoading;
    SwipeRefreshLayout SwipeRefreshProducts;
    int id_user, partner, cd_cat;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;

    final Retrofit retrofitProducs = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ArrayList<DtoMenu> dtoMenus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_all_products);
        RecyclerCategory = findViewById(R.id.RecyclerCategory);
        RecyclerProducts = findViewById(R.id.RecyclerProducts);
        SwipeRefreshProducts = findViewById(R.id.SwipeRefreshProducts);
        AnimationcategoryLoading = findViewById(R.id.AnimationcategoryLoading);
        AnimationProductsLoading = findViewById(R.id.AnimationProductsLoading);


        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        cd_cat = bundle.getInt("cd_cat");
        if (cd_cat == 0){
            GetUserInformation(bundle);
            loadProducts();
            loadCategorys();
            //loadproducts();
        }else {
            GetUserInformation(bundle);
            AsyncProdCategory asyncProdCategory = new AsyncProdCategory(RecyclerProducts, AnimationProductsLoading, email_user, SwipeRefreshProducts, cd_cat, AllProductsActivity.this);
            asyncProdCategory.execute();
            loadCategorys();
        }

        SwipeRefreshProducts.setOnRefreshListener(() -> {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
            RecyclerProducts.setLayoutManager(layoutManager);
            AsyncProducts asyncProducts = new AsyncProducts(RecyclerProducts, AnimationProductsLoading, SwipeRefreshProducts, email_user, AllProductsActivity.this);
            asyncProducts.execute();
        });
    }

    public void loadproducts(){
        MenuService menuService = retrofitProducs.create(MenuService.class);
        Call<ArrayList<DtoMenu>> menuCall = menuService.getAllProducts();
        menuCall.enqueue(new Callback<ArrayList<DtoMenu>>() {
            @Override
            public void onResponse(Call<ArrayList<DtoMenu>> call, Response<ArrayList<DtoMenu>> response) {
                dtoMenus = new ArrayList<>();
                Toast.makeText(AllProductsActivity.this, "Deu certo", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<DtoMenu>> call, Throwable t) {
                Toast.makeText(AllProductsActivity.this, "Não deu " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ErrorArrayProd", t.getMessage());
            }
        });
    }

    private void GetUserInformation(Bundle bundle) {
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
    }

    private void loadProducts() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2,StaggeredGridLayoutManager.VERTICAL);
        RecyclerProducts.setLayoutManager(layoutManager);

        AsyncProducts asyncProducts = new AsyncProducts(RecyclerProducts, AnimationProductsLoading, SwipeRefreshProducts, email_user, AllProductsActivity.this);
        asyncProducts.execute();

    }

    private void loadCategorys() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        RecyclerCategory.setLayoutManager(layoutManager);

        AsyncCategory asyncCategory = new AsyncCategory(RecyclerCategory, AnimationcategoryLoading, AllProductsActivity.this, RecyclerProducts, AnimationProductsLoading, SwipeRefreshProducts, email_user);
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
        GoTo_Main.putExtra("zipcode", zipcode);
        GoTo_Main.putExtra("address_user", address_user);
        GoTo_Main.putExtra("complement", complement);
        GoTo_Main.putExtra("img_user", img_user);
        GoTo_Main.putExtra("partner", partner);
        GoTo_Main.putExtra("partner_Startdate", partner_Startdate);
        GoTo_Main.putExtra("statusavisoend", "desativado");
        startActivity(GoTo_Main);
        finish();

    }
}