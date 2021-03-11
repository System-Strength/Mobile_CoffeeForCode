package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.LocalDataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.LocalDataBases.Clientes.DtoClientes;

public class Editar_PerfilActivity extends AppCompatActivity {
    EditText edit_nome_edicaoperfil, edit_cpf_edicaoperfil, edit_email_edicaopefil, edit_celular_edicaopefil, edit_endereco_edicaopefil , edit_complemento_edicaopefil;
    //  Aterar Senha
    EditText edit_antiga_senha, edit_novasenha_senha01, edit_novasenha_senha02;
    ConstraintLayout base_dados_primarios, base_dados_secundarios;
    TextView txt_alter_senha, txt_btn_confirmar01, txt_btn_confirmar02;
    CardView card_confirmar_edicao, btn_alterar_senha;
    LottieAnimationView btn_voltaraoperfil, animationloading_dados01, animationloading_senha;
    DtoClientes cliente;
    Handler timer = new Handler();
    String emaillogado;
    String senha_antiga;
    int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar__perfil);
        btn_voltaraoperfil = findViewById(R.id.btn_voltaraoperfil);
        edit_nome_edicaoperfil = findViewById(R.id.edit_nome_edicaoperfil);
        edit_cpf_edicaoperfil = findViewById(R.id.edit_cpf_edicaoperfil);
        edit_email_edicaopefil = findViewById(R.id.edit_email_edicaopefil);
        edit_celular_edicaopefil = findViewById(R.id.edit_celular_edicaopefil);
        edit_endereco_edicaopefil = findViewById(R.id.edit_endereco_edicaopefil);
        edit_complemento_edicaopefil = findViewById(R.id.edit_complemento_edicaopefil);
        card_confirmar_edicao = findViewById(R.id.card_confirmar_edicao);
        txt_alter_senha = findViewById(R.id.txt_alter_senha);
        base_dados_primarios = findViewById(R.id.base_dados_primarios);
        base_dados_secundarios = findViewById(R.id.base_dados_secundarios);
        animationloading_dados01 = findViewById(R.id.animationloading_dados01);
        txt_btn_confirmar01 = findViewById(R.id.txt_btn_confirmar01);
        btn_alterar_senha = findViewById(R.id.btn_alterar_senha);
        edit_antiga_senha = findViewById(R.id.edit_antiga_senha);
        edit_novasenha_senha01 = findViewById(R.id.edit_novasenha_senha01);
        edit_novasenha_senha02 = findViewById(R.id.edit_novasenha_senha02);
        animationloading_senha = findViewById(R.id.animationloading_senha);
        txt_btn_confirmar02 = findViewById(R.id.txt_btn_confirmar02);
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set Mask
        edit_cpf_edicaoperfil.addTextChangedListener(MaskEditUtil.mask(edit_cpf_edicaoperfil, MaskEditUtil.FORMAT_CPF));
        edit_celular_edicaopefil.addTextChangedListener(MaskEditUtil.mask(edit_celular_edicaopefil, MaskEditUtil.FORMAT_FONE));

        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado = bundle.getString("emailuser");

        //  Set somethings with gone or visible
        base_dados_primarios.setVisibility(View.VISIBLE);
        base_dados_secundarios.setVisibility(View.GONE);
        animationloading_dados01.pauseAnimation();
        animationloading_dados01.setVisibility(View.GONE);
        animationloading_senha.setVisibility(View.GONE);

        buscar_informacoes();

        //  Btn go Back
        btn_voltaraoperfil.setOnClickListener(v -> voltar_ao_perfil());

        //  Card to confirm edit
        card_confirmar_edicao.setOnClickListener(v -> {
            if (edit_nome_edicaoperfil.getText() == null || edit_nome_edicaoperfil.getText().length() < 4){
                Toast.makeText(this, "Nome obrigatorio!!", Toast.LENGTH_SHORT).show();
                edit_nome_edicaoperfil.requestFocus();
                imm.showSoftInput(edit_nome_edicaoperfil, InputMethodManager.SHOW_IMPLICIT);
            }else if(edit_cpf_edicaoperfil.getText() == null || edit_cpf_edicaoperfil.getText().length() < 14){
                Toast.makeText(this, "CPF obrigatorio!!", Toast.LENGTH_SHORT).show();
                edit_cpf_edicaoperfil.requestFocus();
                imm.showSoftInput(edit_cpf_edicaoperfil, InputMethodManager.SHOW_IMPLICIT);
            }else if(edit_email_edicaopefil.getText() == null || edit_email_edicaopefil.getText().length() == 0){
                Toast.makeText(this, "Necessário preencher o campo: EMAIL", Toast.LENGTH_SHORT).show();
                edit_email_edicaopefil.requestFocus();
                imm.showSoftInput(edit_email_edicaopefil, InputMethodManager.SHOW_IMPLICIT);

            }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit_email_edicaopefil.getText()).matches()){
                Toast.makeText(this, "Preencha corretamente seu email", Toast.LENGTH_SHORT).show();
                edit_email_edicaopefil.requestFocus();
                imm.showSoftInput(edit_email_edicaopefil, InputMethodManager.SHOW_IMPLICIT);
            }else {
                animationloading_dados01.setVisibility(View.VISIBLE);
                txt_btn_confirmar01.setVisibility(View.GONE);
                animationloading_dados01.playAnimation();
                desabilitar_edit();
                timer.postDelayed(() -> {
                    try {
                        DtoClientes dados_aser_alterados = new DtoClientes();
                        dados_aser_alterados.setNomecliente(edit_nome_edicaoperfil.getText().toString());
                        dados_aser_alterados.setCpfcliente(edit_cpf_edicaoperfil.getText().toString());
                        dados_aser_alterados.setEmailcliente(edit_email_edicaopefil.getText().toString());
                        dados_aser_alterados.setComplementocliente(edit_complemento_edicaopefil.getText().toString());
                        dados_aser_alterados.setId(id_user);
                        if (edit_endereco_edicaopefil.getText().length() < 4){
                            dados_aser_alterados.setEnderecocliente(null);
                        }else {
                            dados_aser_alterados.setEnderecocliente(edit_endereco_edicaopefil.getText().toString());
                        }
                        if (edit_celular_edicaopefil.getText().length() < 15){
                            dados_aser_alterados.setCelularcliente(null);
                        }else {
                            dados_aser_alterados.setCelularcliente(edit_celular_edicaopefil.getText().toString());
                        }
                        DaoClientes daoClientes = new DaoClientes(Editar_PerfilActivity.this);
                        long linhasalteradas = daoClientes.atualizar_dados_primario(dados_aser_alterados);
                        if (linhasalteradas > 0){
                            Toast.makeText(this, "Dados atualizado!!", Toast.LENGTH_SHORT).show();
                            emaillogado = edit_email_edicaopefil.getText().toString();
                            voltar_ao_perfil();
                        }else {
                            Toast.makeText(this, "Nao foi possivel alterar seus dados!!\nTente novamente mais tarde.", Toast.LENGTH_LONG).show();
                            voltar_ao_perfil();
                        }
                    }catch (Exception ex){
                        Toast.makeText(this, "Erro ao atualizar: " + ex, Toast.LENGTH_LONG).show();
                        voltar_ao_perfil();
                    }
                },1500);
            }
        });

        //  Text to start seconds dates edit
        txt_alter_senha.setOnClickListener(v -> {
            base_dados_primarios.setVisibility(View.GONE);
            base_dados_secundarios.setVisibility(View.VISIBLE);
            card_confirmar_edicao.setEnabled(false);
        });

        //  When click here will try to register new password
        btn_alterar_senha.setOnClickListener(v -> {
            if (edit_antiga_senha.getText() == null || edit_antiga_senha.getText().length() == 0){
                Toast.makeText(this, "Necessario informar sua senha antiga!!", Toast.LENGTH_SHORT).show();
                edit_antiga_senha.requestFocus();
                imm.showSoftInput(edit_antiga_senha, InputMethodManager.SHOW_IMPLICIT);
            }else if (edit_novasenha_senha01.getText() == null || edit_novasenha_senha01.getText().length() == 0){
                Toast.makeText(this, "Necessario informar sua nova senha!!", Toast.LENGTH_SHORT).show();
                edit_novasenha_senha01.requestFocus();
                imm.showSoftInput(edit_novasenha_senha01, InputMethodManager.SHOW_IMPLICIT);
            }else if (edit_novasenha_senha02.getText() == null || edit_novasenha_senha02.getText().length() == 0){
                Toast.makeText(this, "Necessario confirmar sua nova senha!!", Toast.LENGTH_SHORT).show();
                edit_novasenha_senha01.requestFocus();
                imm.showSoftInput(edit_novasenha_senha01, InputMethodManager.SHOW_IMPLICIT);
            }else {
                if (edit_novasenha_senha01.getText().toString().equals(edit_novasenha_senha02.getText().toString())){
                    if (edit_antiga_senha.getText().toString().equals(senha_antiga)){
                        animationloading_senha.setVisibility(View.VISIBLE);
                        txt_btn_confirmar02.setVisibility(View.GONE);
                        timer.postDelayed(() -> {
                            try {
                                DtoClientes dtoClientes = new DtoClientes();
                                dtoClientes.setSenhacliente(edit_novasenha_senha01.getText().toString());
                                dtoClientes.setId(id_user);

                                DaoClientes daoClientes = new DaoClientes(Editar_PerfilActivity.this);
                                long linhas_alteradas = daoClientes.atualizar_dados_secundario(dtoClientes);
                                if (linhas_alteradas > 0){
                                    Toast.makeText(this, "Senha alterada!!", Toast.LENGTH_SHORT).show();
                                    voltar_ao_perfil();
                                }else {
                                    Toast.makeText(this, "Nao foi possivel alterar sua senha!!\nTente novamente mais tarde", Toast.LENGTH_SHORT).show();
                                    voltar_ao_perfil();
                                }
                            }catch (Exception ex){
                                Toast.makeText(this, "Erro ao cadastrar nova senha: " + ex + "\nContate um adm!!", Toast.LENGTH_SHORT).show();
                                voltar_ao_perfil();
                            }
                        },1500);
                    }else {
                        Toast.makeText(this, "Senha antiga nao confere!!", Toast.LENGTH_SHORT).show();
                        edit_antiga_senha.requestFocus();
                        imm.showSoftInput(edit_antiga_senha, InputMethodManager.SHOW_IMPLICIT);
                    }
                }else {
                    Toast.makeText(this, "Senha nao confere!!", Toast.LENGTH_SHORT).show();
                    edit_novasenha_senha02.requestFocus();
                    imm.showSoftInput(edit_novasenha_senha02, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

    }

    public void buscar_informacoes(){
        try {
            DaoClientes daoClientes = new DaoClientes(Editar_PerfilActivity.this);
            cliente = daoClientes.consultarclienteporemail(emaillogado);
            edit_nome_edicaoperfil.setText(cliente.getNomecliente());
            edit_cpf_edicaoperfil.setText(cliente.getCpfcliente());
            edit_email_edicaopefil.setText(cliente.getEmailcliente());
            edit_celular_edicaopefil.setText(cliente.getCelularcliente());
            edit_endereco_edicaopefil.setText(cliente.getEnderecocliente());
            edit_complemento_edicaopefil.setText(cliente.getComplementocliente());
            id_user = cliente.getId();
            senha_antiga = cliente.getSenhacliente();
        }catch (Exception ex){
            Toast.makeText(this, "Erro ao buscar informaçoes: " + ex , Toast.LENGTH_SHORT).show();
            voltar_ao_perfil();

        }
    }

    public void voltar_ao_perfil(){
        Intent voltar_ao_perfil = new Intent(Editar_PerfilActivity.this, ProfileActivity.class);
        voltar_ao_perfil.putExtra("emailuser", emaillogado);
        startActivity(voltar_ao_perfil);
        finish();
    }

    public void desabilitar_edit(){
        edit_nome_edicaoperfil.setEnabled(false);
        edit_cpf_edicaoperfil.setEnabled(false);
        edit_email_edicaopefil.setEnabled(false);
        edit_celular_edicaopefil.setEnabled(false);
        edit_endereco_edicaopefil.setEnabled(false);
        edit_complemento_edicaopefil.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        voltar_ao_perfil();
    }
}