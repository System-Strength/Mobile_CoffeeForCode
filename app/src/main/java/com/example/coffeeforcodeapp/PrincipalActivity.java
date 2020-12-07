package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DtoClientes;

public class PrincipalActivity extends AppCompatActivity {
    TextView txtnomedocliente;
    CardView cardviewnotpartner;
    String emaillogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        txtnomedocliente = findViewById(R.id.txtnomedocliente);
        cardviewnotpartner = findViewById(R.id.cardviewnotpartner);

        //  Get information for login of client
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado  = bundle.getString("emailuser");

        recebendodadosiniciaisdocliente();


    }

    private void recebendodadosiniciaisdocliente() {
        DaoClientes daoClientes = new DaoClientes(PrincipalActivity.this);
        DtoClientes dtoClientes = daoClientes.consultarcliente(emaillogado);
        String nomeclientecompleto = dtoClientes.getNomecliente();
        String[]  nomecliente = nomeclientecompleto.split(" ");
        String primeironome = nomecliente[0];

        //  Set first name of client
        txtnomedocliente.setText(primeironome);

        if (dtoClientes.getParceiro().equals("nao")){
            Toast.makeText(this, "Não Parceiro", Toast.LENGTH_SHORT).show();
            cardviewnotpartner.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(this, "Parceiro", Toast.LENGTH_SHORT).show();
            cardviewnotpartner.setVisibility(View.GONE);
        }
    }
}