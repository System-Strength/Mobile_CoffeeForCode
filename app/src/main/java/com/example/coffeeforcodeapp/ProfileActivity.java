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

public class ProfileActivity extends AppCompatActivity {
    //  Text Header
    TextView txtName_user, txtEmail_user;
    //  Text Body
    TextView txtcpfperfil, txtcelularperfil, txtenderecoperfil, txtcomplementoperfil, txtstatuscard_perfil, data_ativacaoparceiro_perfil;
    //  Bases
    ConstraintLayout base_nao_tem_celular_cadastrado, base_tem_celular_cadastrado, base_tem_endereco_cadastrado, base_nao_tem_endereco_cadastrado,
            nao_sou_parceiro_perfil, cliente_eparceiro, base_desc_parceiro_perfil;
    LottieAnimationView animacaogiftcardperfil;
    CardView btnvirarparceiro_perfil, card_cadastarceluar_perfil, cardbtn_cadastrarendereco_perfil, cardbtn_editarperfil;
    Handler timer = new Handler();
    int id_user, partner;
    String nm_user, email_user, phone_user, address_user, complement, img_user, cpf_user, partner_Startdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTheme(R.style.Perfil);

        txtName_user = findViewById(R.id.txtName_user);
        txtEmail_user = findViewById(R.id.txtEmail_user);
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
        id_user = bundle.getInt("id_user");
        email_user = bundle.getString("email_user");
        nm_user = bundle.getString("nm_user");
        cpf_user = bundle.getString("cpf_user");
        phone_user = bundle.getString("phone_user");
        address_user = bundle.getString("address_user");
        complement = bundle.getString("complement");
        img_user = bundle.getString("img_user");
        partner = bundle.getInt("partner");
        partner_Startdate = bundle.getString("partner_Startdate");

        carregar_info_user();

        btnvirarparceiro_perfil.setOnClickListener(v -> {
            Intent irpara_sejaparceira = new Intent(ProfileActivity.this, SejaParceiroActivity.class);
            irpara_sejaparceira.putExtra("id_user", id_user);
            irpara_sejaparceira.putExtra("email_user", email_user);
            irpara_sejaparceira.putExtra("nm_user", nm_user);
            irpara_sejaparceira.putExtra("cpf_user", cpf_user);
            irpara_sejaparceira.putExtra("phone_user", phone_user);
            irpara_sejaparceira.putExtra("address_user", address_user);
            irpara_sejaparceira.putExtra("complement", complement);
            irpara_sejaparceira.putExtra("img_user", img_user);
            irpara_sejaparceira.putExtra("partner", partner);
            irpara_sejaparceira.putExtra("partner_Startdate", partner_Startdate);
            startActivity(irpara_sejaparceira);
            finish();
        });

        cardbtn_editarperfil.setOnClickListener(v -> GoTo_EditProfile());

        card_cadastarceluar_perfil.setOnClickListener(v -> GoTo_EditProfile());

        cardbtn_cadastrarendereco_perfil.setOnClickListener(v -> GoTo_EditProfile());

    }

    private void GoTo_EditProfile() {
        Intent GoTo_EditPofile = new Intent(ProfileActivity.this, Editar_PerfilActivity.class);
        GoTo_EditPofile.putExtra("id_user", id_user);
        GoTo_EditPofile.putExtra("email_user", email_user);
        GoTo_EditPofile.putExtra("nm_user", nm_user);
        GoTo_EditPofile.putExtra("cpf_user", cpf_user);
        GoTo_EditPofile.putExtra("phone_user", phone_user);
        GoTo_EditPofile.putExtra("address_user", address_user);
        GoTo_EditPofile.putExtra("complement", complement);
        GoTo_EditPofile.putExtra("img_user", img_user);
        GoTo_EditPofile.putExtra("partner", partner);
        startActivity(GoTo_EditPofile);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void carregar_info_user(){
        txtName_user.setText(nm_user);
        txtEmail_user.setText(email_user);
        txtcpfperfil.setText(cpf_user);
        if (phone_user == null || phone_user.equals(" ")){
            base_nao_tem_celular_cadastrado.setVisibility(View.VISIBLE);
            base_tem_celular_cadastrado.setVisibility(View.GONE);
        }else {
            txtcelularperfil.setText(phone_user);
            base_nao_tem_celular_cadastrado.setVisibility(View.GONE);
            base_tem_celular_cadastrado.setVisibility(View.VISIBLE);
        }
        if (address_user == null || address_user.equals(" ")){
            base_tem_endereco_cadastrado.setVisibility(View.GONE);
            base_nao_tem_endereco_cadastrado.setVisibility(View.VISIBLE);

        }else {
            base_tem_endereco_cadastrado.setVisibility(View.VISIBLE);
            base_nao_tem_endereco_cadastrado.setVisibility(View.GONE);
            txtenderecoperfil.setText(address_user);
            if (complement == null){
                txtcomplementoperfil.setText("Complemento não cadastrado");
            }else {
                txtcomplementoperfil.setText(complement);
            }
        }
        info_parceiro();

    }

    @SuppressLint("SetTextI18n")
    private void info_parceiro(){
        if (partner == 0){
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

            cliente_eparceiro.setVisibility(View.VISIBLE);
            nao_sou_parceiro_perfil.setVisibility(View.GONE);
            txtstatuscard_perfil.setText("A um cartao ativado!!");
            data_ativacaoparceiro_perfil.setText("Assinatura gerada em: " + partner_Startdate);
        }
    }

    @Override
    public void onBackPressed() {
        Intent GoBack_ToMain = new Intent(ProfileActivity.this, MainActivity.class);
        GoBack_ToMain.putExtra("id_user", id_user);
        GoBack_ToMain.putExtra("nm_user", nm_user);
        GoBack_ToMain.putExtra("email_user", email_user);
        GoBack_ToMain.putExtra("phone_user", phone_user);
        GoBack_ToMain.putExtra("address_user", address_user);
        GoBack_ToMain.putExtra("complement", complement);
        GoBack_ToMain.putExtra("img_user", img_user);
        GoBack_ToMain.putExtra("address_user", address_user);
        GoBack_ToMain.putExtra("cpf_user", cpf_user);
        GoBack_ToMain.putExtra("partner", partner);
        GoBack_ToMain.putExtra("partner_Startdate", partner_Startdate);
        GoBack_ToMain.putExtra("statusavisoend","desativado");
        startActivity(GoBack_ToMain);
        finish();
    }
}