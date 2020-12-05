package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.Adapters.LoadingDialog;
import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DtoClientes;


public class CriarContaActivity extends AppCompatActivity {
    TextView txtlogar, txttermos;
    CheckBox checkboxaceitoostermos;
    EditText edittextnomecriarconta, edittextcpfcriarconta, edittextemailcriarconta, edittextsenhacriarconta;
    ImageView imgolhofechadocriarconta, imgolhoabertocriarconta;
    LottieAnimationView btnvoltarcriarconta, certosenhacriarconta;
    CardView cardviewbtncriarconta;
    Dialog termos, avisoerro;
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);
        btnvoltarcriarconta = findViewById(R.id.btnvoltarcriarconta);
        certosenhacriarconta = findViewById(R.id.certosenhacriarconta);
        cardviewbtncriarconta = findViewById(R.id.cardviewbtncriarconta);
        imgolhofechadocriarconta = findViewById(R.id.imgolhofechadocriarconta);
        imgolhoabertocriarconta = findViewById(R.id.imgolhoabertocriarconta);
        edittextsenhacriarconta = findViewById(R.id.edittextsenhacriarconta);
        edittextemailcriarconta = findViewById(R.id.edittextemailcriarconta);
        edittextnomecriarconta = findViewById(R.id.edittextnomecriarconta);
        edittextcpfcriarconta = findViewById(R.id.edittextcpfcriarconta);
        checkboxaceitoostermos = findViewById(R.id.checkboxaceitoostermos);
        txttermos = findViewById(R.id.txttermos);
        txtlogar = findViewById(R.id.txtlogar);
        termos = new Dialog(this);
        avisoerro = new Dialog(this);
        LoadingDialog loadingDialog = new LoadingDialog(CriarContaActivity.this);
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set Mask
        edittextcpfcriarconta.addTextChangedListener(MaskEditUtil.mask(edittextcpfcriarconta, MaskEditUtil.FORMAT_CPF));

        //  Set some thinks with gone
        certosenhacriarconta.setVisibility(View.GONE);
        imgolhofechadocriarconta.setVisibility(View.GONE);
        imgolhoabertocriarconta.setVisibility(View.GONE);

        //  When click in the eye closed will hide de password
        imgolhofechadocriarconta.setOnClickListener(v -> {
            edittextsenhacriarconta.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgolhoabertocriarconta.setVisibility(View.VISIBLE);
            imgolhofechadocriarconta.setVisibility(View.GONE);
            edittextsenhacriarconta.setSelection(edittextsenhacriarconta.getText().length());
        });

        //  When click in the eye opened will show de password
        imgolhoabertocriarconta.setOnClickListener(v -> {
            edittextsenhacriarconta.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgolhoabertocriarconta.setVisibility(View.GONE);
            imgolhofechadocriarconta.setVisibility(View.VISIBLE);
            edittextsenhacriarconta.setSelection(edittextsenhacriarconta.getText().length());
        });

        //  When click here will go back to LoginActivity
        btnvoltarcriarconta.setOnClickListener(v -> {
            Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
            startActivity(voltaraologin);
            finish();
        });

        //  When click here will try to create a new account
        cardviewbtncriarconta.setOnClickListener(v -> {
            if (edittextnomecriarconta.getText() == null || edittextnomecriarconta.getText().length() < 4){
                Toast.makeText(this, "Necessário preencher corretamente o campo: NOME", Toast.LENGTH_SHORT).show();
                edittextnomecriarconta.requestFocus();
                imm.showSoftInput(edittextnomecriarconta, InputMethodManager.SHOW_IMPLICIT);

            }else if(edittextcpfcriarconta.getText() == null || edittextcpfcriarconta.getText().length() < 14){
                Toast.makeText(this, "CPF informado é invalido", Toast.LENGTH_SHORT).show();
                edittextcpfcriarconta.requestFocus();
                imm.showSoftInput(edittextcpfcriarconta, InputMethodManager.SHOW_IMPLICIT);

            }else if(edittextemailcriarconta.getText() == null || edittextemailcriarconta.getText().length() == 0){
                Toast.makeText(this, "Necessário preencher o campo: EMAIL", Toast.LENGTH_SHORT).show();
                edittextemailcriarconta.requestFocus();
                imm.showSoftInput(edittextemailcriarconta, InputMethodManager.SHOW_IMPLICIT);

            }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edittextemailcriarconta.getText()).matches()){
                Toast.makeText(this, "Preencha corretamente seu email", Toast.LENGTH_SHORT).show();
                edittextemailcriarconta.requestFocus();
                imm.showSoftInput(edittextemailcriarconta, InputMethodManager.SHOW_IMPLICIT);

            }else if (edittextsenhacriarconta.getText() == null || edittextsenhacriarconta.getText().length() < 8){
                Toast.makeText(this, "Necessário preencher corretamente o campo: SENHA\nMinimo 8 caracteres", Toast.LENGTH_SHORT).show();
                edittextsenhacriarconta.requestFocus();
                imm.showSoftInput(edittextsenhacriarconta, InputMethodManager.SHOW_IMPLICIT);

            }else if(!checkboxaceitoostermos.isChecked()){
                Toast.makeText(this, "Necessario aceitar os termos", Toast.LENGTH_SHORT).show();
            }else {
                DtoClientes dtoClientes = new DtoClientes();
                dtoClientes.setNomecliente(edittextnomecriarconta.getText().toString());
                dtoClientes.setCpfcliente(edittextcpfcriarconta.getText().toString());
                dtoClientes.setEmailcliente(edittextemailcriarconta.getText().toString());
                dtoClientes.setSenhacliente(edittextsenhacriarconta.getText().toString());
                DaoClientes daoClientes = new DaoClientes(CriarContaActivity.this);
                loadingDialog.startLoading();
                timer.postDelayed(() -> {
                    try {
                        long linhasafetadas = daoClientes.cadastrar(dtoClientes);
                        if (linhasafetadas > 0){
                            Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
                            voltaraologin.putExtra("emailusu",edittextemailcriarconta.getText().toString());
                            voltaraologin.putExtra("senhausu",edittextsenhacriarconta.getText().toString());
                            startActivity(voltaraologin);
                            finish();
                        }else {
                            loadingDialog.dimissDialog();
                            mostrarerro();
                        }
                    }catch (Exception ex){
                        Toast.makeText(this, "Erro ao criar conta: "+ ex, Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });

        //  When click will go back to LoginActivity
        txtlogar.setOnClickListener(v -> {
            Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
            startActivity(voltaraologin);
            finish();
        });

        //  Set commands to start in real time
        edittextsenhacriarconta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittextsenhacriarconta.getText() == null || edittextsenhacriarconta.getText().length() == 0){
                    imgolhoabertocriarconta.setVisibility(View.GONE);
                    imgolhofechadocriarconta.setVisibility(View.GONE);
                    edittextsenhacriarconta.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    if (edittextsenhacriarconta.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                        imgolhoabertocriarconta.setVisibility(View.VISIBLE);
                        imgolhofechadocriarconta.setVisibility(View.GONE);
                    }else{
                        imgolhofechadocriarconta.setVisibility(View.VISIBLE);
                        imgolhoabertocriarconta.setVisibility(View.GONE);
                    }
                }
                if (edittextsenhacriarconta.getText().length() >= 8){
                    certosenhacriarconta.setVisibility(View.VISIBLE);
                    certosenhacriarconta.playAnimation();
                }else {
                    certosenhacriarconta.setVisibility(View.GONE);
                }
            }
        });

        //  When click here will show the terms of user
        txttermos.setOnClickListener(v -> mostrartermos());
    }

    //  Create method to see the terms
    private void mostrartermos() {
        LottieAnimationView btnfechartermos;
        CardView cardviewaceitoostermos;
        termos.setContentView(R.layout.termoscfc);
        btnfechartermos = termos.findViewById(R.id.btnfechartermos);
        cardviewaceitoostermos = termos.findViewById(R.id.cardviewaceitoostermos);

        //  When click here will close the terms
        btnfechartermos.setOnClickListener(v -> termos.dismiss());

        //  When click here will accept the terms
        cardviewaceitoostermos.setOnClickListener(v -> {
            termos.dismiss();
            checkboxaceitoostermos.setChecked(true);
        });

        termos.show();
    }

    //  Create method to see the error
    private void mostrarerro(){
        CardView cardokavisoerro;
        avisoerro.setContentView(R.layout.aviso_erro_criarconta);
        cardokavisoerro = avisoerro.findViewById(R.id.cardokavisoerro);
        avisoerro.setCancelable(false);

        cardokavisoerro.setOnClickListener(v -> {
            Intent voltaramain = new Intent(CriarContaActivity.this,MainActivity.class);
            voltaramain.putExtra("novotimerstart",1);
            voltaramain.putExtra("novotimershowoption",200);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerda, R.anim.mover_direita);
            ActivityCompat.startActivity(CriarContaActivity.this,voltaramain, activityOptionsCompat.toBundle());
            finish();
        });

        avisoerro.show();
    }

    @Override
    public void onBackPressed() {
        Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
        startActivity(voltaraologin);
        finish();
    }
}