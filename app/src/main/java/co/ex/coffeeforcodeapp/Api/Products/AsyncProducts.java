package co.ex.coffeeforcodeapp.Api.Products;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import co.ex.coffeeforcodeapp.Adapters.Products_Adapter;
import co.ex.coffeeforcodeapp.HandlerJson.JsonHandler;
import co.ex.coffeeforcodeapp.Activitys.ProductDetailsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class AsyncProducts extends AsyncTask {
    ArrayList<DtoMenu> arrayListDto;
    Activity contexto;
    RecyclerView recyclerProducts;
    SwipeRefreshLayout SwipeRefreshProducts;
    LottieAnimationView AnimationProductsLoading;
    String email_user, img_prod_ad;
    private Object DtoMenu;

    public AsyncProducts(RecyclerView recyclerProducts, LottieAnimationView AnimationProductsLoading, SwipeRefreshLayout SwipeRefreshProducts, String email_user, Activity contexto) {
        this.recyclerProducts = recyclerProducts;
        this.contexto = contexto;
        this.AnimationProductsLoading = AnimationProductsLoading;
        this.SwipeRefreshProducts = SwipeRefreshProducts;
        this.email_user = email_user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        AnimationProductsLoading.playAnimation();
        recyclerProducts.setVisibility(View.GONE);
        AnimationProductsLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://coffeeforcode.herokuapp.com/products");
        Products_Adapter products_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Products");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoMenu dtoMenu = new DtoMenu();
                dtoMenu.setCd_prod(jsonArray.getJSONObject(i).getInt("cd_prod"));
                dtoMenu.setNm_prod(jsonArray.getJSONObject(i).getString("nm_prod"));
                dtoMenu.setNm_cat(jsonArray.getJSONObject(i).getString("nm_cat"));
                //dtoMenu.setSize(jsonArray.getJSONObject(i).getString("size"));
                dtoMenu.setPrice_prod((float) jsonArray.getJSONObject(i).getDouble("price_prod"));
                img_prod_ad = jsonArray.getJSONObject(i).getString("img_prod");
                dtoMenu.setImg_prod_st(img_prod_ad);
                /*URL url = new URL(jsonArray.getJSONObject(i).getString("img_prod"));
                Bitmap img_cat = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                dtoMenu.setImg_prod(img_cat);*/

                arrayListDto.add(dtoMenu);
            }
            products_adapter = new Products_Adapter(arrayListDto);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return products_adapter;
    }

    @Override
    protected void onPostExecute(Object products_adapter) {
        super.onPostExecute(products_adapter);
        recyclerProducts.setVisibility(View.VISIBLE);
        AnimationProductsLoading.setVisibility(View.GONE);
        AnimationProductsLoading.pauseAnimation();
        recyclerProducts.setAdapter((RecyclerView.Adapter) products_adapter);
        //((RecyclerView.Adapter) products_adapter).notifyDataSetChanged();
        SwipeRefreshProducts.setRefreshing(false);

        recyclerProducts.addOnItemTouchListener(new RecyclerItemClickListener(contexto, recyclerProducts,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int cd_prod = arrayListDto.get(position).getCd_prod();
                        String nm_cat = arrayListDto.get(position).getNm_cat();
                        Intent Goto_ProdDesc = new Intent(contexto, ProductDetailsActivity.class);
                        Goto_ProdDesc.putExtra("cd_prod", cd_prod);
                        Goto_ProdDesc.putExtra("email_user", email_user);
                        Goto_ProdDesc.putExtra("nm_cat", nm_cat);
                        contexto.startActivity(Goto_ProdDesc);
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
