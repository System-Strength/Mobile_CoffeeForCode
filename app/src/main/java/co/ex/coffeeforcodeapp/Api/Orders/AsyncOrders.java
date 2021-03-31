package co.ex.coffeeforcodeapp.Api.Orders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.ex.coffeeforcodeapp.Activitys.FollowOrderActivity;
import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Adapters.Orders_Adapter;
import co.ex.coffeeforcodeapp.Api.Products.RecyclerItemClickListener;
import co.ex.coffeeforcodeapp.HandlerJson.JsonHandler;

@SuppressWarnings("rawtypes")
@SuppressLint("StaticFieldLeak")
public class AsyncOrders extends AsyncTask {
    ArrayList<DtoOrders> arrayListDto;
    Activity contexto;
    RecyclerView recyclerOrders;
    String email_user;
    LoadingDialog loadingDialog;
    SwipeRefreshLayout SwipeRefreshOrders;

    @SuppressWarnings("deprecation")
    public AsyncOrders(RecyclerView recyclerOrders, Activity contexto, SwipeRefreshLayout SwipeRefreshOrders, String email_user) {
        this.recyclerOrders = recyclerOrders;
        this.contexto = contexto;
        this.SwipeRefreshOrders = SwipeRefreshOrders;
        this.email_user = email_user;
        this.loadingDialog = new LoadingDialog(contexto);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        recyclerOrders.setVisibility(View.GONE);
        loadingDialog.startLoading();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://coffeeforcode.herokuapp.com/orders/" + email_user);
        Orders_Adapter orders_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("orders");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoOrders dtoOrders = new DtoOrders();
                dtoOrders.setCd_order(jsonArray.getJSONObject(i).getInt("cd_order"));
                dtoOrders.setEmail_user(jsonArray.getJSONObject(i).getString("email_user"));
                dtoOrders.setZipcode(jsonArray.getJSONObject(i).getString("zipcode"));
                dtoOrders.setAddress_user(jsonArray.getJSONObject(i).getString("address_user"));
                dtoOrders.setComplement(jsonArray.getJSONObject(i).getString("complement"));
                dtoOrders.setCd_prods(jsonArray.getJSONObject(i).getString("cd_prods"));
                dtoOrders.setPayFormat_user(jsonArray.getJSONObject(i).getString("PayFormat_user"));
                dtoOrders.setStatus(jsonArray.getJSONObject(i).getString("status"));
                dtoOrders.setHeld_in(jsonArray.getJSONObject(i).getString("held_in"));

                arrayListDto.add(dtoOrders);
            }
            orders_adapter = new Orders_Adapter(arrayListDto, contexto);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return orders_adapter;
    }

    @Override
    protected void onPostExecute(Object orders_adapter) {
        super.onPostExecute(orders_adapter);
        recyclerOrders.setVisibility(View.VISIBLE);
        loadingDialog.dimissDialog();
        recyclerOrders.setAdapter((RecyclerView.Adapter) orders_adapter);
        SwipeRefreshOrders.setRefreshing(false);

        recyclerOrders.addOnItemTouchListener(new RecyclerItemClickListener(contexto, recyclerOrders,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                loadingDialog.startLoading();
                Intent followOrder = new Intent(contexto, FollowOrderActivity.class);
                followOrder.putExtra("cd_order", arrayListDto.get(position).getCd_order());
                followOrder.putExtra("email_user", email_user);
                contexto.startActivity(followOrder);
                loadingDialog.dimissDialog();
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
