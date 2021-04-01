package co.ex.coffeeforcodeapp.Api.PopularProducts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Adapters.TopProducts_Adapter;
import co.ex.coffeeforcodeapp.Api.Products.DtoMenu;
import co.ex.coffeeforcodeapp.Api.Products.RecyclerItemClickListener;
import co.ex.coffeeforcodeapp.HandlerJson.JsonHandler;
import co.ex.coffeeforcodeapp.Activitys.ProductDetailsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressWarnings("ALL")
@SuppressLint("StaticFieldLeak")
public class AsyncPopularProducts extends AsyncTask {
    ArrayList<DtoMenu> arrayListDto;
    Activity contexto;
    RecyclerView recyclerPopularProducts;
    LoadingDialog loadingDialog;
    CardView AnimationLoading_PopularProducts;
    String email_user, img_prod_st;

    public AsyncPopularProducts(RecyclerView recyclerPopularProducts, CardView AnimationLoading_PopularProducts, String email_user, Activity contexto) {
        this.recyclerPopularProducts = recyclerPopularProducts;
        this.contexto = contexto;
        this.AnimationLoading_PopularProducts = AnimationLoading_PopularProducts;
        this.email_user = email_user;
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
                dtoMenu.setCd_prod(jsonArray.getJSONObject(i).getInt("cd_prod"));
                dtoMenu.setBonusDesc(jsonArray.getJSONObject(i).getString("bonusDesc"));
                dtoMenu.setNm_prod(jsonArray.getJSONObject(i).getString("nm_prod"));
                dtoMenu.setSize(jsonArray.getJSONObject(i).getString("size"));
                dtoMenu.setCd_cat(jsonArray.getJSONObject(i).getInt("cd_cat"));
                dtoMenu.setNm_cat(jsonArray.getJSONObject(i).getString("nm_cat"));
                img_prod_st = jsonArray.getJSONObject(i).getString("img_prod");
                /*URL url = new URL(jsonArray.getJSONObject(i).getString("img_prod"));
                Bitmap img_prod = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                dtoMenu.setImg_prod(img_prod);*/
                dtoMenu.setImg_prod_st(img_prod_st);

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

        recyclerPopularProducts.addOnItemTouchListener(new RecyclerItemClickListener(contexto, recyclerPopularProducts,
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
