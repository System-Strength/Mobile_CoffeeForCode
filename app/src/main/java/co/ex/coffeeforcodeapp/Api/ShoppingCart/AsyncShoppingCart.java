package co.ex.coffeeforcodeapp.Api.ShoppingCart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import co.ex.coffeeforcodeapp.Activitys.EditProductActivity;
import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Adapters.ShoppingCart_Adapter;
import co.ex.coffeeforcodeapp.Api.Products.DtoMenu;
import co.ex.coffeeforcodeapp.Api.Products.MenuService;
import co.ex.coffeeforcodeapp.Api.Products.RecyclerItemClickListener;
import co.ex.coffeeforcodeapp.HandlerJson.JsonHandler;
import co.ex.coffeeforcodeapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@SuppressLint("StaticFieldLeak")
public class AsyncShoppingCart extends AsyncTask {
    ArrayList<DtoShoppingCart> arrayListDto;
    Activity contexto;
    TextView txt_total;
    RecyclerView recyclerShoppingCart;
    String email_user;
    LoadingDialog loadingDialog;
    float fullPrice;
    //  Retrofit's
    final Retrofit retrofitShoppingCart = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/shoppingcart/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

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
        ShoppingCart_Adapter shoppingCart_adapter_ad = null;
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
                fullPrice += (float) jsonArray.getJSONObject(i).getDouble("full_price_prod");
                dtoShoppingCart.setImg_prod(jsonArray.getJSONObject(i).getString("img_prod"));
                arrayListDto.add(dtoShoppingCart);
            }
            shoppingCart_adapter_ad = new ShoppingCart_Adapter(arrayListDto);
            shoppingCart_adapter_ad.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return shoppingCart_adapter_ad;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(Object shoppingCart_adapter) {
        super.onPostExecute(shoppingCart_adapter);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        recyclerShoppingCart.setAdapter((RecyclerView.Adapter) shoppingCart_adapter);
        txt_total.setText("Total: R$ "+ numberFormat.format(fullPrice));
        loadingDialog.dimissDialog();
        recyclerShoppingCart.getRecycledViewPool().clear();
        recyclerShoppingCart.addOnItemTouchListener(new RecyclerItemClickListener(contexto, recyclerShoppingCart,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(contexto)
                        .setTitle(arrayListDto.get(position).getNm_prod() + "")
                        .setMessage(R.string.what_would_you_this_product)
                        .setPositiveButton(R.string.remove, (dialog, which) -> {
                            int cd_prod = arrayListDto.get(position).getCd_prod();
                            MenuService menuService = retrofitShoppingCart.create(MenuService.class);
                            Call<DtoMenu> menuCall = menuService.removeProd(email_user, cd_prod);
                            menuCall.enqueue(new Callback<DtoMenu>() {
                                @Override
                                public void onResponse(Call<DtoMenu> call, Response<DtoMenu> response) {
                                    if (response.code() == 202){
                                        arrayListDto.remove(position);
                                        AsyncShoppingCart asyncShoppingCart = new AsyncShoppingCart(recyclerShoppingCart, email_user, txt_total, contexto);
                                        asyncShoppingCart.execute();
                                    }else{
                                        Toast.makeText(contexto, R.string.you_dont_have_product_cart, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<DtoMenu> call, Throwable t) {
                                    Toast.makeText(contexto, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton(R.string.edit, (dialog, which) -> {
                            Intent goTo_Edit_prod = new Intent(contexto, EditProductActivity.class);
                            goTo_Edit_prod.putExtra("email_user", email_user);
                            goTo_Edit_prod.putExtra("cd_prod", arrayListDto.get(position).getCd_prod());
                            goTo_Edit_prod.putExtra("img_prod", arrayListDto.get(position).getImg_prod());
                            goTo_Edit_prod.putExtra("nm_prod", arrayListDto.get(position).getNm_prod());
                            goTo_Edit_prod.putExtra("qt_prod", arrayListDto.get(position).getQt_prod());
                            goTo_Edit_prod.putExtra("price_prod", arrayListDto.get(position).getFull_price_prod());
                            goTo_Edit_prod.putExtra("unit_prod_price", arrayListDto.get(position).getPrice_unit_prod());
                            contexto.startActivity(goTo_Edit_prod);
                            contexto.finish();
                        });
                alert.show();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }
}
