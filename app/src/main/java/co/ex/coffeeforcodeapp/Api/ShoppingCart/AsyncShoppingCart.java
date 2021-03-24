package co.ex.coffeeforcodeapp.Api.ShoppingCart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Adapters.Products_Adapter;
import co.ex.coffeeforcodeapp.Adapters.ShoppingCart_Adapter;
import co.ex.coffeeforcodeapp.Api.Products.DtoMenu;
import co.ex.coffeeforcodeapp.Api.Products.RecyclerItemClickListener;
import co.ex.coffeeforcodeapp.HandlerJson.JsonHandler;
import co.ex.coffeeforcodeapp.ProductDetailsActivity;

@SuppressLint("StaticFieldLeak")
public class AsyncShoppingCart extends AsyncTask {
    ArrayList<DtoShoppingCart> arrayListDto;
    Activity contexto;
    TextView txt_total;
    RecyclerView recyclerShoppingCart;
    String email_user;
    private Object DtoMenu;
    LoadingDialog loadingDialog;

    public AsyncShoppingCart(RecyclerView recyclerShoppingCart, String email_user, TextView txt_total, Activity contexto) {
        this.recyclerShoppingCart = recyclerShoppingCart;
        this.contexto = contexto;
        this.email_user = email_user;
        this.txt_total = txt_total;
        loadingDialog = new LoadingDialog(contexto);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog.startLoading();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://coffeeforcode.herokuapp.com/shoppingcart/" + email_user);
        ShoppingCart_Adapter shoppingCart_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("CartItens");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoShoppingCart dtoShoppingCart = new DtoShoppingCart();
                dtoShoppingCart.setCd_prod(jsonArray.getJSONObject(i).getInt("cd_prod"));
                dtoShoppingCart.setNm_prod(jsonArray.getJSONObject(i).getString("nm_prod"));
                dtoShoppingCart.setQt_prod(jsonArray.getJSONObject(i).getInt("qt_prod"));
                dtoShoppingCart.setPrice_unit_prod((float) jsonArray.getJSONObject(i).getDouble("price_unit_prod"));
                dtoShoppingCart.setFull_price_prod((float) jsonArray.getJSONObject(i).getDouble("full_price_prod"));
                URL url = new URL(jsonArray.getJSONObject(i).getString("img_prod"));
                Bitmap img_prod_cart = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                dtoShoppingCart.setImg_prod_cart(img_prod_cart);
                arrayListDto.add(dtoShoppingCart);
            }
            shoppingCart_adapter = new ShoppingCart_Adapter(txt_total, arrayListDto);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return shoppingCart_adapter;
    }

    @Override
    protected void onPostExecute(Object shoppingCart_adapter) {
        super.onPostExecute(shoppingCart_adapter);
        recyclerShoppingCart.setAdapter((RecyclerView.Adapter) shoppingCart_adapter);
        loadingDialog.dimissDialog();
    }
}
