package com.example.coffeeforcodeapp.Api.PopularProducts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeforcodeapp.Adapters.LoadingDialog;
import com.example.coffeeforcodeapp.Adapters.TopProducts_Adapter;
import com.example.coffeeforcodeapp.Api.DtoMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import retrofit2.http.Url;

@SuppressLint("StaticFieldLeak")
public class AsyncPopularProducts extends AsyncTask {
    ArrayList<DtoMenu> arrayListDto;
    Activity contexto;
    RecyclerView recyclerPopularProducts;
    LoadingDialog loadingDialog;
    CardView AnimationLoading_PopularProducts;

    public AsyncPopularProducts(RecyclerView recyclerPopularProducts, CardView AnimationLoading_PopularProducts, Activity contexto) {
        this.recyclerPopularProducts = recyclerPopularProducts;
        this.contexto = contexto;
        this.AnimationLoading_PopularProducts = AnimationLoading_PopularProducts;
        loadingDialog = new LoadingDialog(contexto);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        AnimationLoading_PopularProducts.setVisibility(View.VISIBLE);
        recyclerPopularProducts.setVisibility(View.GONE);
        //loadingDialog.startLoading();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://coffeeforcode.herokuapp.com/products/popular");
        TopProducts_Adapter topProducts_adapter = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("PopularProducts");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoMenu dtoMenu = new DtoMenu();
                dtoMenu.setBonusDesc(jsonArray.getJSONObject(i).getString("bonusDesc"));
                dtoMenu.setNm_prod(jsonArray.getJSONObject(i).getString("nm_prod"));
                dtoMenu.setSize(jsonArray.getJSONObject(i).getString("size"));
                URL url = new URL(jsonArray.getJSONObject(i).getString("img_prod"));
                Bitmap img_prod = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                dtoMenu.setImg_prod(img_prod);

                arrayListDto.add(dtoMenu);
            }

            topProducts_adapter = new TopProducts_Adapter(arrayListDto);


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
            //loadingDialog.dimissDialog();
        }

        return topProducts_adapter;
    }

    @Override
    protected void onPostExecute(Object topProducts_adapter) {
        super.onPostExecute(topProducts_adapter);
        recyclerPopularProducts.setVisibility(View.VISIBLE);
        AnimationLoading_PopularProducts.setVisibility(View.GONE);
        recyclerPopularProducts.setAdapter((RecyclerView.Adapter) topProducts_adapter);
        //loadingDialog.dimissDialog();


    }
}
