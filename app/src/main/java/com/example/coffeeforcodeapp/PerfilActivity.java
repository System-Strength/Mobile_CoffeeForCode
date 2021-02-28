package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.LocalDataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.LocalDataBases.Clientes.DtoClientes;
import com.example.coffeeforcodeapp.LocalDataBases.Parceiro.DaoParceiro;
import com.example.coffeeforcodeapp.LocalDataBases.Parceiro.DtoParceiro;

public class PerfilActivity extends AppCompatActivity {
    //  Text Header
    TextView txtnomeuser, txtemailuser;
    //  Text Body
    TextView txtcpfperfil, txtcelularperfil, txtenderecoperfil, txtcomplementoperfil, txtstatuscard_perfil, data_ativacaoparceiro_perfil;
    //  Bases
    ConstraintLayout base_nao_tem_celular_cadastrado, base_tem_celular_cadastrado, base_tem_endereco_cadastrado, base_nao_tem_endereco_cadastrado,
            nao_sou_parceiro_perfil, cliente_eparceiro, base_desc_parceiro_perfil;
    LottieAnimationView animacaogiftcardperfil;
    CardView btnvirarparceiro_perfil, card_cadastarceluar_perfil, cardbtn_cadastrarendereco_perfil, cardbtn_editarperfil;
    DtoClientes clientelogado;
    DtoParceiro dados_do_parceiro;
    Handler timer = new Handler();
    String cpfcliente, emaillogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setTheme(R.style.Perfil);

        txtnomeuser = findViewById(R.id.txtnomeuser);
        txtemailuser = findViewById(R.id.txtemailuser);
        txtcpfperfil = findViewById(R.id.txtcpfperfil);
        base_nao_tem_celular_cadastrado = findViewById(R.id.base_nao_tem_celular_cadastrado);
        base_tem_celular_cadastrado = findViewById(R.id.base_tem_celular_cadastrado);
        txtcelularperfil = findViewById(R.id.txtcelularperfil);
        txtenderecoperfil = findViewById(R.id.txtenderecoperfil);
        txtcomplementoperfil = findViewById(R.id.txtcomplementoperfil);
        base_tem_endereco_cadastrado = findViewById(R.id.base_tem_endereco_cadastrado);
        base_nao_tem_endereco_cadastrado = findViewById(R.id.base_nao_tem_endereco_cadastrado);
        nao_sou_parceiro_perfil = findViewById(R.id.nao_sou_parceiro_perfil);
        cliente_eparceiro = findViewById(R.id.cliente_eparceiro);
        animacaogiftcardperfil = findViewById(R.id.animacaogiftcardperfil);
        base_desc_parceiro_perfil = findViewById(R.id.base_desc_parceiro_perfil);
        txtstatuscard_perfil = findViewById(R.id.txtstatuscard_perfil);
        data_ativacaoparceiro_perfil = findViewById(R.id.data_ativacaoparceiro_perfil);
        btnvirarparceiro_perfil = findViewById(R.id.btnvirarparceiro_perfil);
        card_cadastarceluar_perfil = findViewById(R.id.card_cadastarceluar_perfil);
        cardbtn_cadastrarendereco_perfil = findViewById(R.id.cardbtn_cadastrarendereco_perfil);
        cardbtn_editarperfil = findViewById(R.id.cardbtn_editarperfil);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado = bundle.getString("emailuser");

        carregar_info_user();

        btnvirarparceiro_perfil.setOnClickListener(v -> {
            Intent irpara_sejaparceira = new Intent(PerfilActivity.this, SejaParceiroActivity.class);
            irpara_sejaparceira.putExtra("emailuser", emaillogado);
            startActivity(irpara_sejaparceira);
            finish();
        });

        cardbtn_editarperfil.setOnClickListener(v -> {
            Intent irpara_editarperfil = new Intent(PerfilActivity.this, Editar_PerfilActivity.class);
            irpara_editarperfil.putExtra("emailuser", emaillogado);
            startActivity(irpara_editarperfil);
            finish();
        });

