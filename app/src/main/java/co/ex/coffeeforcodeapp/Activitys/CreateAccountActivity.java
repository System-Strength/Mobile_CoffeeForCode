package co.ex.coffeeforcodeapp.Activitys;

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
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.Products.DtoMenuById;
import co.ex.coffeeforcodeapp.Api.Products.MenuService;
import co.ex.coffeeforcodeapp.Api.User.DtoUsers;
import co.ex.coffeeforcodeapp.Api.User.UsersService;

import co.ex.coffeeforcodeapp.MaskEditUtil;
import co.ex.coffeeforcodeapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class CreateAccountActivity extends AppCompatActivity {
    TextView txtLogin, txtTerms;
    CheckBox checkboxaceitoostermos;
    EditText edittextFirstName_userCreateAccount, edittextLastName_userCreateAccount, edittextcpf_userCreateAccount, edittextEmail_userCreateAccount, edittextPassword_userCreateAccount;
    ImageView imgolhofechadocriarconta, imgolhoabertocriarconta;
    LottieAnimationView btnvoltarcriarconta, certosenhacriarconta;
    CardView card_btn_create_account;
    Dialog termos, avisoerro;
    private FirebaseAuth mAuth;
    Dialog warning_emailsend;
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final Retrofit retrofitPreLoad = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/products/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Ids();
        termos = new Dialog(this);
        avisoerro = new Dialog(this);
        warning_emailsend = new Dialog(this);
        LoadingDialog loadingDialog = new LoadingDialog(CreateAccountActivity.this);
        PreloadServer();
        card_btn_create_account.setElevation(20);


        // Initialize Firebase Auth
        FirebaseApp.initializeApp(CreateAccountActivity.this);
        mAuth = FirebaseAuth.getInstance();
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


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
            Intent voltaraologin = new Intent(CreateAccountActivity.this, LoginActivity.class);
            startActivity(voltaraologin);
            finish();
        });

        //  When click here will try to create a new account
        card_btn_create_account.setOnClickListener(v -> {
            card_btn_create_account.setElevation(0);
            if (edittextFirstName_userCreateAccount.getText() == null || edittextFirstName_userCreateAccount.getText().length() < 4){
                edittextFirstName_userCreateAccount.setError("Necessary to fill in the field correctly: FIRST NAME" + "\n" +
                        "Necessário preencher corretamente o campo: PRIMEIRO NOME");
                edittextFirstName_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextFirstName_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);
                card_btn_create_account.setElevation(20);
            }else if(edittextLastName_userCreateAccount.getText() == null || edittextLastName_userCreateAccount.getText().length() < 2){
                edittextLastName_userCreateAccount.setError("Necessary to fill in the field correctly: LAST NAME" + "\n" +
                        "Necessário preencher corretamente o campo: ULTIMO NOME");
                edittextLastName_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextLastName_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);
                card_btn_create_account.setElevation(20);
            }else if(edittextEmail_userCreateAccount.getText() == null || edittextEmail_userCreateAccount.getText().length() == 0){
                edittextEmail_userCreateAccount.setError("Required to fill in the field: EMAIL" + "\n" + "Necessário preencher o campo: EMAIL");
                edittextEmail_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextEmail_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);
                card_btn_create_account.setElevation(20);
            }else if (!Patterns.EMAIL_ADDRESS.matcher(edittextEmail_userCreateAccount.getText()).matches()){
                edittextEmail_userCreateAccount.setError("Fill in your email correctly" + "\n" + "Preencha corretamente seu email");
                //Toast.makeText(this, R.string.fill_email_correctly, Toast.LENGTH_SHORT).show();
                edittextEmail_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextEmail_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);
                card_btn_create_account.setElevation(20);
            }else if (edittextPassword_userCreateAccount.getText() == null || edittextPassword_userCreateAccount.getText().length() < 8){
                edittextPassword_userCreateAccount.setError("Necessary to fill the field correctly: PASSWORD\n" +
                        "Minimum 8 characters" + "\n" + "Necessário preencher corretamente o campo: SENHA\n" +  "Minimo 8 caracteres");
                //Toast.makeText(this, R.string.necessary_fill_correctly_password, Toast.LENGTH_SHORT).show();
                edittextPassword_userCreateAccount.requestFocus();
                imm.showSoftInput(edittextPassword_userCreateAccount, InputMethodManager.SHOW_IMPLICIT);
                card_btn_create_account.setElevation(20);
            }else if(!checkboxaceitoostermos.isChecked()){
                Toast.makeText(this, "Necessario aceitar os termos", Toast.LENGTH_SHORT).show();
                card_btn_create_account.setElevation(20);
            }else {
                String First_name = edittextFirstName_userCreateAccount.getText().toString();
                String Last_Name = edittextLastName_userCreateAccount.getText().toString();
                String nm_user = First_name + " " + Last_Name;
                String email = edittextEmail_userCreateAccount.getText().toString();
                String cpf_user = edittextcpf_userCreateAccount.getText().toString();
                String password = edittextPassword_userCreateAccount.getText().toString();
                loadingDialog.startLoading();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("StatusCreateAccount", "createUserWithEmail:success");
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        UsersService usersService = retrofitUser.create(UsersService.class);
                                        DtoUsers newuser = new DtoUsers(email, nm_user, cpf_user, password);
                                        Call<DtoUsers> clientesCall = usersService.registerNewUse(newuser);
                                        clientesCall.enqueue(new Callback<DtoUsers>() {
                                            @Override
                                            public void onResponse(@NotNull Call<DtoUsers> call, @NotNull Response<DtoUsers> response) {
                                                if(response.code() == 201 || response.code() == 200){
                                                    Log.d("EmailStatus", "Email sent.");
                                                    Show_WeSendEmail_Warning(email, password);
                                                    card_btn_create_account.setElevation(20);
                                                }
                                                else if (response.code() == 409){
                                                    loadingDialog.dimissDialog();
                                                    showError();
                                                    card_btn_create_account.setElevation(20);
                                                }else{
                                                    Toast.makeText(CreateAccountActivity.this, R.string.wehaveaproblem, Toast.LENGTH_LONG).show();
                                                    loadingDialog.dimissDialog();
                                                    card_btn_create_account.setElevation(20);
                                                }
                                            }
                                            @Override
                                            public void onFailure(@NotNull Call<DtoUsers> call, @NotNull Throwable t) {
                                                Toast.makeText(CreateAccountActivity.this, R.string.ApplicationErrorTryLater, Toast.LENGTH_LONG).show();
                                                loadingDialog.dimissDialog();
                                                card_btn_create_account.setElevation(20);
                                            }
                                        });
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("StatusCreateAccount", "createUserWithEmail:failure", task.getException());
                                loadingDialog.dimissDialog();
                                Toast.makeText(CreateAccountActivity.this, R.string.authfailed_thisemail,
                                        Toast.LENGTH_SHORT).show();
                                card_btn_create_account.setElevation(20);
                            }
                        });
            }
        });

        //  When click will go back to LoginActivity
        txtLogin.setOnClickListener(v -> {
            Intent voltaraologin = new Intent(CreateAccountActivity.this, LoginActivity.class);
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

    private void PreloadServer() {
        int cd_prod = 14;
        MenuService menuService = retrofitPreLoad.create(MenuService.class);
        Call<DtoMenuById> call = menuService.getProductByCd(cd_prod);
        call.enqueue(new Callback<DtoMenuById>() {
            @Override
            public void onResponse(@NotNull Call<DtoMenuById> call, @NotNull Response<DtoMenuById> response) {
                Log.d("PreLoadStatus", "Server ON");
            }
            @Override
            public void onFailure(@NotNull Call<DtoMenuById> call, @NotNull Throwable t) {
                //Toast.makeText(CreateAccountActivity.this, "We have a problem with our servers, please try again later !!\nEstamos com problema em nossos servidores tente novamente mais tarde!!", Toast.LENGTH_SHORT).show();
                Log.d("ServerError", t.getMessage());
            }
        });
    }

    private void Ids() {
        btnvoltarcriarconta = findViewById(R.id.btnvoltarcriarconta);
        certosenhacriarconta = findViewById(R.id.certosenhacriarconta);
        card_btn_create_account = findViewById(R.id.card_btn_create_account);
        imgolhofechadocriarconta = findViewById(R.id.imgolhofechadocriarconta);
        imgolhoabertocriarconta = findViewById(R.id.imgolhoabertocriarconta);
        edittextPassword_userCreateAccount = findViewById(R.id.edittextPassword_userCreateAccount);
        edittextEmail_userCreateAccount = findViewById(R.id.edittextEmail_userCreateAccount);
        edittextFirstName_userCreateAccount = findViewById(R.id.edittextFirstName_userCreateAccount);
        edittextLastName_userCreateAccount = findViewById(R.id.edittextLastName_userCreateAccount);
        edittextcpf_userCreateAccount = findViewById(R.id.edittextcpf_userCreateAccount);
        checkboxaceitoostermos = findViewById(R.id.checkboxaceitoostermos);
        txtTerms = findViewById(R.id.txtTerms);
        txtLogin = findViewById(R.id.txtLogin);
    }

    //  Create Method for show alert of email send
    private void Show_WeSendEmail_Warning(String email, String password){
        CardView btnIwillConfirm;
        warning_emailsend.setContentView(R.layout.adapter_wesendemail);
        warning_emailsend.setCancelable(false);
        btnIwillConfirm = warning_emailsend.findViewById(R.id.btnIwillConfirm);

        btnIwillConfirm.setOnClickListener(v -> {
            Intent voltaraologin = new Intent(CreateAccountActivity.this, LoginActivity.class);
            voltaraologin.putExtra("email_user", email);
            voltaraologin.putExtra("password_user", password);
            startActivity(voltaraologin);
            finish();
        });
        warning_emailsend.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            onRestart();
        }
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
            Intent voltaramain = new Intent(CreateAccountActivity.this, SplashActivity.class);
            voltaramain.putExtra("new_time_to_start",1);
            voltaramain.putExtra("new_time_to_showOptions",200);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
            ActivityCompat.startActivity(CreateAccountActivity.this,voltaramain, activityOptionsCompat.toBundle());
            finish();
        });

        avisoerro.show();
    }

    @Override
    public void onBackPressed() {
        Intent voltaraologin = new Intent(CreateAccountActivity.this, LoginActivity.class);
        startActivity(voltaraologin);
        finish();
    }
}