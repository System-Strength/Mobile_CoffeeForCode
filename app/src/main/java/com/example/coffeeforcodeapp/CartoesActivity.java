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
import com.example.coffeeforcodeapp.Adapters.AdapterCartoes;
import com.example.coffeeforcodeapp.LocalDataBases.Cartoes.DaoCartoes;
import com.example.coffeeforcodeapp.LocalDataBases.Cartoes.DtoCartoes;
import com.example.coffeeforcodeapp.LocalDataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.LocalDataBases.Clientes.DtoClientes;
import com.example.coffeeforcodeapp.LocalDataBases.Parceiro.DaoParceiro;
import com.example.coffeeforcodeapp.LocalDataBases.Parceiro.DtoParceiro;

import java.util.ArrayList;

public class CartoesActivity extends AppCompatActivity {
    ConstraintLayout basenaotemcartaocfc, basetenhocartaocfc;
    CardView cardcadastrarcartao, btnvermaissobrecardcfc;
    LottieAnimationView btnvoltarcartoes;
    RelativeLayout avisonenhumcardcadastrado;
    ArrayList<DtoCartoes> cartoes;
    DtoCartoes cartaoselecionado;
    AdapterCartoes adapterCartoes;
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

        receber_informacoes_do_cliente_e_receber_cartoes();

        verificandocartaocfc();

        atualizarlistview();

        //  When do once click will go to Ver_e_Deletar_Cartao
        listadecartoes.setOnItemClickListener((parent, view, position, id) -> {
            cartaoselecionado = cartoes.get(position);
            Intent irparadetalhesdocartao = new Intent(CartoesActivity.this,Ver_e_Deletar_CartaoActivity.class);
            irparadetalhesdocartao.putExtra("iddocartao", cartaoselecionado.getId());
            irparadetalhesdocartao.putExtra("emailuser",emaillogado);
            irparadetalhesdocartao.putExtra("statusavisoend","desativado");
            startActivity(irparadetalhesdocartao);
            finish();

        });

        basetenhocartaocfc.setOnClickListener(v -> {
            Intent irparadetalhesdocartao = new Intent(CartoesActivity.this,Ver_e_Deletar_CartaoActivity.class);
            irparadetalhesdocartao.putExtra("iddocartao", 9999);
            irparadetalhesdocartao.putExtra("cpfparceiro", cpfdousuario);
            irparadetalhesdocartao.putExtra("emailuser",emaillogado);
            irparadetalhesdocartao.putExtra("statusavisoend","desativado");
            startActivity(irparadetalhesdocartao);
            finish();
        });

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

    //  Create method for update  ListView
    private void atualizarlistview() {
        DaoCartoes daoCartoes = new DaoCartoes(CartoesActivity.this);
        cartoes = daoCartoes.consultar_cartao_porcpf(cpfdousuario);
        if (cartoes.size() > 0){
            adapterCartoes = new AdapterCartoes(CartoesActivity.this, R.layout.modelo_cartoes, cartoes);
            listadecartoes.setAdapter(adapterCartoes);
            avisonenhumcardcadastrado.setVisibility(View.GONE);
            listadecartoes.setVisibility(View.VISIBLE);
        }else {
            avisonenhumcardcadastrado.setVisibility(View.VISIBLE);
            listadecartoes.setVisibility(View.GONE);
        }
    }

    //  Create method to get first information of client
    @SuppressLint("SetTextI18n")
    private void receber_informacoes_do_cliente_e_receber_cartoes(){
        DaoClientes daoClientes = new DaoClientes(CartoesActivity.this);
        DtoClientes dtoClientes = daoClientes.consultarclienteporemail(emaillogado);
        cpfdousuario = dtoClientes.getCpfcliente();
    }

    //  Create method to check if client have or not cfc card
    @SuppressLint("SetTextI18n")
    private void verificandocartaocfc() {
        DaoParceiro daoParceiro = new DaoParceiro(CartoesActivity.this);
        DtoParceiro dtoParceiro = daoParceiro.consultarclienteporcpf(cpfdousuario);
        if (dtoParceiro.getNumerocartao() == null){
            basenaotemcartaocfc.setVisibility(View.VISIBLE);
            basetenhocartaocfc.setVisibility(View.GONE);
        }else {
            basenaotemcartaocfc.setVisibility(View.GONE);
            basetenhocartaocfc.setVisibility(View.VISIBLE);
            numerocartaocfc = dtoParceiro.getNumerocartao();
            String numerodocartaocompleto = dtoParceiro.getNumerocartao() + " ";
            String[]  numerodocartao = numerodocartaocompleto.split(" ");
            String ultimonumero = numerodocartao[3];
            txtnumerocartaocfc.setText("**** " +ultimonumero);
            txtnomeproprietariocartaocfc.setText(dtoParceiro.getNomecliente());
            nometitularcartaocfc = dtoParceiro.getNomecliente();
        }
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