        card_cadastarceluar_perfil.setOnClickListener(v -> {
            Intent irpara_editarperfil = new Intent(PerfilActivity.this, Editar_PerfilActivity.class);
            irpara_editarperfil.putExtra("emailuser", emaillogado);
            startActivity(irpara_editarperfil);
            finish();
        });

        cardbtn_cadastrarendereco_perfil.setOnClickListener(v -> {
            Intent irpara_editarperfil = new Intent(PerfilActivity.this, Editar_PerfilActivity.class);
            irpara_editarperfil.putExtra("emailuser", emaillogado);
            startActivity(irpara_editarperfil);
            finish();
        });

    }

    @SuppressLint("SetTextI18n")
    private void carregar_info_user(){
        DaoClientes daoClientes = new DaoClientes(PerfilActivity.this);
        clientelogado = daoClientes.consultarclienteporemail(emaillogado);
        txtnomeuser.setText(clientelogado.getNomecliente());
        txtemailuser.setText(clientelogado.getEmailcliente());
        txtcpfperfil.setText(clientelogado.getCpfcliente());
        if (clientelogado.getCelularcliente() == null || clientelogado.getCelularcliente().equals("")){
            base_nao_tem_celular_cadastrado.setVisibility(View.VISIBLE);
            base_tem_celular_cadastrado.setVisibility(View.GONE);
        }else {
            txtcelularperfil.setText(clientelogado.getCelularcliente());
            base_nao_tem_celular_cadastrado.setVisibility(View.GONE);
            base_tem_celular_cadastrado.setVisibility(View.VISIBLE);
        }
        if (clientelogado.getEnderecocliente() == null || clientelogado.getComplementocliente() == null){
            base_tem_endereco_cadastrado.setVisibility(View.GONE);
            base_nao_tem_endereco_cadastrado.setVisibility(View.VISIBLE);

        }else {
            base_tem_endereco_cadastrado.setVisibility(View.VISIBLE);
            base_nao_tem_endereco_cadastrado.setVisibility(View.GONE);
            txtenderecoperfil.setText(clientelogado.getEnderecocliente());
            if (clientelogado.getComplementocliente() == null){
                txtcomplementoperfil.setText("Complemento não cadastrado");
            }else {
                txtcomplementoperfil.setText(clientelogado.getComplementocliente());
            }
        }
        info_parceiro();

    }

    @SuppressLint("SetTextI18n")
    private void info_parceiro(){
        DaoClientes daoClientes = new DaoClientes(PerfilActivity.this);
        clientelogado = daoClientes.consultarclienteporemail(emaillogado);
        cpfcliente = clientelogado.getCpfcliente();

        if (clientelogado.getParceiro().equals("nao")){
            cliente_eparceiro.setVisibility(View.GONE);
            base_desc_parceiro_perfil.setVisibility(View.GONE);
            nao_sou_parceiro_perfil.setVisibility(View.VISIBLE);
            animacaogiftcardperfil.setVisibility(View.VISIBLE);
            animacaogiftcardperfil.playAnimation();
            timer.postDelayed(() -> {
                animacaogiftcardperfil.setVisibility(View.GONE);
                base_desc_parceiro_perfil.setVisibility(View.VISIBLE);
            },2950);
        }else {
            DaoParceiro daoParceiro = new DaoParceiro(PerfilActivity.this);
            dados_do_parceiro = daoParceiro.consultarclienteporcpf(cpfcliente);

            cliente_eparceiro.setVisibility(View.VISIBLE);
            nao_sou_parceiro_perfil.setVisibility(View.GONE);
            txtstatuscard_perfil.setText("A um cartao ativado!!");
            data_ativacaoparceiro_perfil.setText("Assinatura gerada em: " + dados_do_parceiro.getData_ativacao());
        }
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