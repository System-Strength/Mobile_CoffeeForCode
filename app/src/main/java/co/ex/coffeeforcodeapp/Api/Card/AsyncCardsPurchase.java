package co.ex.coffeeforcodeapp.Api.Card;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.ex.coffeeforcodeapp.Adapters.CardsPurchase_Adapter;
import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.HandlerJson.JsonHandler;
import co.ex.coffeeforcodeapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


@SuppressWarnings("ALL")
@SuppressLint("StaticFieldLeak")
public class AsyncCardsPurchase extends AsyncTask {
    ArrayList<DtoCard> arrayListDto;
    Activity contexto;
    CardView card_noCard_purchase;
    RecyclerView recyclerCards;
    String email_user;
    LoadingDialog loadingDialog;

    //  Retrofit
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "card/remove/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final Retrofit retrofitCardCheck = new Retrofit.Builder()
            .baseUrl( baseurl + "card/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public AsyncCardsPurchase(RecyclerView recycler_cards_pruchase, CardView card_noCard_purchase, Activity contexto, String email_user) {
        this.recyclerCards = recycler_cards_pruchase;
        this.card_noCard_purchase = card_noCard_purchase;
        this.contexto = contexto;
        this.email_user = email_user;
        this.loadingDialog = new LoadingDialog(contexto);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        recyclerCards.setVisibility(View.GONE);
        card_noCard_purchase.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json =  JsonHandler.getJson("https://coffeeforcode.herokuapp.com/card/" + email_user);
        CardsPurchase_Adapter cards_adapter = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Cards");
            arrayListDto = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {
                DtoCard dtoCard = new DtoCard();
                dtoCard.setCd_card(jsonArray.getJSONObject(i).getInt("cd_card"));
                dtoCard.setNmUser_card(jsonArray.getJSONObject(i).getString("nmUser_card"));
                dtoCard.setFlag_card(jsonArray.getJSONObject(i).getString("flag_card"));
                dtoCard.setNumber_card(jsonArray.getJSONObject(i).getString("number_card"));

                arrayListDto.add(dtoCard);
            }
            cards_adapter = new CardsPurchase_Adapter(arrayListDto, contexto);
            cards_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorNetWork", e.toString());
        }
        return cards_adapter;
    }

    @Override
    protected void onPostExecute(Object category_adapter) {
        super.onPostExecute(category_adapter);
        recyclerCards.setVisibility(View.VISIBLE);
        card_noCard_purchase.setVisibility(View.GONE);
        //noinspection rawtypes
        recyclerCards.setAdapter((RecyclerView.Adapter) category_adapter);
        recyclerCards.getRecycledViewPool().clear();
    }

    private void GetCards(){
        CardService cardService = retrofitCard.create(CardService.class);
        Call<DtoCard> cardCall = cardService.getCardsOfUser(email_user);
        cardCall.enqueue(new Callback<DtoCard>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DtoCard> call, @NotNull Response<DtoCard> response) {
                if (response.code() == 412){
                    card_noCard_purchase.setVisibility(View.VISIBLE);
                    recyclerCards.setVisibility(View.GONE);
                }else if(response.code() == 200){
                    card_noCard_purchase.setVisibility(View.GONE);
                    recyclerCards.setVisibility(View.VISIBLE);
                    LinearLayoutManager linearLayout = new LinearLayoutManager(contexto);
                    recyclerCards.setLayoutManager(linearLayout);
                    AsyncCardsPurchase asyncCards = new AsyncCardsPurchase(recyclerCards, card_noCard_purchase, contexto, email_user);
                    asyncCards.execute();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DtoCard> call, @NotNull Throwable t) {
                Toast.makeText(contexto, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
            }
        });
    }
}