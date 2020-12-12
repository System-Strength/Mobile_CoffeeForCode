package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DtoClientes;

import java.security.Principal;

public class PrincipalActivity extends AppCompatActivity {
    TextView txtnomedocliente;
    CardView cardviewnotpartner, cardbepartner, cardvercartoes, cardvercarrinhodecompra;
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    Dialog avisoendereco;
    String emaillogado, apareceravisoendereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        txtnomedocliente = findViewById(R.id.txtnomedocliente);
        cardviewnotpartner = findViewById(R.id.cardviewnotpartner);
        cardbepartner = findViewById(R.id.cardbepartner);
        cardvercartoes = findViewById(R.id.cardvercartoes);
        cardvercarrinhodecompra = findViewById(R.id.cardvercarrinhodecompra);
        avisoendereco = new Dialog(this);

        //  Get information for login of client
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null){
            apareceravisoendereco = "ativado";
        }else {
            emaillogado  = bundle.getString("emailuser");
            apareceravisoendereco = bundle.getString("statusavisoend");
        }

        recebendodadosiniciaisdocliente();


        //  When click in this card user will to SejaParceiroActivity
        cardbepartner.setOnClickListener(v -> {
            Intent irparavirarparceiro = new Intent(PrincipalActivity.this,SejaParceiroActivity.class);
            irparavirarparceiro.putExtra("emailuser",emaillogado);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerdarapido, R.anim.mover_direitarapido);
            ActivityCompat.startActivity(PrincipalActivity.this,irparavirarparceiro, activityOptionsCompat.toBundle());
            finish();
        });

        //  When click in this card user will to CartoesActivity
        cardvercartoes.setOnClickListener(v -> {
            Intent irparacartoes = new Intent(PrincipalActivity.this,CartoesActivity.class);
            irparacartoes.putExtra("emailuser",emaillogado);
            startActivity(irparacartoes);
            finish();
        });

        //  When click in this card user will to cardvercarrinhodecompra
        cardvercarrinhodecompra.setOnClickListener(v -> {
            Intent vercarrinhodecompra = new Intent(PrincipalActivity.this,CarrinhoDeCompraActivity.class);
            vercarrinhodecompra.putExtra("emailuser", emaillogado);
            startActivity(vercarrinhodecompra);
            finish();

        });

    }

    //  Create method to check customer first things
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
            if (apareceravisoendereco.equals("desativado")){
                avisoendereco.dismiss();
            }else {
                mostraavisoend();
            }
        }else{
            avisoendereco.dismiss();
        }
    }

    //  Create Method for show alert of no adress register
    private void mostraavisoend(){
        ConstraintLayout btncadastraragoraend, btncadastrardepoisend;
        avisoendereco.setContentView(R.layout.aviso_sem_endereco_cadatrado);
        avisoendereco.setCancelable(false);
        btncadastraragoraend = avisoendereco.findViewById(R.id.btncadastraragoraend);
        btncadastrardepoisend = avisoendereco.findViewById(R.id.btncadastrardepoisend);

        btncadastraragoraend.setOnClickListener(v -> {
           Intent ircadastrarendereco = new Intent(PrincipalActivity.this,CadastrarenderecoActivity.class);
           ircadastrarendereco.putExtra("emailuser",emaillogado);
           startActivity(ircadastrarendereco);
           finish();
        });

        btncadastrardepoisend.setOnClickListener(v -> avisoendereco.dismiss());

        avisoendereco.show();
    }

    //  Create Method for do animation in Not Partner Card
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

}