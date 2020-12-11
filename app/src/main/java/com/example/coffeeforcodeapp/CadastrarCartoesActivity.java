package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wajahatkarim3.easyflipview.EasyFlipView;

public class CadastrarCartoesActivity extends AppCompatActivity {
    ConstraintLayout frentedocartao, primeirodadoscartao, segundodadoscartao;
    RelativeLayout costasdocartao;
    ConstraintLayout btnproximoparaccc, btnvoltaraoprimeirodados, btnfinalizar;
    EditText editnumerocartao, editnomeproprietariocartao;
    TextView txtbasenumerocard, txtexnomeproprietario;
    String emaillogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_cartoes);
        btnproximoparaccc = findViewById(R.id.btnproximoparaccc);
        primeirodadoscartao = findViewById(R.id.primeirodadoscartao);
        segundodadoscartao = findViewById(R.id.segundodadoscartao);
        btnvoltaraoprimeirodados = findViewById(R.id.btnvoltaraoprimeirodados);
        frentedocartao = findViewById(R.id.frentedocartao);
        costasdocartao = findViewById(R.id.costasdocartao);
        txtbasenumerocard = findViewById(R.id.txtbasenumerocard);
        txtexnomeproprietario = findViewById(R.id.txtexnomeproprietario);
        editnumerocartao = findViewById(R.id.editnumerocartao);
        editnomeproprietariocartao = findViewById(R.id.editnomeproprietariocartao);

        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado  = bundle.getString("emailuser");

        //  Set somethings with gone and visible
        primeirodadoscartao.setVisibility(View.VISIBLE);
        segundodadoscartao.setVisibility(View.GONE);
        frentedocartao.setVisibility(View.VISIBLE);
        costasdocartao.setVisibility(View.GONE);

        //  When click here will to next step of register
        btnproximoparaccc.setOnClickListener(v -> {
            frentedocartao.setVisibility(View.GONE);
            costasdocartao.setVisibility(View.VISIBLE);
            primeirodadoscartao.setVisibility(View.GONE);
            segundodadoscartao.setVisibility(View.VISIBLE);
        });

        //  When click here will go back to first step of register
        btnvoltaraoprimeirodados.setOnClickListener(v -> {
            frentedocartao.setVisibility(View.VISIBLE);
            costasdocartao.setVisibility(View.GONE);
            primeirodadoscartao.setVisibility(View.VISIBLE);
            segundodadoscartao.setVisibility(View.GONE);
        });

        //  When put some text will do some comands in real time
        editnumerocartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (editnumerocartao.getText() == null){
                    txtbasenumerocard.setText(R.string.numerocardzero);
                } else {
                    txtbasenumerocard.setText(editnumerocartao.getText());
                }
            }
        });

        editnomeproprietariocartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent voltaraoscartoes = new Intent(CadastrarCartoesActivity.this,PrincipalActivity.class);
        voltaraoscartoes.putExtra("emailuser",emaillogado);
        voltaraoscartoes.putExtra("statusavisoend","desativado");
        startActivity(voltaraoscartoes);
        finish();
    }
}