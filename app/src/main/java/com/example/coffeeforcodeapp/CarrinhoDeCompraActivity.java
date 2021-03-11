package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class CarrinhoDeCompraActivity extends AppCompatActivity {
    String emaillogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho_de_compra);

        // get some information  
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado = bundle.getString("emailuser");

    }

    @Override
    public void onBackPressed() {
        Intent voltaraoprincipal = new Intent(CarrinhoDeCompraActivity.this, MainActivity.class);
        voltaraoprincipal.putExtra("emailuser",emaillogado);
        voltaraoprincipal.putExtra("statusavisoend","desativado");
        startActivity(voltaraoprincipal);
        finish();

    }
}