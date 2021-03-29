package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.Products.DtoMenuById;
import co.ex.coffeeforcodeapp.Api.Products.MenuService;
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

public class ProductDetailsActivity extends AppCompatActivity {
    TextView nm_prod_Desc, txtSize_ProductDesc, txtCategory_Prod_Desc, price_prod_Desc, txtQt_prod, txtCartSize;
    ImageView ImgProd_Desc;
    CardView btnGoBackAllProducts;
    CardView infoCartSize;

    CardView BtnAddToCart;
    CardView btnLessQT_Prod, btnPlusQT_Prod;

    Dialog ShoppingCartAlert;

    //  User information
    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;
    int  cd_prod;
    int qt_prod = 1;
    int qt_prodGet;
    String nm_cat;
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    String ImageUrl;
    String nm_prod;

    //  Product Information
    float unit_prod_price;
    float full_prod_price = unit_prod_price;

    //  Retrofit's
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
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/info/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    LottieAnimationView animation_add_to_cart;
    TextView txt_add_to_cart;
    LoadingDialog loadingDialog = new LoadingDialog(ProductDetailsActivity.this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_product_details);
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
        animation_add_to_cart = findViewById(R.id.animation_add_to_cart);
        txt_add_to_cart = findViewById(R.id.txt_add_to_cart);
        ShoppingCartAlert = new Dialog(this);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email_user = bundle.getString("email_user");
        cd_prod = bundle.getInt("cd_prod");
        nm_cat = bundle.getString("nm_cat");
        if (cd_prod == 0){
            txtQt_prod.setText(qt_prod +"" );
            Toast.makeText(this, R.string.error_for_select_product, Toast.LENGTH_SHORT).show();
            finish();
        }else{
            txtQt_prod.setText(qt_prod +"" );
            ImgProd_Desc.setVisibility(View.VISIBLE);
            loadUserInformation();
            getProductDescription();
            GetCartSize();
        }

        btnLessQT_Prod.setOnClickListener(v -> {
            if (qt_prod == 1){
                Toast.makeText(this, R.string.one_is_the_minumum_quantity, Toast.LENGTH_SHORT).show();
            }else{
                qt_prod--;
                setNewPrice(numberFormat);
                RefreshQtText();
            }
        });

        btnPlusQT_Prod.setOnClickListener(v -> {
            if (qt_prod == qt_prodGet || qt_prod == 20){
                Toast.makeText(this, R.string.maximum_amount_reached, Toast.LENGTH_SHORT).show();
            }else {
                qt_prod++;
                setNewPrice(numberFormat);
                RefreshQtText();
            }
        });

