package co.ex.coffeeforcodeapp.Api.Category;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import co.ex.coffeeforcodeapp.Adapters.Category_Adapter;
import co.ex.coffeeforcodeapp.Api.Products.AsyncProdCategory;
import co.ex.coffeeforcodeapp.Api.Products.DtoMenu;
import co.ex.coffeeforcodeapp.Api.Products.RecyclerItemClickListener;
import co.ex.coffeeforcodeapp.HandlerJson.JsonHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class AsyncCategory extends AsyncTask {
    ArrayList<DtoCategorys> arrayListDto;
    Activity contexto;
    RecyclerView recyclerCategorys;
    LottieAnimationView AnimationcategoryLoading;
    String email_user;

    //  Products
    ArrayList<DtoMenu> arrayListDtomenu = new ArrayList<>();
    RecyclerView recyclerProducts;
    LottieAnimationView AnimationproductsLoading;
    SwipeRefreshLayout SwipeRefreshProducts;

    public AsyncCategory(RecyclerView recyclerCategorys, LottieAnimationView AnimationcategoryLoading, Activity contexto, ArrayList<DtoMenu> arrayListDto, RecyclerView recyclerProducts,
                         LottieAnimationView AnimationproductsLoading, SwipeRefreshLayout SwipeRefreshProducts, String email_user) {
        this.recyclerCategorys = recyclerCategorys;
        this.contexto = contexto;
        this.AnimationcategoryLoading =  AnimationcategoryLoading;

        this.recyclerProducts = recyclerProducts;
        this.AnimationproductsLoading = AnimationproductsLoading;
        this.SwipeRefreshProducts = SwipeRefreshProducts;
        this.email_user = email_user;
        this.arrayListDtomenu = arrayListDto;
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
                dtoCategorys.setCd_cat(jsonArray.getJSONObject(i).getInt("cd_cat"));
                dtoCategorys.setNm_cat(jsonArray.getJSONObject(i).getString("nm_cat"));
                dtoCategorys.setImg_cat_st(jsonArray.getJSONObject(i).getString("img_cat"));

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

        recyclerCategorys.addOnItemTouchListener(new RecyclerItemClickListener(contexto, recyclerCategorys,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int cd_cat = arrayListDto.get(position).getCd_cat();
                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2,StaggeredGridLayoutManager.VERTICAL);
                        recyclerProducts.setLayoutManager(layoutManager);
                        arrayListDtomenu.clear();
                        AsyncProdCategory asyncProdCategory = new AsyncProdCategory(recyclerProducts, AnimationproductsLoading, arrayListDtomenu, email_user, SwipeRefreshProducts, cd_cat, contexto);
                        asyncProdCategory.execute();
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
