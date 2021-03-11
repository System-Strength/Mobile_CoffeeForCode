package com.example.coffeeforcodeapp.Api.PopularProducts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeforcodeapp.Adapters.LoadingDialog;
import com.example.coffeeforcodeapp.Adapters.TopProducts_Adapter;
import com.example.coffeeforcodeapp.Api.DtoMenu;
import com.squareup.picasso.Picasso;

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

    public AsyncPopularProducts(RecyclerView recyclerPopularProducts, Activity contexto) {
        this.recyclerPopularProducts = recyclerPopularProducts;
        this.contexto = contexto;
        loadingDialog = new LoadingDialog(contexto);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        recyclerPopularProducts.setAdapter((RecyclerView.Adapter) topProducts_adapter);
        //loadingDialog.dimissDialog();


    }
}
