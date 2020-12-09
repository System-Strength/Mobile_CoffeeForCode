package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DtoClientes;

public class PrincipalActivity extends AppCompatActivity {
    TextView txtnomedocliente;
    CardView cardviewnotpartner;
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    Dialog avisoendereco;
    String emaillogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        txtnomedocliente = findViewById(R.id.txtnomedocliente);
        cardviewnotpartner = findViewById(R.id.cardviewnotpartner);
        avisoendereco = new Dialog(this);

        //  Get information for login of client
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado  = bundle.getString("emailuser");

        recebendodadosiniciaisdocliente();


    }

    private void  animacaonotpartner(){
        ConstraintLayout animationnotpartner, constraintdescnotpartner;
        animationnotpartner = findViewById(R.id.animationnotpartner);
        constraintdescnotpartner = findViewById(R.id.constraintdescnotpartner);

        cardviewnotpartner.setVisibility(View.VISIBLE);
        animationnotpartner.setVisibility(View.VISIBLE);
        constraintdescnotpartner.setVisibility(View.GONE);
        timer.postDelayed(() -> {
            animationnotpartner.setVisibility(View.GONE);
            constraintdescnotpartner.setVisibility(View.VISIBLE);
        },2950);
    }

    private void recebendodadosiniciaisdocliente() {
        DaoClientes daoClientes = new DaoClientes(PrincipalActivity.this);
        DtoClientes dtoClientes = daoClientes.consultarclienteporemail(emaillogado);
        String nomeclientecompleto = dtoClientes.getNomecliente();
        String[]  nomecliente = nomeclientecompleto.split(" ");
        String primeironome = nomecliente[0];

        //  Set first name of client
        txtnomedocliente.setText(primeironome);

        if (dtoClientes.getParceiro().equals("nao")){
            animacaonotpartner();
        }else {
            Toast.makeText(this, "Parceiro", Toast.LENGTH_SHORT).show();
            cardviewnotpartner.setVisibility(View.GONE);
        }

        if (dtoClientes.getEnderecocliente() == null || dtoClientes.getEnderecocliente().equals("")){
            mostraavisoend();
        }else {
            avisoendereco.dismiss();
        }
    }

    private void mostraavisoend(){
        ConstraintLayout btncadastraragoraend, btncadastrardepoisend;
        avisoendereco.setContentView(R.layout.aviso_sem_endereco_cadatrado);
        avisoendereco.setCancelable(false);
        btncadastraragoraend = avisoendereco.findViewById(R.id.btncadastraragoraend);
        btncadastrardepoisend = avisoendereco.findViewById(R.id.btncadastrardepoisend);

        btncadastraragoraend.setOnClickListener(v -> {
            Toast.makeText(this, "Em Desenvolvimento!!", Toast.LENGTH_SHORT).show();
            avisoendereco.dismiss();
        });

        btncadastrardepoisend.setOnClickListener(v -> avisoendereco.dismiss());

        avisoendereco.show();
    }

}