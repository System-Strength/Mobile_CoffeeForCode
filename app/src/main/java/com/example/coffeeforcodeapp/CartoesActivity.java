package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class CartoesActivity extends AppCompatActivity {
    ConstraintLayout basenaotemcartaocfc, basetenhocartaocfc;
    CardView cardcadastrarcartao, btnvermaissobrecardcfc;
    LottieAnimationView btnvoltarcartoes;
    RelativeLayout avisonenhumcardcadastrado;
    ListView listadecartoes;
    TextView txtnumerocartaocfc, txtnomeproprietariocartaocfc;
    String emaillogado;
    String cpfdousuario;
    String numerocartaocfc, nometitularcartaocfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoes);
        btnvoltarcartoes = findViewById(R.id.btnvoltarcartoes);
        avisonenhumcardcadastrado = findViewById(R.id.avisonenhumcardcadastrado);
        cardcadastrarcartao = findViewById(R.id.cardcadastrarcartao);
        listadecartoes = findViewById(R.id.listadecartoes);
        basenaotemcartaocfc = findViewById(R.id.basenaotemcartaocfc);
        basetenhocartaocfc = findViewById(R.id.basetenhocartaocfc);
        txtnumerocartaocfc = findViewById(R.id.txtnumerocartaocfc);
        btnvermaissobrecardcfc = findViewById(R.id.btnvermaissobrecardcfc);
        txtnomeproprietariocartaocfc = findViewById(R.id.txtnomeproprietariocartaocfc);

        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado  = bundle.getString("emailuser");

        //  When click here will to card register
        cardcadastrarcartao.setOnClickListener(v -> {
            Intent irparacadastramentodecartao = new Intent(CartoesActivity.this,CadastrarCartoesActivity.class);
            irparacadastramentodecartao.putExtra("emailuser",emaillogado);
            irparacadastramentodecartao.putExtra("statusavisoend","desativado");
            startActivity(irparacadastramentodecartao);
            finish();
        });

        //  When click here will back to PrincipalActivity
        btnvoltarcartoes.setOnClickListener(v -> {
            Intent voltaraoprincipal = new Intent(CartoesActivity.this, MainActivity.class);
            voltaraoprincipal.putExtra("emailuser",emaillogado);
            voltaraoprincipal.putExtra("statusavisoend","desativado");
            startActivity(voltaraoprincipal);
            finish();
        });

        //  When click here will to SejaParceiro
        btnvermaissobrecardcfc.setOnClickListener(v -> {
            Intent irparavirarparceiro = new Intent(CartoesActivity.this,SejaParceiroActivity.class);
            irparavirarparceiro.putExtra("emailuser",emaillogado);
            startActivity(irparavirarparceiro);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Intent voltaraoprincipal = new Intent(CartoesActivity.this, MainActivity.class);
        voltaraoprincipal.putExtra("emailuser",emaillogado);
        voltaraoprincipal.putExtra("statusavisoend","desativado");
        startActivity(voltaraoprincipal);
        finish();
    }
}