package co.ex.coffeeforcodeapp.HandlerJson;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

        }catch (Exception e){
            Log.d("ErroNetwork", e.toString());
        }
        return textojson;
    }
}
