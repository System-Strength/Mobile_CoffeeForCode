package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class LoginActivity extends AppCompatActivity {
    TextView txtcriarnovaconta;
    LottieAnimationView btnvoltarlogin;
    ImageView imgolhofechado, imgolhoaberto;
    CardView cardviewbtnlogar;
    EditText edittextemail, edittextsenha;

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

        //  Set some thinks with gone
        imgolhoaberto.setVisibility(View.GONE);
        imgolhofechado.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null){
            edittextemail.setText(null);
            edittextsenha.setText(null);
        }else {
            edittextemail.setText(bundle.getString("emailusu"));
            edittextsenha.setText(bundle.getString("senhausu"));
        }

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

        cardviewbtnlogar.setOnClickListener(v -> {
            //  Soon
        });

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