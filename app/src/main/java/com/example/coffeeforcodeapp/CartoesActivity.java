package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.DataBases.Cartoes.DaoCartoes;
import com.example.coffeeforcodeapp.DataBases.Cartoes.DtoCartoes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DtoClientes;

public class CartoesActivity extends AppCompatActivity {
    CardView cardcadastrarcartao;
    LottieAnimationView btnvoltarcartoes;
    RelativeLayout avisonenhumcardcadastrado;
    TextView txtnumerocartaocfc, txtvalidadecartaocfc, txtnomeproprietariocartaocfc;
    String emaillogado;
    String cpfdousuario;
    String numerocartaocfc, validadecartaocfc, nometitularcartaocfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoes);
        btnvoltarcartoes = findViewById(R.id.btnvoltarcartoes);
        avisonenhumcardcadastrado = findViewById(R.id.avisonenhumcardcadastrado);
        cardcadastrarcartao = findViewById(R.id.cardcadastrarcartao);

        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado  = bundle.getString("emailuser");

        receber_informacoes_do_cliente_e_receber_cartoes();

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
            Intent voltaraoprincipal = new Intent(CartoesActivity.this,PrincipalActivity.class);
            voltaraoprincipal.putExtra("emailuser",emaillogado);
            voltaraoprincipal.putExtra("statusavisoend","desativado");
            startActivity(voltaraoprincipal);
            finish();
        });
    }

    private void receber_informacoes_do_cliente_e_receber_cartoes(){
        DaoClientes daoClientes = new DaoClientes(CartoesActivity.this);
        DtoClientes dtoClientes = daoClientes.consultarclienteporemail(emaillogado);
        cpfdousuario = dtoClientes.getCpfcliente();

        DaoCartoes daoCartoes = new DaoCartoes(CartoesActivity.this);
        DtoCartoes dtoCartoes = daoCartoes.consultar_cartao_porcpf(cpfdousuario);
    }

    @Override
    public void onBackPressed() {
        Intent voltaraoprincipal = new Intent(CartoesActivity.this,PrincipalActivity.class);
        voltaraoprincipal.putExtra("emailuser",emaillogado);
        voltaraoprincipal.putExtra("statusavisoend","desativado");
        startActivity(voltaraoprincipal);
        finish();
    }
}