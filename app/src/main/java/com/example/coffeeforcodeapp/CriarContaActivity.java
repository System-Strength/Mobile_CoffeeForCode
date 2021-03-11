package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class CriarContaActivity extends AppCompatActivity {
    TextView txtLogin, txtTerms;
    CheckBox checkboxaceitoostermos;
    EditText edittextName_userCreateAccount, edittextcpf_userCreateAccount, edittextEmail_userCreateAccount, edittextPassword_userCreateAccount;
    ImageView imgolhofechadocriarconta, imgolhoabertocriarconta;
    LottieAnimationView btnvoltarcriarconta, certosenhacriarconta;
    CardView cardviewbtncriarconta;
    Dialog termos, avisoerro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);
        btnvoltarcriarconta = findViewById(R.id.btnvoltarcriarconta);
        certosenhacriarconta = findViewById(R.id.certosenhacriarconta);
        cardviewbtncriarconta = findViewById(R.id.cardviewbtncriarconta);
        imgolhofechadocriarconta = findViewById(R.id.imgolhofechadocriarconta);
        imgolhoabertocriarconta = findViewById(R.id.imgolhoabertocriarconta);
        edittextPassword_userCreateAccount = findViewById(R.id.edittextPassword_userCreateAccount);
        edittextEmail_userCreateAccount = findViewById(R.id.edittextEmail_userCreateAccount);
        edittextName_userCreateAccount = findViewById(R.id.edittextName_userCreateAccount);
        edittextcpf_userCreateAccount = findViewById(R.id.edittextcpf_userCreateAccount);
        checkboxaceitoostermos = findViewById(R.id.checkboxaceitoostermos);
        txtTerms = findViewById(R.id.txtTerms);
        txtLogin = findViewById(R.id.txtLogin);
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
        edittextcpf_userCreateAccount.addTextChangedListener(MaskEditUtil.mask(edittextcpf_userCreateAccount, MaskEditUtil.FORMAT_CPF));

        //  Set some thinks with gone
        certosenhacriarconta.setVisibility(View.GONE);
        imgolhofechadocriarconta.setVisibility(View.GONE);
        imgolhoabertocriarconta.setVisibility(View.GONE);

        //  When click in the eye closed will hide de password
        imgolhofechadocriarconta.setOnClickListener(v -> {
            edittextPassword_userCreateAccount.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgolhoabertocriarconta.setVisibility(View.VISIBLE);
            imgolhofechadocriarconta.setVisibility(View.GONE);
            edittextPassword_userCreateAccount.setSelection(edittextPassword_userCreateAccount.getText().length());
        });

        //  When click in the eye opened will show de password
        imgolhoabertocriarconta.setOnClickListener(v -> {
            edittextPassword_userCreateAccount.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgolhoabertocriarconta.setVisibility(View.GONE);
            imgolhofechadocriarconta.setVisibility(View.VISIBLE);
            edittextPassword_userCreateAccount.setSelection(edittextPassword_userCreateAccount.getText().length());
        });

        //  When click here will go back to LoginActivity
        btnvoltarcriarconta.setOnClickListener(v -> {
            Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
            startActivity(voltaraologin);
            finish();
        });

        //  When click here will try to create a new account
        cardviewbtncriarconta.setOnClickListener(v -> {
            if (edittextName_userCreateAccount.getText() == null || edittextName_userCreateAccount.getText().length() < 4){
                edittextName_userCreateAccount.setError("Necessary to fill in the field correctly: NAME" + "\n" +
                        "Necessário preencher corretamente o campo: NOME");
                //Toast.makeText(this, R.string.necessary_fill_name, Toast.LENGTH_SHORT).show();
                edittextName_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextName_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);

            }else if(edittextcpf_userCreateAccount.getText() == null || edittextcpf_userCreateAccount.getText().length() < 14){
                edittextcpf_userCreateAccount.setError("CPF informed is invalid" + "\n"  + "CPF informado é invalido");
                //Toast.makeText(this, R.string.cpf_informed_is_invalid, Toast.LENGTH_SHORT).show();
                edittextcpf_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextcpf_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);

            }else if(edittextEmail_userCreateAccount.getText() == null || edittextEmail_userCreateAccount.getText().length() == 0){
                edittextEmail_userCreateAccount.setError("Required to fill in the field: EMAIL" + "\n" + "Necessário preencher o campo: EMAIL");
                //Toast.makeText(this, R.string.required_fill_email, Toast.LENGTH_SHORT).show();
                edittextEmail_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextEmail_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);

            }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edittextEmail_userCreateAccount.getText()).matches()){
                edittextEmail_userCreateAccount.setError("Fill in your email correctly" + "\n" + "Preencha corretamente seu email");
                //Toast.makeText(this, R.string.fill_email_correctly, Toast.LENGTH_SHORT).show();
                edittextEmail_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextEmail_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);

            }else if (edittextPassword_userCreateAccount.getText() == null || edittextPassword_userCreateAccount.getText().length() < 8){
                edittextPassword_userCreateAccount.setError("Necessary to fill the field correctly: PASSWORD\n" +
                        "Minimum 8 characters" + "\n" + "Necessário preencher corretamente o campo: SENHA\n" +  "Minimo 8 caracteres");
                //Toast.makeText(this, R.string.necessary_fill_correctly_password, Toast.LENGTH_SHORT).show();
                edittextPassword_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextPassword_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);

            }else if(!checkboxaceitoostermos.isChecked()){
                Toast.makeText(this, "Necessario aceitar os termos", Toast.LENGTH_SHORT).show();
            }else {
                String email = edittextEmail_userCreateAccount.getText().toString();
                String cpf_user = edittextcpf_userCreateAccount.getText().toString();
                String password = edittextPassword_userCreateAccount.getText().toString();
                String nm_user = edittextName_userCreateAccount.getText().toString();
                UsersService usersService = retrofitUser.create(UsersService.class);
                DtoUsers newuser = new DtoUsers(email, nm_user, cpf_user, password);
                Call<DtoUsers> clientesCall = usersService.registerNewUse(newuser);
                loadingDialog.startLoading();

                clientesCall.enqueue(new Callback<DtoUsers>() {
                    @Override
                    public void onResponse(Call<DtoUsers> call, Response<DtoUsers> response) {
                        if(response.code() == 201 || response.code() == 200){
                            Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
                            voltaraologin.putExtra("email_user", email);
                            voltaraologin.putExtra("password_user", password);
                            startActivity(voltaraologin);
                            finish();
                        }
                        else if (response.code() == 409){
                            loadingDialog.dimissDialog();
                            showError();
                        }else{
                            Toast.makeText(CriarContaActivity.this, R.string.wehaveaproblem, Toast.LENGTH_LONG).show();
                            loadingDialog.dimissDialog();
                        }
                    }
                    @Override
                    public void onFailure(Call<DtoUsers> call, Throwable t) {
                        Toast.makeText(CriarContaActivity.this, R.string.ApplicationErrorTryLater, Toast.LENGTH_LONG).show();
                        loadingDialog.dimissDialog();
                    }
                });
            }
        });

        //  When click will go back to LoginActivity
        txtLogin.setOnClickListener(v -> {
            Intent voltaraologin = new Intent(CriarContaActivity.this, LoginActivity.class);
            startActivity(voltaraologin);
            finish();
        });

        //  Set commands to start in real time
        edittextPassword_userCreateAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittextPassword_userCreateAccount.getText() == null || edittextPassword_userCreateAccount.getText().length() == 0){
                    imgolhoabertocriarconta.setVisibility(View.GONE);
                    imgolhofechadocriarconta.setVisibility(View.GONE);
                    edittextPassword_userCreateAccount.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    if (edittextPassword_userCreateAccount.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                        imgolhoabertocriarconta.setVisibility(View.VISIBLE);
                        imgolhofechadocriarconta.setVisibility(View.GONE);
                    }else{
                        imgolhofechadocriarconta.setVisibility(View.VISIBLE);
                        imgolhoabertocriarconta.setVisibility(View.GONE);
                    }
                }
                if (edittextPassword_userCreateAccount.getText().length() >= 8){
                    certosenhacriarconta.setVisibility(View.VISIBLE);
                    certosenhacriarconta.playAnimation();
                }else {
                    certosenhacriarconta.setVisibility(View.GONE);
                }
            }
        });

        //  When click here will show the terms of user
        txtTerms.setOnClickListener(v -> ShowTerms());
    }

    //  Create method to see the terms
    private void ShowTerms() {
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
            Intent voltaramain = new Intent(CriarContaActivity.this, SplashActivity.class);
            voltaramain.putExtra("new_time_to_start",1);
            voltaramain.putExtra("new_time_to_showOptions",200);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
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