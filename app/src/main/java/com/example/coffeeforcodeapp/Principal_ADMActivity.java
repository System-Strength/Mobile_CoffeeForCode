package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.LocalDataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.LocalDataBases.Clientes.DtoClientes;

public class Principal_ADMActivity extends AppCompatActivity {
    TextView txt_olaadm;
    LottieAnimationView btn_voltar_login_adm;
    DtoClientes user;
    String emailloga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_adm);
        txt_olaadm = findViewById(R.id.txt_olaadm);
        btn_voltar_login_adm = findViewById(R.id.btn_voltar_login_adm);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emailloga = bundle.getString("emailuser");

        receber_info_user();

        btn_voltar_login_adm.setOnClickListener(v -> {
            Intent voltar_ao_login = new Intent(Principal_ADMActivity.this,LoginActivity.class);
            startActivity(voltar_ao_login);
            finish();
        });
    }


    @SuppressLint("SetTextI18n")
    public void receber_info_user(){
        try {
            DaoClientes daoClientes = new DaoClientes(Principal_ADMActivity.this);
            user = daoClientes.consultarclienteporemail(emailloga);
            String nome_completo = user.getNomecliente();
            String[] nomecliente = nome_completo.split(" ");
            String primeiro_nome = nomecliente[0];
            txt_olaadm.setText("Olá, " + primeiro_nome);


        }catch (Exception ex){
            Toast.makeText(this, "Erro: " + ex, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        Intent voltar_ao_login = new Intent(Principal_ADMActivity.this,LoginActivity.class);
        startActivity(voltar_ao_login);
        finish();
    }
}