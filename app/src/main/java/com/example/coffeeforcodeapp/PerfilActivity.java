package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DtoClientes;

public class PerfilActivity extends AppCompatActivity {
    TextView txtnomeuser, txtemailuser;
    DtoClientes clientelogado;
    String emaillogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setTheme(R.style.Perfil);

        txtnomeuser = findViewById(R.id.txtnomeuser);
        txtemailuser = findViewById(R.id.txtemailuser);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado = bundle.getString("emailuser");

        carregar_info_user();
    }

    private void carregar_info_user(){
        DaoClientes daoClientes = new DaoClientes(PerfilActivity.this);
        clientelogado = daoClientes.consultarclienteporemail(emaillogado);

        txtnomeuser.setText(clientelogado.getNomecliente());
        txtemailuser.setText(clientelogado.getEmailcliente());


    }

    @Override
    public void onBackPressed() {
        Intent voltaraoprincipal = new Intent(PerfilActivity.this,PrincipalActivity.class);
        voltaraoprincipal.putExtra("emailuser",emaillogado);
        voltaraoprincipal.putExtra("statusavisoend","desativado");
        startActivity(voltaraoprincipal);
        finish();
    }
}