        BtnAddToCart.setOnClickListener(v -> {
            BtnAddToCart.setEnabled(false);
            btnLessQT_Prod.setEnabled(false);
            btnPlusQT_Prod.setEnabled(false);
            animation_add_to_cart.setVisibility(View.VISIBLE);
            animation_add_to_cart.playAnimation();
            txt_add_to_cart.setVisibility(View.GONE);
            setNewPrice(numberFormat);
            ShoppingCartService shoppingCartService = retrofitShoppingCart.create(ShoppingCartService.class);
            Call<DtoShoppingCart> cartCall = shoppingCartService.insertItem(id_user, email_user, cd_prod, nm_prod, ImageUrl, qt_prod, unit_prod_price, full_prod_price);

            cartCall.enqueue(new Callback<DtoShoppingCart>() {
                @Override
                public void onResponse(@NotNull Call<DtoShoppingCart> call, @NotNull Response<DtoShoppingCart> response) {
                    if (response.code() == 201){
                        ShowShoppingCartAlert();
                        pauseLoadingAnimation();
                    }else if (response.code() == 409){
                        pauseLoadingAnimation();
                        Toast.makeText(ProductDetailsActivity.this, R.string.you_already_have_this_products_in_your_cart, Toast.LENGTH_SHORT).show();
                    }else{
                        pauseLoadingAnimation();
                        Toast.makeText(ProductDetailsActivity.this, R.string.no_possible_to_insert_this, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DtoShoppingCart> call, @NotNull Throwable t) {
                    pauseLoadingAnimation();
                    Toast.makeText(ProductDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnGoBackAllProducts.setOnClickListener(v -> finish());
    }

    @SuppressLint("SetTextI18n")
    private void setNewPrice(NumberFormat numberFormat) {
        full_prod_price = unit_prod_price * qt_prod;
        price_prod_Desc.setText("R$ " + numberFormat.format(full_prod_price));
    }

    public void loadUserInformation(){
        UsersService usersService = retrofitUser.create(UsersService.class);
        Call<DtoUsers> call = usersService.infoUser(email_user);
        call.enqueue(new Callback<DtoUsers>() {
            @Override
            public void onResponse(@NotNull Call<DtoUsers> call, @NotNull Response<DtoUsers> response) {
                if (response.code() == 200) {
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
                }

            }

            @Override
            public void onFailure(@NotNull Call<DtoUsers> call, @NotNull Throwable t) {
                loadingDialog.dimissDialog();
                Toast.makeText(ProductDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void pauseLoadingAnimation() {
        animation_add_to_cart.setVisibility(View.GONE);
        animation_add_to_cart.pauseAnimation();
        txt_add_to_cart.setVisibility(View.VISIBLE);

        BtnAddToCart.setEnabled(true);
        btnLessQT_Prod.setEnabled(true);
        btnPlusQT_Prod.setEnabled(true);
    }

    private void ShowShoppingCartAlert() {
        ConstraintLayout cardBtnSeeCart, cardBtnGoBackMenu;
        ShoppingCartAlert.setContentView(R.layout.adapter_shoppingcart);
        ShoppingCartAlert.setCancelable(false);

        cardBtnSeeCart = ShoppingCartAlert.findViewById(R.id.cardBtnSeeCart);
        cardBtnGoBackMenu = ShoppingCartAlert.findViewById(R.id.cardBtnGoBackMenu);

        cardBtnSeeCart.setOnClickListener(v -> {
            MainActivity mainActivity = new MainActivity();
            mainActivity.finish();
            Intent goTo_cart = new Intent(ProductDetailsActivity.this, ShoppingCartActivity.class);
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
            public void onResponse(@NotNull Call<DtoShoppingCart> call, @NotNull Response<DtoShoppingCart> response) {
                if (response.code() == 412){
                    infoCartSize.setVisibility(View.GONE);
                    txtCartSize.setText("");
                }else if(response.code() == 200){
                    assert response.body() != null;
                    txtCartSize.setText(response.body().getLength() + "");
                    infoCartSize.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoShoppingCart> call, @NotNull Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, R.string.error_to_find_your_cart, Toast.LENGTH_SHORT).show();
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
            public void onResponse(@NotNull Call<DtoMenuById> call, @NotNull Response<DtoMenuById> response) {
                if (response.code() == 200){
                    assert response.body() != null;
                    qt_prodGet = response.body().getQntd_prod();
                    if (qt_prodGet <= 0){
                        Toast.makeText(ProductDetailsActivity.this, "Sem estoque", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        nm_prod = response.body().getNm_prod();
                        ImageUrl = response.body().getImg_prod();
                        nm_prod_Desc.setText(nm_prod);
                        txtSize_ProductDesc.setText(response.body().getSize());
                        txtCategory_Prod_Desc.setText(nm_cat);
                        unit_prod_price = response.body().getPrice_prod();
                        price_prod_Desc.setText("R$ "+ unit_prod_price);
                        Picasso.get().load(ImageUrl).into(ImgProd_Desc, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                ImgProd_Desc.setVisibility(View.VISIBLE);
                                loadingDialog.dimissDialog();
                            }

                            @Override
                            public void onError(Exception e) {
                                loadingDialog.dimissDialog();
                                Toast.makeText(ProductDetailsActivity.this, R.string.error_to_get_image, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else if(response.code() == 404){
                    loadingDialog.dimissDialog();
                    Toast.makeText(ProductDetailsActivity.this, R.string.products_not_found, Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(ProductDetailsActivity.this, R.string.server_timeout, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DtoMenuById> call, @NotNull Throwable t) {
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