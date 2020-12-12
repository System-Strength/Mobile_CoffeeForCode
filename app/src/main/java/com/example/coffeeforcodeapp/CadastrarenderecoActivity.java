package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class CadastrarenderecoActivity extends AppCompatActivity {
    String emaillogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrarendereco);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado = bundle.getString("emailuser");


    }

    @Override
    public void onBackPressed() {
        Intent voltarcadastrarendereco = new Intent(CadastrarenderecoActivity.this,PrincipalActivity.class);
        voltarcadastrarendereco.putExtra("emailuser",emaillogado);
        voltarcadastrarendereco.putExtra("statusavisoend","desativado");
        startActivity(voltarcadastrarendereco);
        finish();

    }
}