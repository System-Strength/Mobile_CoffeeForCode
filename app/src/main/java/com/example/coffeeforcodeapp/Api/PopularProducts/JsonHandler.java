package com.example.coffeeforcodeapp.Api.PopularProducts;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonHandler {
    public static String getJson(String textoURL){
        InputStream inputStream = null;
        String textojson = "";
        try {
            URL url = new URL(textoURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha = "";
            while((linha = bufferedReader.readLine()) != null){
                textojson += "\n"+linha;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            Log.d("ErroNetwork", e.toString());
        }
        return textojson;
    }
}
