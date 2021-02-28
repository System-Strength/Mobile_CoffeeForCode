package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeforcodeapp.Adapters.LoadingDialog;
import com.example.coffeeforcodeapp.LocalDataBases.Cartoes.DaoCartoes;
import com.example.coffeeforcodeapp.LocalDataBases.Cartoes.DtoCartoes;
import com.example.coffeeforcodeapp.LocalDataBases.Parceiro.DaoParceiro;
import com.example.coffeeforcodeapp.LocalDataBases.Parceiro.DtoParceiro;

public class Ver_e_Deletar_CartaoActivity extends AppCompatActivity {
    CardView deletarcartao, cancelarcartao, cartaonormal, excardcfc;
    DaoCartoes cartoes = new DaoCartoes(Ver_e_Deletar_CartaoActivity.this);
    DtoCartoes cartoaserapresentado;
    DtoParceiro cartaocfcapresentado;
    Handler timer = new Handler();
    TextView txtexnomeproprietarioex, txtexnumerocardex, txtexvalidadeex, txtbandeiracardex;
    TextView txtbandeiracardexcfc, txtexnumerocardexcfc, txtexnomeproprietarioexcfc, txtexvalidadeexcfc;
    String emaillogado, cpfdoparceiro;
    int iddocartao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_e__deletar__cartao);
        txtexnomeproprietarioex = findViewById(R.id.txtexnomeproprietarioex);
        txtexnumerocardex = findViewById(R.id.txtexnumerocardex);
        txtexvalidadeex = findViewById(R.id.txtexvalidadeex);
        txtbandeiracardex = findViewById(R.id.txtbandeiracardex);
        deletarcartao = findViewById(R.id.deletarcartao);
        cancelarcartao = findViewById(R.id.cancelarcartao);
        cartaonormal = findViewById(R.id.cartaonormal);
        excardcfc = findViewById(R.id.excardcfc);
        txtbandeiracardexcfc = findViewById(R.id.txtbandeiracardexcfc);
        txtexnumerocardexcfc = findViewById(R.id.txtexnumerocardexcfc);
        txtexnomeproprietarioexcfc = findViewById(R.id.txtexnomeproprietarioexcfc);
        txtexvalidadeexcfc = findViewById(R.id.txtexvalidadeexcfc);

        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado  = bundle.getString("emailuser");
        iddocartao  = bundle.getInt("iddocartao");
        if (bundle.getString("cpfparceiro") == null){
            cpfdoparceiro = null;
        }else {
            cpfdoparceiro = bundle.getString("cpfparceiro");
        }

        buscar_e_apresentar_o_cartao();

        //  When click will try to delete the card
        deletarcartao.setOnClickListener(v -> {
            AlertDialog.Builder aviso = new AlertDialog.Builder(Ver_e_Deletar_CartaoActivity.this);
            aviso.setIcon(R.drawable.logocfcsembg);
            aviso.setTitle("Deletar Cartão");
            aviso.setMessage("Desejá realmente deletar esse cartão");
            aviso.setPositiveButton("Sim", (dialog, which) -> {
                LoadingDialog loadingDialog = new LoadingDialog(Ver_e_Deletar_CartaoActivity.this);
                loadingDialog.startLoading();
                try {
                    int deletado = cartoes.excluir_cartao(cartoaserapresentado);
                    if (deletado > 0) {
                        timer.postDelayed(() -> {
                            Intent voltaraocartoes = new Intent(Ver_e_Deletar_CartaoActivity.this, CartoesActivity.class);
                            voltaraocartoes.putExtra("emailuser", emaillogado);
                            voltaraocartoes.putExtra("statusavisoend", "desativado");
                            startActivity(voltaraocartoes);
                            finish();
                            loadingDialog.dimissDialog();
                        }, 500);
                    } else {
                        loadingDialog.dimissDialog();
                        Toast.makeText(this, "Falha ao cadastrar tente mais tarde!!!", Toast.LENGTH_SHORT).show();
                        Intent voltaraocartoes = new Intent(Ver_e_Deletar_CartaoActivity.this, CartoesActivity.class);
                        voltaraocartoes.putExtra("emailuser", emaillogado);
                        voltaraocartoes.putExtra("statusavisoend", "desativado");
                        startActivity(voltaraocartoes);
                        finish();
                    }
                } catch (Exception ex) {
                    Toast.makeText(this, "Erro ao excluir: " + ex, Toast.LENGTH_LONG).show();
                    Intent voltaraocartoes = new Intent(Ver_e_Deletar_CartaoActivity.this, CartoesActivity.class);
                    voltaraocartoes.putExtra("emailuser", emaillogado);
                    voltaraocartoes.putExtra("statusavisoend", "desativado");
                    startActivity(voltaraocartoes);
                    finish();
                }
            }).setNegativeButton("Não", null);
            aviso.show();
        });

        //  When click here will go to cancel card
        cancelarcartao.setOnClickListener(v -> {
            //Intent irparacancelamento = new Intent(Ver_e_Deletar_CartaoActivity.this, Cancelamento.class);
            Toast.makeText(this, "Em dev...", Toast.LENGTH_SHORT).show();
        });

    }

    @SuppressLint("SetTextI18n")
    private void buscar_e_apresentar_o_cartao() {
        if (iddocartao != 9999){
            DaoCartoes daoCartoes = new DaoCartoes(Ver_e_Deletar_CartaoActivity.this);
            cartoaserapresentado = daoCartoes.consultar_cartao_pelo_id(iddocartao);

            txtbandeiracardex.setText(cartoaserapresentado.getBandeira());
            txtexnomeproprietarioex.setText(cartoaserapresentado.getNomedotitular());
            String numerodocartaocompleto = cartoaserapresentado.getNumero() + " ";
            String[]  numerodocartao = numerodocartaocompleto.split(" ");
            String ultimonumero = numerodocartao[3];
            txtexnumerocardex.setText("**** " + ultimonumero);
            txtexvalidadeex.setText(cartoaserapresentado.getValidade());
            cartaonormal.setVisibility(View.VISIBLE);
            excardcfc.setVisibility(View.GONE);
            cancelarcartao.setVisibility(View.GONE);
        }else {
            DaoParceiro daoParceiro = new DaoParceiro(Ver_e_Deletar_CartaoActivity.this);
            cartaocfcapresentado = daoParceiro.consultarclienteporcpf(cpfdoparceiro);

            txtbandeiracardexcfc.setText("Visa");
            txtexnomeproprietarioexcfc.setText(cartaocfcapresentado.getNomecliente());
            String numerodocartaocompleto = cartaocfcapresentado.getNumerocartao() + " ";
            String[]  numerodocartao = numerodocartaocompleto.split(" ");
            String ultimonumero = numerodocartao[3];
            txtexnumerocardexcfc.setText("**** " + ultimonumero);
            txtexvalidadeexcfc.setText("Ativo");
            deletarcartao.setVisibility(View.GONE);
            cancelarcartao.setVisibility(View.VISIBLE);
            cartaonormal.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {
        Intent voltaraocartoes = new Intent(Ver_e_Deletar_CartaoActivity.this,CartoesActivity.class);
        voltaraocartoes.putExtra("emailuser",emaillogado);
        voltaraocartoes.putExtra("statusavisoend","desativado");
        startActivity(voltaraocartoes);
        finish();
    }
}