package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
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

public class EditProductActivity extends AppCompatActivity {
    CardView btnGoBackShoppingCart_edit, infoCartSize_edit;
    CardView btnLessQT_Prod_edit, btnPlusQT_Prod_edit;
    CardView BtnAddToCart_edit;
    LottieAnimationView animation_add_to_cart_edit;
    ImageView ImgProd_Desc_edit;
    TextView nm_prod_Desc_edit, price_prod_Desc_edit, txtQt_prod_edit, txtCartSize_edit, txt_add_to_cart_edit;

    String img_prod, nm_prod;
    int qt_prod, cd_prod;
    float price_prod;
    //  Product Information
    float unit_prod_price;
    float full_price_prod = unit_prod_price;
    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;
    LoadingDialog loadingDialog = new LoadingDialog(EditProductActivity.this);


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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_edit_product);
        btnGoBackShoppingCart_edit = findViewById(R.id.btnGoBackShoppingCart_edit);
        ImgProd_Desc_edit = findViewById(R.id.ImgProd_Desc_edit);
        nm_prod_Desc_edit = findViewById(R.id.nm_prod_Desc_edit);
        price_prod_Desc_edit = findViewById(R.id.price_prod_Desc_edit);
        txtQt_prod_edit = findViewById(R.id.txtQt_prod_edit);
        infoCartSize_edit = findViewById(R.id.infoCartSize_edit);
        txtCartSize_edit = findViewById(R.id.txtCartSize_edit);
        btnLessQT_Prod_edit = findViewById(R.id.btnLessQT_Prod_edit);
        btnPlusQT_Prod_edit = findViewById(R.id.btnPlusQT_Prod_edit);
        BtnAddToCart_edit = findViewById(R.id.BtnAddToCart_edit);
        animation_add_to_cart_edit = findViewById(R.id.animation_add_to_cart_edit);
        txt_add_to_cart_edit = findViewById(R.id.txt_add_to_cart_edit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email_user = bundle.getString("email_user");
        cd_prod = bundle.getInt("cd_prod");
        img_prod = bundle.getString("img_prod");
        nm_prod = bundle.getString("nm_prod");
        qt_prod = bundle.getInt("qt_prod");
        price_prod = bundle.getFloat("price_prod");
        unit_prod_price = bundle.getFloat("unit_prod_price");
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        GetCartSize();
        loadUserInformation();


        Picasso.get().load(img_prod).into(ImgProd_Desc_edit);
        nm_prod_Desc_edit.setText(nm_prod);
        price_prod_Desc_edit.setText(price_prod + "");
        txtQt_prod_edit.setText(qt_prod + "");

        btnLessQT_Prod_edit.setOnClickListener(v -> {
            if (qt_prod == 1){
                Toast.makeText(this, R.string.one_is_the_minumum_quantity, Toast.LENGTH_SHORT).show();
            }else{
                qt_prod--;
                setNewPrice(numberFormat);
                RefreshQtText();
            }
        });

        btnPlusQT_Prod_edit.setOnClickListener(v -> {
            if (qt_prod == 20){
                Toast.makeText(this, R.string.maximum_amount_reached, Toast.LENGTH_SHORT).show();
            }else {
                qt_prod++;
                setNewPrice(numberFormat);
                RefreshQtText();
            }
        });

        btnGoBackShoppingCart_edit.setOnClickListener(v -> gobackShoppingCart());

        BtnAddToCart_edit.setOnClickListener(v -> {
            animation_add_to_cart_edit.setVisibility(View.VISIBLE);
            txt_add_to_cart_edit.setVisibility(View.GONE);
            ShoppingCartService shoppingCartService = retrofitShoppingCart.create(ShoppingCartService.class);
            Call<DtoShoppingCart> cartCall = shoppingCartService.updateItem(email_user, cd_prod, qt_prod, full_price_prod);
            cartCall.enqueue(new Callback<DtoShoppingCart>() {
                @Override
                public void onResponse(Call<DtoShoppingCart> call, Response<DtoShoppingCart> response) {
                    if (response.code() == 202)
                        gobackShoppingCart();
                    else if(response.code() == 417){
                        animation_add_to_cart_edit.setVisibility(View.GONE);
                        txt_add_to_cart_edit.setVisibility(View.VISIBLE);
                        Toast.makeText(EditProductActivity.this, R.string.you_dont_have_product_cart, Toast.LENGTH_SHORT).show();
                    }else{
                        animation_add_to_cart_edit.setVisibility(View.GONE);
                        txt_add_to_cart_edit.setVisibility(View.VISIBLE);
                        Toast.makeText(EditProductActivity.this, "Erro: " + response.body(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DtoShoppingCart> call, Throwable t) {
                    animation_add_to_cart_edit.setVisibility(View.GONE);
                    txt_add_to_cart_edit.setVisibility(View.VISIBLE);
                    Toast.makeText(EditProductActivity.this, "Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    public void loadUserInformation(){
        loadingDialog.startLoading();
        UsersService usersService = retrofitUser.create(UsersService.class);
        Call<DtoUsers> call = usersService.infoUser(email_user);
        call.enqueue(new Callback<DtoUsers>() {
            @Override
            public void onResponse(@NotNull Call<DtoUsers> call, @NotNull Response<DtoUsers> response) {
                if (response.code() == 200) {
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
                }else {
                    loadingDialog.dimissDialog();
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoUsers> call, @NotNull Throwable t) {
                loadingDialog.dimissDialog();
                Toast.makeText(EditProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void RefreshQtText() {
        txtQt_prod_edit.setText(qt_prod + "");
    }

    @SuppressLint("SetTextI18n")
    private void setNewPrice(NumberFormat numberFormat) {
        full_price_prod = unit_prod_price * qt_prod;
        price_prod_Desc_edit.setText("R$ " + numberFormat.format(full_price_prod));
    }

    private void GetCartSize() {
        infoCartSize_edit.setVisibility(View.GONE);
        ShoppingCartService shoppingCartService = retrofitShoppingCart.create(ShoppingCartService.class);
        Call<DtoShoppingCart> cartCall = shoppingCartService.getCartInfomration(email_user);
        cartCall.enqueue(new Callback<DtoShoppingCart>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DtoShoppingCart> call, @NotNull Response<DtoShoppingCart> response) {
                if (response.code() == 412){
                    infoCartSize_edit.setVisibility(View.GONE);
                    txtCartSize_edit.setText("");
                }else if(response.code() == 200){
                    assert response.body() != null;
                    txtCartSize_edit.setText(response.body().getLength() + "");
                    infoCartSize_edit.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoShoppingCart> call, @NotNull Throwable t) {
                Toast.makeText(EditProductActivity.this, R.string.error_to_find_your_cart, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gobackShoppingCart() {
        Intent goTo_cart = new Intent(EditProductActivity.this, ShoppingCartActivity.class);
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
    }

    @Override
    public void onBackPressed() {
        gobackShoppingCart();
    }
}