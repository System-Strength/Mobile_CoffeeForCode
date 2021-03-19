package co.ex.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.Products.DtoMenuById;
import co.ex.coffeeforcodeapp.Api.Products.MenuService;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.DtoShoppingCart;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.ShoppingCartService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProductDetailsActivity extends AppCompatActivity {
    TextView nm_prod_Desc, txtSize_ProductDesc, txtCategory_Prod_Desc, price_prod_Desc, txtQt_prod, txtCartSize;
    ImageView ImgProd_Desc;
    CardView btnGoBackAllProducts;
    CardView infoCartSize;

    CardView BtnAddToCart;
    CardView btnLessQT_Prod, btnPlusQT_Prod;

    Dialog ShoppingCartAlert;

    //  User information
    int  cd_prod;
    int qt_prod = 1;
    String email_user, nm_cat;
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    final Retrofit retrofitShoppingCart = new Retrofit.Builder()
            .baseUrl( baseurl + "shoppingcart/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final Retrofit retrofitProduct = new Retrofit.Builder()
            .baseUrl( baseurl + "products/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    LoadingDialog loadingDialog = new LoadingDialog(ProductDetailsActivity.this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        nm_prod_Desc = findViewById(R.id.nm_prod_Desc);
        txtSize_ProductDesc = findViewById(R.id.txtSize_ProductDesc);
        txtCartSize = findViewById(R.id.txtCartSize);
        price_prod_Desc = findViewById(R.id.price_prod_Desc);
        ImgProd_Desc = findViewById(R.id.ImgProd_Desc);
        txtCategory_Prod_Desc = findViewById(R.id.txtCategory_Prod_Desc);
        txtQt_prod = findViewById(R.id.txtQt_prod);
        btnGoBackAllProducts = findViewById(R.id.btnGoBackAllProducts);
        btnLessQT_Prod = findViewById(R.id.btnLessQT_Prod);
        btnPlusQT_Prod = findViewById(R.id.btnPlusQT_Prod);
        BtnAddToCart = findViewById(R.id.BtnAddToCart);
        infoCartSize = findViewById(R.id.infoCartSize);
        ShoppingCartAlert = new Dialog(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email_user = bundle.getString("email_user");
        cd_prod = bundle.getInt("cd_prod");
        nm_cat = bundle.getString("nm_cat");
        if (cd_prod == 0){
            txtQt_prod.setText(qt_prod +"" );
            Toast.makeText(this, "Error for select product\nTry Again\nErro em selecionar produto\nTente novamente", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            txtQt_prod.setText(qt_prod +"" );
            ImgProd_Desc.setVisibility(View.VISIBLE);
            getProductDescription();
            GetCartSize();
        }

        btnLessQT_Prod.setOnClickListener(v -> {
            if (qt_prod == 1){
                Toast.makeText(this, "1 is the minimum quantity\n1 é a quantidade mínima", Toast.LENGTH_SHORT).show();
            }else{
                qt_prod--;
                RefreshQtText();
            }
        });

        btnPlusQT_Prod.setOnClickListener(v -> {
            if (qt_prod == 20){
                Toast.makeText(this, "The maximum quantity to buy is 20\nA quantidade maxima por comprar é 20", Toast.LENGTH_SHORT).show();
            }else {
                qt_prod++;
                RefreshQtText();
            }
        });

        BtnAddToCart.setOnClickListener(v -> {
            BtnAddToCart.setEnabled(false);
            btnLessQT_Prod.setEnabled(false);
            btnPlusQT_Prod.setEnabled(false);
            ShoppingCartService shoppingCartService = retrofitShoppingCart.create(ShoppingCartService.class);
            Call<DtoShoppingCart> cartCall = shoppingCartService.insertItem(email_user, cd_prod, qt_prod);

            cartCall.enqueue(new Callback<DtoShoppingCart>() {
                @Override
                public void onResponse(Call<DtoShoppingCart> call, Response<DtoShoppingCart> response) {
                    if (response.code() == 201){
                        ShowShoppingCartAlert();
                        //Toast.makeText(ProductDetailsActivity.this, "Successfully inserted", Toast.LENGTH_SHORT).show();
                    }else if (response.code() == 409){
                        Toast.makeText(ProductDetailsActivity.this, "You already have this product in your cart", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ProductDetailsActivity.this, "No possible to insert this item right now", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DtoShoppingCart> call, Throwable t) {
                    Toast.makeText(ProductDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnGoBackAllProducts.setOnClickListener(v -> finish());
    }

    private void ShowShoppingCartAlert() {
        ConstraintLayout cardBtnSeeCart, cardBtnGoBackMenu;
        ShoppingCartAlert.setContentView(R.layout.adapter_shoppingcart);
        ShoppingCartAlert.setCancelable(false);

        cardBtnSeeCart = ShoppingCartAlert.findViewById(R.id.cardBtnSeeCart);
        cardBtnGoBackMenu = ShoppingCartAlert.findViewById(R.id.cardBtnGoBackMenu);

        cardBtnSeeCart.setOnClickListener(v -> {
            Toast.makeText(this, "Under development", Toast.LENGTH_SHORT).show();
        });

        cardBtnGoBackMenu.setOnClickListener(v -> finish());

        ShoppingCartAlert.show();
    }

    private void GetCartSize() {
        infoCartSize.setVisibility(View.GONE);
        ShoppingCartService shoppingCartService = retrofitShoppingCart.create(ShoppingCartService.class);
        Call<DtoShoppingCart> cartCall = shoppingCartService.getCartInfomration(email_user);
        cartCall.enqueue(new Callback<DtoShoppingCart>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<DtoShoppingCart> call, Response<DtoShoppingCart> response) {
                if (response.code() == 412){
                    infoCartSize.setVisibility(View.GONE);
                    txtCartSize.setText("");
                }else if(response.code() == 200){
                    txtCartSize.setText(response.body().getLength() + "");
                    infoCartSize.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<DtoShoppingCart> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Erro to find your cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void RefreshQtText() {
        txtQt_prod.setText(qt_prod + "");
    }

    private void getProductDescription() {
        loadingDialog.startLoading();
        MenuService menuService = retrofitProduct.create(MenuService.class);
        Call<DtoMenuById> menuCall = menuService.getProductByCd(cd_prod);

        menuCall.enqueue(new Callback<DtoMenuById>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<DtoMenuById> call, Response<DtoMenuById> response) {
                if (response.code() == 200){
                    String  a = response.body().toString();
                    nm_prod_Desc.setText(response.body().getNm_prod());
                    txtSize_ProductDesc.setText(response.body().getSize());
                    txtCategory_Prod_Desc.setText(nm_cat);
                    price_prod_Desc.setText("R$ "+ response.body().getPrice_prod());
                    String ImageUrl = response.body().getImg_prod();
                    Picasso.get().load(ImageUrl).into(ImgProd_Desc, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            ImgProd_Desc.setVisibility(View.VISIBLE);
                            loadingDialog.dimissDialog();
                        }

                        @Override
                        public void onError(Exception e) {
                            loadingDialog.dimissDialog();
                            Toast.makeText(ProductDetailsActivity.this, "Error to get image\nErro em receber a imagem", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(response.code() == 404){
                    Toast.makeText(ProductDetailsActivity.this, "Products not found", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProductDetailsActivity.this, "Server timeout try later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DtoMenuById> call, Throwable t) {
                loadingDialog.dimissDialog();
                Toast.makeText(ProductDetailsActivity.this, "Server error :" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}