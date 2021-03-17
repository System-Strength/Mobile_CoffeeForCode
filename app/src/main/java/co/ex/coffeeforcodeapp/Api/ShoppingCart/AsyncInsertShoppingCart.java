package co.ex.coffeeforcodeapp.Api.ShoppingCart;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import co.ex.coffeeforcodeapp.HandlerJson.JsonHandler;

public class AsyncInsertShoppingCart extends AsyncTask {
    int cd_prod, qt_prod;
    String email_user;
    String baseUrl = "https://coffeeforcode.herokuapp.com/insert/";

    public AsyncInsertShoppingCart(int cd_prod, int qt_prod, String email_user) {
        this.cd_prod = cd_prod;
        this.qt_prod = qt_prod;
        this.email_user = email_user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return JsonHandler.getJson( baseUrl + email_user + "/" + cd_prod + "/" + qt_prod);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        try {
            JSONObject jsonObject = new JSONObject((String) result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
