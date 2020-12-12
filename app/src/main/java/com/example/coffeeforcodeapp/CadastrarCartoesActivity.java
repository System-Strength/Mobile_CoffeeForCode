package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeforcodeapp.Adapters.LoadingDialog;
import com.example.coffeeforcodeapp.DataBases.Cartoes.DaoCartoes;
import com.example.coffeeforcodeapp.DataBases.Cartoes.DtoCartoes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DtoClientes;

public class CadastrarCartoesActivity extends AppCompatActivity {
    ConstraintLayout frentedocartao, primeirodadoscartao, segundodadoscartao;
    RelativeLayout costasdocartao;
    ConstraintLayout btnproximoparaccc, btnvoltaraoprimeirodados, btnfinalizar;
    EditText editnumerocartao, editnomeproprietariocartao, editvalidadecartao, editccccartao, editbandeiracartao;
    TextView txtexnumerocard, txtexnomeproprietario,  txtexvalidade, txtexccc, txtbandeiracard;
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    String emaillogado, cpfpararegistarocartao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_cartoes);
        btnfinalizar = findViewById(R.id.btnfinalizar);
        btnproximoparaccc = findViewById(R.id.btnproximoparaccc);
        primeirodadoscartao = findViewById(R.id.primeirodadoscartao);
        segundodadoscartao = findViewById(R.id.segundodadoscartao);
        btnvoltaraoprimeirodados = findViewById(R.id.btnvoltaraoprimeirodados);
        frentedocartao = findViewById(R.id.frentedocartao);
        costasdocartao = findViewById(R.id.costasdocartao);
        txtexnumerocard = findViewById(R.id.txtexnumerocard);
        txtexnomeproprietario = findViewById(R.id.txtexnomeproprietario);
        txtexvalidade = findViewById(R.id.txtexvalidade);
        txtexccc = findViewById(R.id.txtexccc);
        txtbandeiracard = findViewById(R.id.txtbandeiracard);
        editnumerocartao = findViewById(R.id.editnumerocartao);
        editnomeproprietariocartao = findViewById(R.id.editnomeproprietariocartao);
        editvalidadecartao = findViewById(R.id.editvalidadecartao);
        editccccartao = findViewById(R.id.editccccartao);
        editbandeiracartao = findViewById(R.id.editbandeiracartao);
        LoadingDialog loadingDialog = new LoadingDialog(CadastrarCartoesActivity.this);
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set Mask
        editnumerocartao.addTextChangedListener(MaskEditUtil.mask(editnumerocartao, MaskEditUtil.FORMAT_NUMCARD));
        editvalidadecartao.addTextChangedListener(MaskEditUtil.mask(editvalidadecartao, MaskEditUtil.FORMAT_DATAVALI));

        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado  = bundle.getString("emailuser");
        
        recebendoprimeirosdadosdousuarios();

        //  Set somethings with gone and visible
        primeirodadoscartao.setVisibility(View.VISIBLE);
        segundodadoscartao.setVisibility(View.GONE);
        frentedocartao.setVisibility(View.VISIBLE);
        costasdocartao.setVisibility(View.GONE);

        //  When click here will to next step of register
        btnproximoparaccc.setOnClickListener(v -> {
                if (editnumerocartao.getText().length() == 0 || editnumerocartao.getText().length() < 19){
                    Toast.makeText(this, "Preencha corretamente o numero do cartão!!", Toast.LENGTH_SHORT).show();
                    editnumerocartao.requestFocus();
                    imm.showSoftInput(editnumerocartao, InputMethodManager.SHOW_IMPLICIT);
                }else if (editnomeproprietariocartao.getText() == null || editnomeproprietariocartao.getText().length() < 4){
                    Toast.makeText(this, "Preencha corretamente o impresso no cartão!!", Toast.LENGTH_SHORT).show();
                    editnomeproprietariocartao.requestFocus();
                    imm.showSoftInput(editnomeproprietariocartao, InputMethodManager.SHOW_IMPLICIT);
                }else if (editvalidadecartao.getText() == null || editvalidadecartao.getText().length() < 5){
                    Toast.makeText(this, "Preencha corretamente a validade do cartão!!", Toast.LENGTH_SHORT).show();
                    editvalidadecartao.requestFocus();
                    imm.showSoftInput(editvalidadecartao, InputMethodManager.SHOW_IMPLICIT);
                }else if (editbandeiracartao.getText() == null || editvalidadecartao.getText().length() < 5){
                    Toast.makeText(this, "Preencha corretamente a bandeira do cartão!!", Toast.LENGTH_SHORT).show();
                    editbandeiracartao.requestFocus();
                    imm.showSoftInput(editbandeiracartao, InputMethodManager.SHOW_IMPLICIT);
                }else {
                    editnumerocartao.setEnabled(false);
                    editnomeproprietariocartao.setEnabled(false);
                    editvalidadecartao.setEnabled(false);
                    editbandeiracartao.setEnabled(false);
                    editccccartao.setEnabled(true);
                    frentedocartao.setVisibility(View.GONE);
                    costasdocartao.setVisibility(View.VISIBLE);
                    primeirodadoscartao.setVisibility(View.GONE);
                    segundodadoscartao.setVisibility(View.VISIBLE);
                }
        });

        //  When click here will go back to first step of register
        btnvoltaraoprimeirodados.setOnClickListener(v -> {
            editnumerocartao.setEnabled(true);
            editnomeproprietariocartao.setEnabled(true);
            editvalidadecartao.setEnabled(true);
            editbandeiracartao.setEnabled(true);
            editccccartao.setEnabled(false);
            frentedocartao.setVisibility(View.VISIBLE);
            costasdocartao.setVisibility(View.GONE);
            primeirodadoscartao.setVisibility(View.VISIBLE);
            segundodadoscartao.setVisibility(View.GONE);
        });

        //  When put some text will do some comands in real time
        editnumerocartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (editnumerocartao.getText() == null){
                    txtexnumerocard.setText(R.string.numerocardzero);
                } else {
                    txtexnumerocard.setText(editnumerocartao.getText());
                }
            }
        });

        //  When put some text will do some comands in real time
        editnomeproprietariocartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editnomeproprietariocartao.getText() == null){
                    txtexnomeproprietario.setText(R.string.fulanodetal);
                } else {
                    txtexnomeproprietario.setText(editnomeproprietariocartao.getText());
                }
            }
        });

        //  When put some text will do some comands in real time
        editvalidadecartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editvalidadecartao.getText() == null){
                    txtexvalidade.setText(R.string.validadefake);
                } else {
                    txtexvalidade.setText(editvalidadecartao.getText());
                }
            }
        });

        //  When put some text will do some comands in real time
        editccccartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editccccartao.getText() == null){
                    txtexccc.setText(R.string.ccc);
                } else {
                    txtexccc.setText(editccccartao.getText());
                }
            }
        });

        //  When put some text will do some comands in real time
        editbandeiracartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editbandeiracartao.getText() == null){
                    txtbandeiracard.setText(R.string.bandeira);
                } else {
                    txtbandeiracard.setText(editbandeiracartao.getText());
                }
            }
        });

        //  When click in this button will try to register a new card
        btnfinalizar.setOnClickListener(v -> {
            if (editnumerocartao.getText().length() == 0 || editnumerocartao.getText().length() < 19){
                Toast.makeText(this, "Necessario preencher todos os dados", Toast.LENGTH_SHORT).show();
            }else if (editnomeproprietariocartao.getText() == null || editnomeproprietariocartao.getText().length() < 4){
                Toast.makeText(this, "Necessario preencher todos os dados", Toast.LENGTH_SHORT).show();
            }else if (editvalidadecartao.getText() == null || editvalidadecartao.getText().length() < 5){
                Toast.makeText(this, "Necessario preencher todos os dados", Toast.LENGTH_SHORT).show();
            }else if (editccccartao.getText() == null || editccccartao.getText().length() < 3){
                Toast.makeText(this, "Necessario preencher todos os dados", Toast.LENGTH_SHORT).show();
            }else if (editbandeiracartao.getText() == null || editbandeiracartao.getText().length() < 3){
                Toast.makeText(this, "Necessario preencher todos os dados", Toast.LENGTH_SHORT).show();
            }else {
                loadingDialog.startLoading();
                try {
                    DtoCartoes dtoCartoes = new DtoCartoes();
                    dtoCartoes.setNumero(editnumerocartao.getText().toString());
                    dtoCartoes.setNomedotitular(editnomeproprietariocartao.getText().toString());
                    dtoCartoes.setValidade(editvalidadecartao.getText().toString());
                    dtoCartoes.setCcc(editccccartao.getText().toString());
                    dtoCartoes.setBandeira(editbandeiracartao.getText().toString());
                    dtoCartoes.setCpfproprietario(cpfpararegistarocartao);
                    DaoCartoes daoCartoes = new DaoCartoes(CadastrarCartoesActivity.this);
                    long linhasinseridas = daoCartoes.cadastrar_novo_cartao(dtoCartoes);
                    if (linhasinseridas > 0){
                        timer.postDelayed(() -> {
                            loadingDialog.dimissDialog();
                            Intent voltaraoscartoes = new Intent(CadastrarCartoesActivity.this,CartoesActivity.class);
                            voltaraoscartoes.putExtra("emailuser",emaillogado);
                            voltaraoscartoes.putExtra("statusavisoend","desativado");
                            startActivity(voltaraoscartoes);
                            finish();
                        },1000);
                    }else {
                        Toast.makeText(this, "Ouve algum problema ao cadastrar tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    loadingDialog.dimissDialog();
                    Toast.makeText(this, "Erro ao cadastrar cartão: "+ ex + "\nTente novamente mais tarde!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //  Create method to get first information of client
    private void recebendoprimeirosdadosdousuarios() {
        try {
            DaoClientes daoClientes = new DaoClientes(CadastrarCartoesActivity.this);
            DtoClientes dtoClientes = daoClientes.consultarclienteporemail(emaillogado);
            cpfpararegistarocartao = dtoClientes.getCpfcliente();
        }catch (Exception ex){
            Toast.makeText(this, "Erro em carregar seus dados: "+ ex, Toast.LENGTH_SHORT).show();
            timer.postDelayed(() -> {
                Intent voltaraoscartoes = new Intent(CadastrarCartoesActivity.this,PrincipalActivity.class);
                voltaraoscartoes.putExtra("emailuser",emaillogado);
                voltaraoscartoes.putExtra("statusavisoend","desativado");
                startActivity(voltaraoscartoes);
                finish();
            },500);
        }
    }

    @Override
    public void onBackPressed() {
        Intent voltaraoscartoes = new Intent(CadastrarCartoesActivity.this,CartoesActivity.class);
        voltaraoscartoes.putExtra("emailuser",emaillogado);
        voltaraoscartoes.putExtra("statusavisoend","desativado");
        startActivity(voltaraoscartoes);
        finish();
    }
}