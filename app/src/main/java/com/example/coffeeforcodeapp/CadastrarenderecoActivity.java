package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CadastrarenderecoActivity extends AppCompatActivity {
    EditText edittextenderecocliente, edittextenderecoclientecomp;
    CardView cardviewenderecoconfirmar, cardviewenderecodepois;
    String emaillogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrarendereco);
        edittextenderecocliente = findViewById(R.id.edittextenderecocliente);
        edittextenderecoclientecomp = findViewById(R.id.edittextenderecoclientecomp);
        cardviewenderecoconfirmar = findViewById(R.id.cardviewenderecoconfirmar);
        cardviewenderecodepois = findViewById(R.id.cardviewenderecodepois);

        //get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado = bundle.getString("emailuser");

        //When click at this, it will trying to register address
        cardviewenderecoconfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CadastrarenderecoActivity.this, "Ex", Toast.LENGTH_SHORT).show();
            }
        });



        cardviewenderecodepois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent voltandoparaprincipal = new Intent(CadastrarenderecoActivity.this,PrincipalActivity.class);
                voltandoparaprincipal.putExtra("emailuser",emaillogado);
                voltandoparaprincipal.putExtra("statusavisoend","desativado");
                startActivity(voltandoparaprincipal);
                finish();

            }
        });

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