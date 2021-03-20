package co.ex.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ShoppingCartActivity extends AppCompatActivity {
    String emaillogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);

        // get some information  
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado = bundle.getString("emailuser");

    }

    @Override
    public void onBackPressed() {
        Intent voltaraoprincipal = new Intent(ShoppingCartActivity.this, MainActivity.class);
        voltaraoprincipal.putExtra("emailuser",emaillogado);
        voltaraoprincipal.putExtra("statusavisoend","desativado");
        startActivity(voltaraoprincipal);
        finish();

    }
}