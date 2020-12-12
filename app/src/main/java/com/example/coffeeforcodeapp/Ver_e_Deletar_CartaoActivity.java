package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeforcodeapp.Adapters.LoadingDialog;
import com.example.coffeeforcodeapp.DataBases.Cartoes.DaoCartoes;
import com.example.coffeeforcodeapp.DataBases.Cartoes.DtoCartoes;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class Ver_e_Deletar_CartaoActivity extends AppCompatActivity {
    CardView deletarcartao;
    DaoCartoes cartoes = new DaoCartoes(Ver_e_Deletar_CartaoActivity.this);
    DtoCartoes cartoaserapresentado;
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    TextView txtexnomeproprietarioex, txtexnumerocardex, txtexvalidadeex, txtbandeiracardex;
    String emaillogado;
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

        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado  = bundle.getString("emailuser");
        iddocartao  = bundle.getInt("iddocartao");

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

    }

    private void buscar_e_apresentar_o_cartao() {
        DaoCartoes daoCartoes = new DaoCartoes(Ver_e_Deletar_CartaoActivity.this);
        cartoaserapresentado = daoCartoes.consultar_cartao_pelo_id(iddocartao);

        txtbandeiracardex.setText(cartoaserapresentado.getBandeira());
        txtexnomeproprietarioex.setText(cartoaserapresentado.getNomedotitular());
        txtexnumerocardex.setText(cartoaserapresentado.getNumero());
        txtexvalidadeex.setText(cartoaserapresentado.getValidade());
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