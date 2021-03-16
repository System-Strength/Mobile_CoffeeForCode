package co.ex.coffeeforcodeapp.Api.Products;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import co.ex.coffeeforcodeapp.Adapters.Products_Adapter;
import co.ex.coffeeforcodeapp.Api.DtoMenu;
import co.ex.coffeeforcodeapp.HandlerJson.JsonHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class AsyncProducts extends AsyncTask {
    ArrayList<DtoMenu> arrayListDto;
    Activity contexto;
    RecyclerView recyclerProducts;
    SwipeRefreshLayout SwipeRefreshProducts;
    LottieAnimationView AnimationProductsLoading;
    private Object DtoMenu;

    public AsyncProducts(RecyclerView recyclerProducts, LottieAnimationView AnimationProductsLoading, SwipeRefreshLayout SwipeRefreshProducts, Activity contexto) {
        this.recyclerProducts = recyclerProducts;
        this.contexto = contexto;
        this.AnimationProductsLoading = AnimationProductsLoading;
        this.SwipeRefreshProducts = SwipeRefreshProducts;
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
                dtoMenu.setNm_prod(jsonArray.getJSONObject(i).getString("nm_prod"));
                //dtoMenu.setSize(jsonArray.getJSONObject(i).getString("size"));
                dtoMenu.setPrice_prod((float) jsonArray.getJSONObject(i).getDouble("price_prod"));
                URL url = new URL(jsonArray.getJSONObject(i).getString("img_prod"));
                Bitmap img_cat = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                dtoMenu.setImg_prod(img_cat);

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
        ((RecyclerView.Adapter) products_adapter).notifyDataSetChanged();
        SwipeRefreshProducts.setRefreshing(false);

        recyclerProducts.addOnItemTouchListener(new RecyclerItemClickListener(contexto, recyclerProducts,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(contexto, "Isso é: " + arrayListDto.get(position).getNm_prod(), Toast.LENGTH_SHORT).show();
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
