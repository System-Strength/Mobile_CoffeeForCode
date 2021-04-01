package co.ex.coffeeforcodeapp.Activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

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
    ImageView btn_scanner_qrcode;

    //  User information
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
        btn_scanner_qrcode = findViewById(R.id.btn_scanner_qrcode);

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

        btn_scanner_qrcode.setOnClickListener(v -> scanCode());

        SwipeRefreshProducts.setOnRefreshListener(() -> {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
            RecyclerProducts.setLayoutManager(layoutManager);
            AsyncProducts asyncProducts = new AsyncProducts(RecyclerProducts, AnimationProductsLoading, SwipeRefreshProducts, email_user, AllProductsActivity.this);
            asyncProducts.execute();
        });
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Escaneie o QR code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){
                String prodResult = result.getContents();
                String[]  categoryResult = prodResult.split(" ");
                String[] cd_prodReslt = prodResult.split(" ");
                try {
                    String category = categoryResult[0];
                    int cd_prod = Integer.parseInt(cd_prodReslt[1]);
                    Intent Goto_ProdDesc = new Intent(AllProductsActivity.this, ProductDetailsActivity.class);
                    Goto_ProdDesc.putExtra("cd_prod", cd_prod);
                    Goto_ProdDesc.putExtra("email_user", email_user);
                    Goto_ProdDesc.putExtra("nm_cat", category);
                    startActivity(Goto_ProdDesc);
                    Log.d("QrCodeStatus", "OK " + "\n QrCodeResult: " + prodResult);
                }
                catch (Exception ex){
                    Toast.makeText(this, R.string.error_to_readqrcode, Toast.LENGTH_LONG).show();
                    Log.d("QrCodeStatus", ex.toString() + "\n QrCodeResult: " + prodResult);
                }
            }else{
                Toast.makeText(this, R.string.noresult, Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void loadproducts(){
        MenuService menuService = retrofitProducs.create(MenuService.class);
        Call<ArrayList<DtoMenu>> menuCall = menuService.getAllProducts();
        menuCall.enqueue(new Callback<ArrayList<DtoMenu>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<DtoMenu>> call, @NotNull Response<ArrayList<DtoMenu>> response) {
                dtoMenus = new ArrayList<>();
                Toast.makeText(AllProductsActivity.this, "Deu certo", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(@NotNull Call<ArrayList<DtoMenu>> call, @NotNull Throwable t) {
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