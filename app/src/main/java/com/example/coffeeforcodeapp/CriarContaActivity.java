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
import com.example.coffeeforcodeapp.Api.DtoUsers;
import com.example.coffeeforcodeapp.Api.UsersService;
import com.example.coffeeforcodeapp.LocalDataBases.Clientes.DtoClientes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class CriarContaActivity extends AppCompatActivity {
    TextView txtlogar, txttermos;
    CheckBox checkboxaceitoostermos;
    EditText edittextNameCreateAccount, edittextrg_userCreateAccount, edittextemailcriarconta, edittextPasswordCriarcontacriarconta;
    ImageView imgolhofechadocriarconta, imgolhoabertocriarconta;
    LottieAnimationView btnvoltarcriarconta, certosenhacriarconta;
    CardView cardviewbtncriarconta;
    DtoClientes clientes = new DtoClientes();
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
        edittextPasswordCriarcontacriarconta = findViewById(R.id.edittextPasswordCriarconta);
        edittextemailcriarconta = findViewById(R.id.edittextemailcriarconta);
        edittextNameCreateAccount = findViewById(R.id.edittextNameCreateAccount);
        edittextrg_userCreateAccount = findViewById(R.id.edittextrg_userCreateAccount);
        checkboxaceitoostermos = findViewById(R.id.checkboxaceitoostermos);
        txttermos = findViewById(R.id.txttermos);
        txtlogar = findViewById(R.id.txtlogar);
        termos = new Dialog(this);
        avisoerro = new Dialog(this);
        LoadingDialog loadingDialog = new LoadingDialog(CriarContaActivity.this);
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final Retrofit retrofitUser = new Retrofit.Builder()
                .baseUrl("https://coffeeforcode.herokuapp.com/user/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //  Set Mask
        edittextrg_userCreateAccount.addTextChangedListener(MaskEditUtil.mask(edittextrg_userCreateAccount, MaskEditUtil.FORMAT_RG));

        //  Set some thinks with gone
        certosenhacriarconta.setVisibility(View.GONE);
        imgolhofechadocriarconta.setVisibility(View.GONE);
        imgolhoabertocriarconta.setVisibility(View.GONE);

        //  When click in the eye closed will hide de password
        imgolhofechadocriarconta.setOnClickListener(v -> {
            edittextPasswordCriarcontacriarconta.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgolhoabertocriarconta.setVisibility(View.VISIBLE);
            imgolhofechadocriarconta.setVisibility(View.GONE);
            edittextPasswordCriarcontacriarconta.setSelection(edittextPasswordCriarcontacriarconta.getText().length());
        });

        //  When click in the eye opened will show de password
        imgolhoabertocriarconta.setOnClickListener(v -> {
            edittextPasswordCriarcontacriarconta.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgolhoabertocriarconta.setVisibility(View.GONE);
            imgolhofechadocriarconta.setVisibility(View.VISIBLE);
            edittextPasswordCriarcontacriarconta.setSelection(edittextPasswordCriarcontacriarconta.getText().length());
        });

        //  When click here will go back to LoginActivity
        btnvoltarcriarconta.setOnClickListener(v -> {
            Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
            startActivity(voltaraologin);
            finish();
        });

        //  When click here will try to create a new account
        cardviewbtncriarconta.setOnClickListener(v -> {
            if (edittextNameCreateAccount.getText() == null || edittextNameCreateAccount.getText().length() < 4){
                Toast.makeText(this, "Necessário preencher corretamente o campo: NOME", Toast.LENGTH_SHORT).show();
                edittextNameCreateAccount.requestFocus();
                imm.showSoftInput(edittextNameCreateAccount, InputMethodManager.SHOW_IMPLICIT);

            }else if(edittextrg_userCreateAccount.getText() == null || edittextrg_userCreateAccount.getText().length() < 12){
                Toast.makeText(this, "CPF informado é invalido", Toast.LENGTH_SHORT).show();
                edittextrg_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextrg_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);

            }else if(edittextemailcriarconta.getText() == null || edittextemailcriarconta.getText().length() == 0){
                Toast.makeText(this, "Necessário preencher o campo: EMAIL", Toast.LENGTH_SHORT).show();
                edittextemailcriarconta.requestFocus();
                imm.showSoftInput(edittextemailcriarconta, InputMethodManager.SHOW_IMPLICIT);

            }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edittextemailcriarconta.getText()).matches()){
                Toast.makeText(this, "Preencha corretamente seu email", Toast.LENGTH_SHORT).show();
                edittextemailcriarconta.requestFocus();
                imm.showSoftInput(edittextemailcriarconta, InputMethodManager.SHOW_IMPLICIT);

            }else if (edittextPasswordCriarcontacriarconta.getText() == null || edittextPasswordCriarcontacriarconta.getText().length() < 8){
                Toast.makeText(this, "Necessário preencher corretamente o campo: SENHA\nMinimo 8 caracteres", Toast.LENGTH_SHORT).show();
                edittextPasswordCriarcontacriarconta.requestFocus();
                imm.showSoftInput(edittextPasswordCriarcontacriarconta, InputMethodManager.SHOW_IMPLICIT);

            }else if(!checkboxaceitoostermos.isChecked()){
                Toast.makeText(this, "Necessario aceitar os termos", Toast.LENGTH_SHORT).show();
            }else {
                String email = edittextemailcriarconta.getText().toString();
                String rg_user = edittextrg_userCreateAccount.getText().toString();
                String password = edittextPasswordCriarcontacriarconta.getText().toString();
                String nm_user = edittextNameCreateAccount.getText().toString();
                UsersService usersService = retrofitUser.create(UsersService.class);
                DtoUsers newuser = new DtoUsers(email, nm_user, rg_user, password);
                Call<DtoUsers> clientesCall = usersService.registerNewUse(newuser);
                loadingDialog.startLoading();

                clientesCall.enqueue(new Callback<DtoUsers>() {
                    @Override
                    public void onResponse(Call<DtoUsers> call, Response<DtoUsers> response) {
                        if(response.code() == 201 || response.code() == 200){
                            Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
                            voltaraologin.putExtra("emailusu", email);
                            voltaraologin.putExtra("senhausu", password);
                            startActivity(voltaraologin);
                            finish();
                        }
                        else if (response.code() == 409){
                            loadingDialog.dimissDialog();
                            showError();
                        }else{
                            loadingDialog.dimissDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<DtoUsers> call, Throwable t) {

                    }
                });

                /*String emailsendocadastrado = edittextemailcriarconta.getText().toString();
                DaoClientes clientesconsultados = new DaoClientes(CriarContaActivity.this);
                clientes = clientesconsultados.consultarclienteporemail(emailsendocadastrado);
                if (clientes.getEmailcliente() == null){
                    if (edittextemailcriarconta.getText().toString().equals("kauavitorioof@gmail.com")){
                        DtoClientes dtoClientes = new DtoClientes();
                        dtoClientes.setNomecliente(edittextNameCreateAccount.getText().toString());
                        dtoClientes.setCpfcliente(edittextrg_userCreateAccount.getText().toString());
                        dtoClientes.setEmailcliente(edittextemailcriarconta.getText().toString());
                        dtoClientes.setParceiro("nao");
                        dtoClientes.setAdm("SIM");
                        dtoClientes.setSenhacliente(edittextPasswordCriarcontacriarconta.getText().toString());
                        DaoClientes daoClientes = new DaoClientes(CriarContaActivity.this);
                        timer.postDelayed(() -> {
                            try {
                                long linhasafetadas = daoClientes.cadastrar(dtoClientes);
                                if (linhasafetadas > 0){
                                    Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
                                    voltaraologin.putExtra("emailusu",edittextemailcriarconta.getText().toString());
                                    voltaraologin.putExtra("senhausu",edittextPasswordCriarcontacriarconta.getText().toString());
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

                    }else {
                        DtoClientes dtoClientes = new DtoClientes();
                        dtoClientes.setNomecliente(edittextNameCreateAccount.getText().toString());
                        dtoClientes.setCpfcliente(edittextrg_userCreateAccount.getText().toString());
                        dtoClientes.setEmailcliente(edittextemailcriarconta.getText().toString());
                        dtoClientes.setParceiro("nao");
                        dtoClientes.setAdm("NAO");
                        dtoClientes.setSenhacliente(edittextPasswordCriarcontacriarconta.getText().toString());
                        DaoClientes daoClientes = new DaoClientes(CriarContaActivity.this);
                        loadingDialog.startLoading();
                        timer.postDelayed(() -> {
                            try {
                                long linhasafetadas = daoClientes.cadastrar(dtoClientes);
                                if (linhasafetadas > 0) {
                                    Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
                                    voltaraologin.putExtra("emailusu", edittextemailcriarconta.getText().toString());
                                    voltaraologin.putExtra("senhausu", edittextPasswordCriarcontacriarconta.getText().toString());
                                    startActivity(voltaraologin);
                                    finish();
                                } else {
                                    loadingDialog.dimissDialog();
                                    mostrarerro();
                                }
                            } catch (Exception ex) {
                                Toast.makeText(this, "Erro ao criar conta: " + ex, Toast.LENGTH_SHORT).show();
                            }
                        }, 2000);

                    }
                } else if (clientes.getEmailcliente().equals(edittextemailcriarconta.getText().toString())){
                    Toast.makeText(this, "O email informado já está em uso!!", Toast.LENGTH_SHORT).show();
                    edittextemailcriarconta.requestFocus();
                    imm.showSoftInput(edittextemailcriarconta, InputMethodManager.SHOW_IMPLICIT);
                }*/
            }
        });

        //  When click will go back to LoginActivity
        txtlogar.setOnClickListener(v -> {
            Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
            startActivity(voltaraologin);
            finish();
        });

        //  Set commands to start in real time
        edittextPasswordCriarcontacriarconta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittextPasswordCriarcontacriarconta.getText() == null || edittextPasswordCriarcontacriarconta.getText().length() == 0){
                    imgolhoabertocriarconta.setVisibility(View.GONE);
                    imgolhofechadocriarconta.setVisibility(View.GONE);
                    edittextPasswordCriarcontacriarconta.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    if (edittextPasswordCriarcontacriarconta.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                        imgolhoabertocriarconta.setVisibility(View.VISIBLE);
                        imgolhofechadocriarconta.setVisibility(View.GONE);
                    }else{
                        imgolhofechadocriarconta.setVisibility(View.VISIBLE);
                        imgolhoabertocriarconta.setVisibility(View.GONE);
                    }
                }
                if (edittextPasswordCriarcontacriarconta.getText().length() >= 8){
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
    private void showError(){
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