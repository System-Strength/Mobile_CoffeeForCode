package com.example.coffeeforcodeapp.Api.Category;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.Adapters.Category_Adapter;
import com.example.coffeeforcodeapp.Adapters.LoadingDialog;
import com.example.coffeeforcodeapp.Adapters.TopProducts_Adapter;
import com.example.coffeeforcodeapp.Api.DtoCategorys;
import com.example.coffeeforcodeapp.Api.DtoMenu;
import com.example.coffeeforcodeapp.HandlerJson.JsonHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class AsyncCategory extends AsyncTask {
    ArrayList<DtoCategorys> arrayListDto;
    Activity contexto;
    RecyclerView recyclerCategorys;
    LottieAnimationView AnimationcategoryLoading;

    public AsyncCategory(RecyclerView recyclerCategorys,LottieAnimationView AnimationcategoryLoading, Activity contexto) {
        this.recyclerCategorys = recyclerCategorys;
        this.contexto = contexto;
        this.AnimationcategoryLoading =  AnimationcategoryLoading;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        recyclerCategorys.setVisibility(View.GONE);
        AnimationcategoryLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://coffeeforcode.herokuapp.com/category");
        Category_Adapter category_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoCategorys dtoCategorys = new DtoCategorys();
                dtoCategorys.setNm_cat(jsonArray.getJSONObject(i).getString("nm_cat"));
                URL url = new URL(jsonArray.getJSONObject(i).getString("img_cat"));
                Bitmap img_cat = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                dtoCategorys.setImg_cat(img_cat);

                arrayListDto.add(dtoCategorys);
            }
            category_adapter = new Category_Adapter(arrayListDto);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return category_adapter;
    }

    @Override
    protected void onPostExecute(Object category_adapter) {
        super.onPostExecute(category_adapter);
        recyclerCategorys.setVisibility(View.VISIBLE);
        AnimationcategoryLoading.setVisibility(View.GONE);
        recyclerCategorys.setAdapter((RecyclerView.Adapter) category_adapter);
        //loadingDialog.dimissDialog();


    }
}
