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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.Api.DtoUsers;
import com.example.coffeeforcodeapp.Api.UsersService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {
    TextView txtcriarnovaconta, txtlogarlogin, txtenderecoemaillogin, txtenderecosenhalogin;
    LottieAnimationView btnvoltarlogin, animation_loadingLogin;
    ImageView img_closed_eye, img_opened_eye;
    CardView cardviewbtnlogar;
    EditText edittextEmail_userLogin, edittexPassword_userLogin;
    Dialog avisoemailousenha;
    Handler timer = new Handler();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnvoltarlogin = findViewById(R.id.btnvoltarlogin);
        edittextEmail_userLogin = findViewById(R.id.edittextEmail_userLogin);
        edittexPassword_userLogin = findViewById(R.id.edittexPassword_userLogin);
        txtcriarnovaconta = findViewById(R.id.txtcriarnovaconta);
        img_closed_eye = findViewById(R.id.img_closed_eye);
        img_opened_eye = findViewById(R.id.img_opened_eye);
        cardviewbtnlogar = findViewById(R.id.cardviewbtnlogar);
        animation_loadingLogin = findViewById(R.id.animationloadinglogin);
        txtlogarlogin = findViewById(R.id.txtlogarlogin);
        txtenderecoemaillogin = findViewById(R.id.txtenderecoemaillogin);
        txtenderecosenhalogin = findViewById(R.id.txtenderecosenhalogin);
        avisoemailousenha = new Dialog(this);
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final Retrofit retrofitUser = new Retrofit.Builder()
                .baseUrl("https://coffeeforcode.herokuapp.com/user/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //  Set some thinks with gone
        img_opened_eye.setVisibility(View.GONE);
        img_closed_eye.setVisibility(View.GONE);
        animation_loadingLogin.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null){
            edittextEmail_userLogin.setText(null);
            edittexPassword_userLogin.setText(null);
        }else {
            edittextEmail_userLogin.setText(bundle.getString("email_user"));
            edittexPassword_userLogin.setText(bundle.getString("password_user"));
        }

        DevOptions();

        //  Set commands to start in real time
        edittexPassword_userLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittexPassword_userLogin.getText() == null || edittexPassword_userLogin.getText().length() == 0){
                    img_opened_eye.setVisibility(View.GONE);
                    img_closed_eye.setVisibility(View.GONE);
                    edittexPassword_userLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    if (edittexPassword_userLogin.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                        img_opened_eye.setVisibility(View.VISIBLE);
                        img_closed_eye.setVisibility(View.GONE);
                    }else{
                        img_closed_eye.setVisibility(View.VISIBLE);
                        img_opened_eye.setVisibility(View.GONE);
                    }
                }
            }
        });

        //  When click in the eye closed will hide de password
        img_closed_eye.setOnClickListener(v -> {
            edittexPassword_userLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            img_opened_eye.setVisibility(View.VISIBLE);
            img_closed_eye.setVisibility(View.GONE);
            edittexPassword_userLogin.setSelection(edittexPassword_userLogin.getText().length());
        });

        //  When click in the eye opened will show de password
        img_opened_eye.setOnClickListener(v -> {
            edittexPassword_userLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            img_opened_eye.setVisibility(View.GONE);
            img_closed_eye.setVisibility(View.VISIBLE);
            edittexPassword_userLogin.setSelection(edittexPassword_userLogin.getText().length());
        });

        //  When click here will go back to MainActivity
        btnvoltarlogin.setOnClickListener(v -> {
            Intent voltaramain = new Intent(LoginActivity.this, SplashActivity.class);
            voltaramain.putExtra("new_time_to_start",200);
            voltaramain.putExtra("new_time_to_showOptions",200);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
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
            if (edittextEmail_userLogin.getText() == null || edittextEmail_userLogin.getText().length() == 0){
                txtenderecoemaillogin.setTextColor(Color.RED);
                timer.postDelayed(() -> txtenderecoemaillogin.setTextColor(Color.BLACK),300);
                edittextEmail_userLogin.setError("Fill in correctly: EMAIL" + "\n" + "Preencha corretamente: EMAIL");
                //Toast.makeText(this, R.string.fill_correctly_email, Toast.LENGTH_SHORT).show();
                edittextEmail_userLogin.requestFocus();
                imm.showSoftInput(edittextEmail_userLogin, InputMethodManager.SHOW_IMPLICIT);
            }else if (edittexPassword_userLogin.getText() == null || edittexPassword_userLogin.getText().length() == 0){
                txtenderecosenhalogin.setTextColor(Color.RED);
                timer.postDelayed(() -> txtenderecosenhalogin.setTextColor(Color.BLACK),300);
                Toast.makeText(this, "Preencha corretamente: SENHA", Toast.LENGTH_SHORT).show();
                edittexPassword_userLogin.requestFocus();
                imm.showSoftInput(edittexPassword_userLogin, InputMethodManager.SHOW_IMPLICIT);
            }else {
                String email = edittextEmail_userLogin.getText().toString();
                String password = edittexPassword_userLogin.getText().toString();
                animation_loadingLogin.setVisibility(View.VISIBLE);
                animation_loadingLogin.playAnimation();
                txtlogarlogin.setVisibility(View.GONE);
                UsersService usersService = retrofitUser.create(UsersService.class);
                Call<DtoUsers> resultLogin = usersService.loginUser(email, password);

                resultLogin.enqueue(new Callback<DtoUsers>() {
                    @Override
                    public void onResponse(Call<DtoUsers> call, Response<DtoUsers> response) {
                        if (response.code() == 200){
                            Intent GoTo_Main = new Intent(LoginActivity.this, MainActivity.class);
                            assert response.body() != null;
                            GoTo_Main.putExtra("id_user", response.body().getId_user());
                            GoTo_Main.putExtra("email_user", response.body().getEmail());
                            GoTo_Main.putExtra("phone_user", response.body().getPhone_user());
                            GoTo_Main.putExtra("rg_user", response.body().getRg_user());
                            startActivity(GoTo_Main);
                            finish();
                        }else if(response.code() == 401){;
                            ShowWarning_Email_Password();
                        }else{
                            Toast.makeText(LoginActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                            Log.d("NetWorkError", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<DtoUsers> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, R.string.ApplicationErrorTryLater, Toast.LENGTH_SHORT).show();
                        Log.d("NetWorkError", t.getMessage());

                    }
                });
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void DevOptions() {
        AlertDialog.Builder Warning_who_is_using = new AlertDialog.Builder(LoginActivity.this);
        Warning_who_is_using.setTitle("Quem Esta usando?");
        Warning_who_is_using.setPositiveButton("Kaua", (dialogInterface, i) -> {
            //  Dev Login
            edittextEmail_userLogin.setText("kauavitorioof@gmail.com");
            edittexPassword_userLogin.setText("@!Kaua2004");
        });
        Warning_who_is_using.setNeutralButton("Yuri", (dialogInterface, i) -> {
            //  Dev Login
            edittextEmail_userLogin.setText("yuridantaassg@gmail.com");
            edittexPassword_userLogin.setText("Yuridantas17");
        });

        Warning_who_is_using.setNegativeButton("Test", (dialog, which) -> {
            //  Dev Login
            edittextEmail_userLogin.setText("test123456@gmail.com");
            edittexPassword_userLogin.setText("12345678");
        });

        Warning_who_is_using.show();
    }

    //  Method to show Alert for email and password is wrong
    private void ShowWarning_Email_Password(){
        CardView btnokavisoemailousenhaerrado;
        avisoemailousenha.setContentView(R.layout.aviso_emailousenhaerrodo);
        btnokavisoemailousenhaerrado = avisoemailousenha.findViewById(R.id.btnokavisoemailousenhaerrado);

        animation_loadingLogin.setVisibility(View.GONE);
        animation_loadingLogin.pauseAnimation();
        txtlogarlogin.setVisibility(View.VISIBLE);
        edittexPassword_userLogin.setText(null);

        btnokavisoemailousenhaerrado.setOnClickListener(v -> avisoemailousenha.dismiss());

        avisoemailousenha.show();

    }

    @Override
    public void onBackPressed() {
        Intent goBack_to_main = new Intent(LoginActivity.this, SplashActivity.class);
        goBack_to_main.putExtra("new_time_to_start",1);
        goBack_to_main.putExtra("new_time_to_showOptions",200);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
        ActivityCompat.startActivity(LoginActivity.this,goBack_to_main, activityOptionsCompat.toBundle());
        finish();
    }
}