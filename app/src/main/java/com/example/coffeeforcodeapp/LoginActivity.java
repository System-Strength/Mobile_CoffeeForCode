package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;

public class LoginActivity extends AppCompatActivity {
    TextView txtcriarnovaconta, txtlogarlogin, txtenderecoemaillogin, txtenderecosenhalogin;
    LottieAnimationView btnvoltarlogin, animationloadinglogin;
    ImageView imgolhofechado, imgolhoaberto;
    CardView cardviewbtnlogar;
    EditText edittextemail, edittextsenha;
    Dialog avisoemailousenha;
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnvoltarlogin = findViewById(R.id.btnvoltarlogin);
        edittextemail = findViewById(R.id.edittextemail);
        edittextsenha = findViewById(R.id.edittextsenha);
        txtcriarnovaconta = findViewById(R.id.txtcriarnovaconta);
        imgolhofechado = findViewById(R.id.imgolhofechado);
        imgolhoaberto = findViewById(R.id.imgolhoaberto);
        cardviewbtnlogar = findViewById(R.id.cardviewbtnlogar);
        animationloadinglogin = findViewById(R.id.animationloadinglogin);
        txtlogarlogin = findViewById(R.id.txtlogarlogin);
        txtenderecoemaillogin = findViewById(R.id.txtenderecoemaillogin);
        txtenderecosenhalogin = findViewById(R.id.txtenderecosenhalogin);
        avisoemailousenha = new Dialog(this);
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set some thinks with gone
        imgolhoaberto.setVisibility(View.GONE);
        imgolhofechado.setVisibility(View.GONE);
        animationloadinglogin.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null){
            edittextemail.setText(null);
            edittextsenha.setText(null);
        }else {
            edittextemail.setText(bundle.getString("emailusu"));
            edittextsenha.setText(bundle.getString("senhausu"));
        }

        opcaodedevs();

        //  Set commands to start in real time
        edittextsenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittextsenha.getText() == null || edittextsenha.getText().length() == 0){
                    imgolhoaberto.setVisibility(View.GONE);
                    imgolhofechado.setVisibility(View.GONE);
                    edittextsenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    if (edittextsenha.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                        imgolhoaberto.setVisibility(View.VISIBLE);
                        imgolhofechado.setVisibility(View.GONE);
                    }else{
                        imgolhofechado.setVisibility(View.VISIBLE);
                        imgolhoaberto.setVisibility(View.GONE);
                    }
                }
            }
        });

        //  When click in the eye closed will hide de password
        imgolhofechado.setOnClickListener(v -> {
            edittextsenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgolhoaberto.setVisibility(View.VISIBLE);
            imgolhofechado.setVisibility(View.GONE);
            edittextsenha.setSelection(edittextsenha.getText().length());
        });

        //  When click in the eye opened will show de password
        imgolhoaberto.setOnClickListener(v -> {
            edittextsenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgolhoaberto.setVisibility(View.GONE);
            imgolhofechado.setVisibility(View.VISIBLE);
            edittextsenha.setSelection(edittextsenha.getText().length());
        });

        //  When click here will go back to MainActivity
        btnvoltarlogin.setOnClickListener(v -> {
            Intent voltaramain = new Intent(LoginActivity.this,MainActivity.class);
            voltaramain.putExtra("novotimerstart",200);
            voltaramain.putExtra("novotimershowoption",200);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerda, R.anim.mover_direita);
            ActivityCompat.startActivity(LoginActivity.this,voltaramain, activityOptionsCompat.toBundle());
            finish();
        });

        //  When click here will go to CriarContaActivity
        txtcriarnovaconta.setOnClickListener(v -> {
            Intent irparacriacaodeconta = new Intent(LoginActivity.this,CriarContaActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_para_cima, R.anim.mover_para_baixo);
            ActivityCompat.startActivity(LoginActivity.this,irparacriacaodeconta, activityOptionsCompat.toBundle());
            finish();
        });

        //  When click here will login and go to PrincipalActivity
        cardviewbtnlogar.setOnClickListener(v -> {
            if (edittextemail.getText() == null || edittextemail.getText().length() == 0){
                txtenderecoemaillogin.setTextColor(Color.RED);
                timer.postDelayed(() -> txtenderecoemaillogin.setTextColor(Color.BLACK),300);
                Toast.makeText(this, "Preencha corretamente: EMAIL", Toast.LENGTH_SHORT).show();
                edittextemail.requestFocus();
                imm.showSoftInput(edittextemail, InputMethodManager.SHOW_IMPLICIT);
            }else if (edittextsenha.getText() == null || edittextsenha.getText().length() == 0){
                txtenderecosenhalogin.setTextColor(Color.RED);
                timer.postDelayed(() -> txtenderecosenhalogin.setTextColor(Color.BLACK),300);
                Toast.makeText(this, "Preencha corretamente: SENHA", Toast.LENGTH_SHORT).show();
                edittextsenha.requestFocus();
                imm.showSoftInput(edittextsenha, InputMethodManager.SHOW_IMPLICIT);
            }else {
                DaoClientes daoClientes = new DaoClientes(LoginActivity.this);
                String email = edittextemail.getText().toString();
                String senha = edittextsenha.getText().toString();
                animationloadinglogin.setVisibility(View.VISIBLE);
                animationloadinglogin.playAnimation();
                txtlogarlogin.setVisibility(View.GONE);
                timer.postDelayed(() -> {
                    try {
                        boolean sucesso = daoClientes.onLogin(email,senha);
                        if (sucesso){
                            Intent irparaprincipal = new Intent(LoginActivity.this,PrincipalActivity.class);
                            irparaprincipal.putExtra("emailuser", email);
                            irparaprincipal.putExtra("statusavisoend", "ativado");
                            startActivity(irparaprincipal);
                            finish();
                        }else {
                            mostraravisoemailousenha();
                        }
                    }catch (Exception ex){
                        Toast.makeText(this, "Erro ao logar: "+ex, Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });

    }

    private void opcaodedevs() {
        AlertDialog.Builder avisoquemestausando = new AlertDialog.Builder(LoginActivity.this);
        avisoquemestausando.setTitle("Quem Esta usando?");
        avisoquemestausando.setPositiveButton("Kaua", (dialogInterface, i) -> {
            //  Dev Setting
            edittextemail.setText("kauavitorioof@gmail.com");
            edittextsenha.setText("kaua2004");
        });
        avisoquemestausando.setNeutralButton("Yuri", (dialogInterface, i) -> {
            //  Dev Setting
            edittextemail.setText("yuridantaassg@gmail.com");
            edittextsenha.setText("Yuridantas17");
        });

        avisoquemestausando.show();
    }

    //  Method to show Alert for email and password is wrong
    private void mostraravisoemailousenha(){
        CardView btnokavisoemailousenhaerrado;
        avisoemailousenha.setContentView(R.layout.aviso_emailousenhaerrodo);
        btnokavisoemailousenhaerrado = avisoemailousenha.findViewById(R.id.btnokavisoemailousenhaerrado);

        animationloadinglogin.setVisibility(View.GONE);
        animationloadinglogin.pauseAnimation();
        txtlogarlogin.setVisibility(View.VISIBLE);
        edittextsenha.setText(null);

        btnokavisoemailousenhaerrado.setOnClickListener(v -> avisoemailousenha.dismiss());

        avisoemailousenha.show();

    }

    @Override
    public void onBackPressed() {
        Intent voltaramain = new Intent(LoginActivity.this,MainActivity.class);
        voltaramain.putExtra("novotimerstart",1);
        voltaramain.putExtra("novotimershowoption",200);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerda, R.anim.mover_direita);
        ActivityCompat.startActivity(LoginActivity.this,voltaramain, activityOptionsCompat.toBundle());
        finish();
    }
}