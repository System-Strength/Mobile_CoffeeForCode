package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.User.DtoUsers;
import co.ex.coffeeforcodeapp.Api.User.UsersService;

import co.ex.coffeeforcodeapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {
    TextView txt_create_new_account, txtlogarlogin, txtForgoutPassword;
    LottieAnimationView cardviewbtnlogin, animation_loadingLogin;
    ImageView img_closed_eye, img_opened_eye;
    CardView cardviewbtnlogar, Card_base_login;
    EditText edittextEmail_userLogin, edittexPassword_userLogin;
    CheckBox checkbox_rememberMe;
    Dialog Warning_Email_Password;
    LoadingDialog loading = new LoadingDialog(LoginActivity.this);
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private FirebaseAuth mAuth;
    Dialog warning_emailnotverified;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getIds();
        warning_emailnotverified = new Dialog(this);
        Card_base_login.setElevation(30);
        cardviewbtnlogar.setElevation(20);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

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

        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        getPreferencesData();

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
        cardviewbtnlogin.setOnClickListener(v -> {
            Intent voltaramain = new Intent(LoginActivity.this, SplashActivity.class);
            voltaramain.putExtra("new_time_to_start",200);
            voltaramain.putExtra("new_time_to_showOptions",200);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
            ActivityCompat.startActivity(LoginActivity.this,voltaramain, activityOptionsCompat.toBundle());
            finish();
        });

        //  When click here will go to CriarContaActivity
        txt_create_new_account.setOnClickListener(v -> {
            Intent irparacriacaodeconta = new Intent(LoginActivity.this, CreateAccountActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_para_cima, R.anim.mover_para_baixo);
            ActivityCompat.startActivity(LoginActivity.this,irparacriacaodeconta, activityOptionsCompat.toBundle());
            finish();
        });

        //  When click here will login and go to PrincipalActivity
        cardviewbtnlogar.setOnClickListener(v -> {
            cardviewbtnlogar.setElevation(0);
            if (edittextEmail_userLogin.getText() == null || edittextEmail_userLogin.getText().length() == 0){
                edittextEmail_userLogin.setError("Fill in correctly: EMAIL" + "\n" + "Preencha corretamente: EMAIL");
                edittextEmail_userLogin.requestFocus();
                imm.showSoftInput(edittextEmail_userLogin, InputMethodManager.SHOW_IMPLICIT);
                cardviewbtnlogar.setElevation(20);
            }else if (edittexPassword_userLogin.getText() == null || edittexPassword_userLogin.getText().length() == 0){
                Toast.makeText(this, "Fill in correctly: PASSWOR!!\nPreencha corretamente: SENHA!!", Toast.LENGTH_SHORT).show();
                edittexPassword_userLogin.requestFocus();
                imm.showSoftInput(edittexPassword_userLogin, InputMethodManager.SHOW_IMPLICIT);
                cardviewbtnlogar.setElevation(20);
            }else {
                cardviewbtnlogar.setEnabled(false);
                String email = edittextEmail_userLogin.getText().toString();
                String password = edittexPassword_userLogin.getText().toString();
                animation_loadingLogin.setVisibility(View.VISIBLE);
                animation_loadingLogin.playAnimation();
                txtlogarlogin.setVisibility(View.GONE);
                DoLogin(email, password);
            }
        });

        //  When click here will to ForgotPasswordActivity
        txtForgoutPassword.setOnClickListener(v -> {
            Intent goTo_forgotPassword = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(goTo_forgotPassword);
            finish();
        });
    }

    private void DoLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (mAuth.getCurrentUser().isEmailVerified()){
                    UsersService usersService = retrofitUser.create(UsersService.class);
                    Call<DtoUsers> resultLogin = usersService.loginUser(email);

                    resultLogin.enqueue(new Callback<DtoUsers>() {
                        @Override
                        public void onResponse(@NotNull Call<DtoUsers> call, @NotNull Response<DtoUsers> response) {
                            if (response.code() == 200){
                                assert response.body() != null;
                                int id_user, partner;
                                String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;
                                id_user = response.body().getId_user();
                                nm_user = response.body().getNm_user();
                                email_user = response.body().getEmail();
                                phone_user = response.body().getPhone_user();
                                zipcode = response.body().getZipcode();
                                address_user = response.body().getAddress_user();
                                complement = response.body().getComplement();
                                img_user = response.body().getImg_user();
                                cpf_user = response.body().getCpf_user();
                                partner = response.body().getPartner();
                                partner_Startdate = response.body().getPartner_Startdate();

                                if (checkbox_rememberMe.isChecked()){
                                    mPrefs.edit().clear().apply();
                                    boolean boollsChecked = checkbox_rememberMe.isChecked();
                                    SharedPreferences.Editor editor = mPrefs.edit();
                                    editor.putString("pref_email", email);
                                    editor.putString("pref_password", password);
                                    editor.putBoolean("pref_check", boollsChecked);
                                    editor.apply();
                                    GoToMain_Intent(id_user, nm_user,email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner, partner_Startdate);
                                }else{
                                    mPrefs.edit().clear().apply();
                                    GoToMain_Intent(id_user, nm_user,email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner, partner_Startdate);
                                }
                            }else if(response.code() == 401){
                                ShowWarning_Email_Password();
                                mPrefs.edit().clear().apply();
                                animation_loadingLogin.setVisibility(View.GONE);
                                animation_loadingLogin.playAnimation();
                                txtlogarlogin.setVisibility(View.VISIBLE);
                                cardviewbtnlogar.setElevation(20);
                            }else{
                                Toast.makeText(LoginActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                                loading.dimissDialog();
                                mPrefs.edit().clear().apply();
                                Log.d("NetWorkError", response.message());
                                animation_loadingLogin.setVisibility(View.GONE);
                                animation_loadingLogin.playAnimation();
                                txtlogarlogin.setVisibility(View.VISIBLE);
                                cardviewbtnlogar.setElevation(20);
                                cardviewbtnlogar.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<DtoUsers> call, @NotNull Throwable t) {
                            Toast.makeText(LoginActivity.this, R.string.ApplicationErrorTryLater, Toast.LENGTH_SHORT).show();
                            Log.d("NetWorkError", t.getMessage());
                            loading.dimissDialog();
                            cardviewbtnlogar.setElevation(20);
                        }
                    });

                }else{
                    ShowEmailisNotVerified();
                }
            }else{
                ShowWarning_Email_Password();
            }
        });
    }

    //  Create Method for show alert of email not verified
    private void ShowEmailisNotVerified() {
        animation_loadingLogin.setVisibility(View.GONE);
        animation_loadingLogin.playAnimation();
        txtlogarlogin.setVisibility(View.VISIBLE);
        cardviewbtnlogar.setEnabled(true);
        cardviewbtnlogar.setElevation(20);

        CardView btnIwillConfirmLogin;
        warning_emailnotverified.setContentView(R.layout.adapter_emailnotverified);
        warning_emailnotverified.setCancelable(false);
        btnIwillConfirmLogin = warning_emailnotverified.findViewById(R.id.btnIwillConfirmLogin);

        btnIwillConfirmLogin.setOnClickListener(v -> warning_emailnotverified.dismiss());
        warning_emailnotverified.show();
    }

    private void getIds() {
        cardviewbtnlogin = findViewById(R.id.btnvoltarlogin);
        edittextEmail_userLogin = findViewById(R.id.edittextEmail_userLogin);
        edittexPassword_userLogin = findViewById(R.id.edittexPassword_userLogin);
        checkbox_rememberMe = findViewById(R.id.checkbox_rememberMe);
        txt_create_new_account = findViewById(R.id.txt_create_new_account);
        txtForgoutPassword = findViewById(R.id.txtForgoutPassword);
        img_closed_eye = findViewById(R.id.img_closed_eye);
        img_opened_eye = findViewById(R.id.img_opened_eye);
        cardviewbtnlogar = findViewById(R.id.cardviewbtnlogin);
        animation_loadingLogin = findViewById(R.id.animationloadinglogin);
        txtlogarlogin = findViewById(R.id.txtlogarlogin);
        Card_base_login = findViewById(R.id.Card_base_login);
        Warning_Email_Password = new Dialog(this);
    }

    private void getPreferencesData() {
        loading.startLoading();
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email") && sp.contains("pref_password")){
            String emailPref = sp.getString("pref_email", "not found");
            String PassPref = sp.getString("pref_password", "not found");
            checkbox_rememberMe.setChecked(sp.getBoolean("pref_check", true));
            DoLogin(emailPref, PassPref);
        }else {
            loading.dimissDialog();
        }
    }

    private void GoToMain_Intent(int id_user, String nm_user, String email_user, String phone_user, String zipcode, String address_user, String complement, String img_user, String cpf_user, int partner, String partner_Startdate) {
        Intent GoTo_Main = new Intent(LoginActivity.this, MainActivity.class);
        GoTo_Main.putExtra("id_user", id_user);
        GoTo_Main.putExtra("nm_user", nm_user);
        GoTo_Main.putExtra("email_user", email_user);
        GoTo_Main.putExtra("phone_user", phone_user);
        GoTo_Main.putExtra("zipcode", zipcode);
        GoTo_Main.putExtra("address_user", address_user);
        GoTo_Main.putExtra("complement", complement);
        GoTo_Main.putExtra("img_user", img_user);
        GoTo_Main.putExtra("cpf_user", cpf_user);
        GoTo_Main.putExtra("partner", partner);
        GoTo_Main.putExtra("partner_Startdate", partner_Startdate);
        GoTo_Main.putExtra("statusavisoend", "ativado");
        startActivity(GoTo_Main);
        finish();
    }

    //  Method to show Alert for email and password is wrong
    private void ShowWarning_Email_Password(){
        CardView btnokavisoemailousenhaerrado;
        Warning_Email_Password.setContentView(R.layout.aviso_emailousenhaerrodo);
        btnokavisoemailousenhaerrado = Warning_Email_Password.findViewById(R.id.btnokavisoemailousenhaerrado);
        cardviewbtnlogar.setElevation(20);

        animation_loadingLogin.setVisibility(View.GONE);
        animation_loadingLogin.pauseAnimation();
        txtlogarlogin.setVisibility(View.VISIBLE);
        edittexPassword_userLogin.setText(null);
        cardviewbtnlogar.setEnabled(true);

        loading.dimissDialog();

        btnokavisoemailousenhaerrado.setOnClickListener(v -> Warning_Email_Password.dismiss());

        Warning_Email_Password.show();

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