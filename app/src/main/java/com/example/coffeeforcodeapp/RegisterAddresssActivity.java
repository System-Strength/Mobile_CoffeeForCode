package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

public class RegisterAddresssActivity extends AppCompatActivity {
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    LottieAnimationView animationenderecoconfirm, animationenderecodepois;
    TextView txtbtnconfirmar;
    EditText edittextenderecocliente, edittextenderecoclientecomp;
    CardView cardviewenderecoconfirmar, cardviewenderecodepois;
    int id_user, partner;
    String nm_user, email_user, phone_user, address_user, complement, img_user, cpf_user, partner_Startdate;
    final Retrofit retrofitUserUpdate = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeraddress);
        //  Find ids
        edittextenderecocliente = findViewById(R.id.edittextenderecocliente);
        edittextenderecoclientecomp = findViewById(R.id.edittextenderecoclientecomp);
        cardviewenderecoconfirmar = findViewById(R.id.cardviewenderecoconfirmar);
        cardviewenderecodepois = findViewById(R.id.cardviewenderecodepois);
        animationenderecoconfirm = findViewById(R.id.animationenderecoconfirm);
        animationenderecodepois = findViewById(R.id.animationenderecodepois);
        txtbtnconfirmar = findViewById(R.id.txtbtnconfirmar);
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Get some information
        GetSomeInformation();

        //  Set somethings with gone and visible
        animationenderecoconfirm.setVisibility(View.GONE);
        animationenderecodepois.setVisibility(View.GONE);
        txtbtnconfirmar.setVisibility(View.VISIBLE);

        //  When click at this, it will trying to register address
        cardviewenderecoconfirmar.setOnClickListener(v -> {
            if (edittextenderecocliente.getText() == null ||edittextenderecocliente.getText().length() <3){
                Toast.makeText(RegisterAddresssActivity.this, "Preencha corretamente o campo: Endereço", Toast.LENGTH_SHORT).show();
                edittextenderecocliente.requestFocus();
                imm.showSoftInput(edittextenderecocliente, InputMethodManager.SHOW_IMPLICIT);
            }else {
                txtbtnconfirmar.setVisibility(View.GONE);
                animationenderecoconfirm.setVisibility(View.VISIBLE);
                animationenderecoconfirm.playAnimation();
                edittextenderecocliente.setEnabled(false);
                edittextenderecoclientecomp.setEnabled(false);
                address_user = edittextenderecocliente.getText().toString();
                String complement = edittextenderecoclientecomp.getText().toString();
                UsersService usersService = retrofitUserUpdate.create(UsersService.class);
                DtoUsers newAddress = new DtoUsers(address_user, complement);
                Call<DtoUsers> resultUpdate = usersService.UpdateAddress(id_user, newAddress);

                resultUpdate.enqueue(new Callback<DtoUsers>() {
                    @Override
                    public void onResponse(Call<DtoUsers> call, Response<DtoUsers> response) {
                        if (response.code() == 202){
                            Toast.makeText(RegisterAddresssActivity.this, "Endereço cadastrado com sucesso!!", Toast.LENGTH_SHORT).show();
                            Intent voltarcadastrarendereco = new Intent(RegisterAddresssActivity.this, MainActivity.class);
                            voltarcadastrarendereco.putExtra("id_user", id_user);
                            voltarcadastrarendereco.putExtra("nm_user", nm_user);
                            voltarcadastrarendereco.putExtra("email_user", email_user);
                            voltarcadastrarendereco.putExtra("phone_user", phone_user);
                            voltarcadastrarendereco.putExtra("address_user", address_user);
                            voltarcadastrarendereco.putExtra("complement", complement);
                            voltarcadastrarendereco.putExtra("img_user", img_user);
                            voltarcadastrarendereco.putExtra("address_user", address_user);
                            voltarcadastrarendereco.putExtra("cpf_user", cpf_user);
                            voltarcadastrarendereco.putExtra("partner", partner);
                            voltarcadastrarendereco.putExtra("partner_Startdate", partner_Startdate);
                            voltarcadastrarendereco.putExtra("statusavisoend","desativado");
                            startActivity(voltarcadastrarendereco);
                            finish();
                        }else{
                            Toast.makeText(RegisterAddresssActivity.this, "Error trying to register your address !!\nErro ao tentar registrar seu endereço!!", Toast.LENGTH_LONG).show();
                            GoBack_toMain();
                        }
                    }

                    @Override
                    public void onFailure(Call<DtoUsers> call, Throwable t) {
                        Log.d("NetWorkError", t.getMessage());
                        Toast.makeText(RegisterAddresssActivity.this, "Error trying to register your address !!\nErro ao tentar registrar seu endereço!!", Toast.LENGTH_LONG).show();
                        GoBack_toMain();
                    }
                });
            }
        });

        //  When click here will to PrincipalActivity
        cardviewenderecodepois.setOnClickListener(v -> GoBack_toMain());

    }

    private void GetSomeInformation() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_user = bundle.getInt("id_user");
        email_user = bundle.getString("email_user");
        nm_user = bundle.getString("nm_user");
        cpf_user = bundle.getString("cpf_user");
        phone_user = bundle.getString("phone_user");
        address_user = bundle.getString("address_user");
        complement = bundle.getString("complement");
        img_user = bundle.getString("img_user");
        partner = bundle.getInt("partner");
        partner_Startdate = bundle.getString("partner_Startdate");
    }

    private void GoBack_toMain(){
        Intent GoBack_ToMain = new Intent(RegisterAddresssActivity.this, MainActivity.class);
        GoBack_ToMain.putExtra("id_user", id_user);
        GoBack_ToMain.putExtra("nm_user", nm_user);
        GoBack_ToMain.putExtra("email_user", email_user);
        GoBack_ToMain.putExtra("phone_user", phone_user);
        GoBack_ToMain.putExtra("address_user", address_user);
        GoBack_ToMain.putExtra("complement", complement);
        GoBack_ToMain.putExtra("img_user", img_user);
        GoBack_ToMain.putExtra("address_user", address_user);
        GoBack_ToMain.putExtra("cpf_user", cpf_user);
        GoBack_ToMain.putExtra("partner", partner);
        GoBack_ToMain.putExtra("partner_Startdate", partner_Startdate);
        GoBack_ToMain.putExtra("statusavisoend","desativado");
        startActivity(GoBack_ToMain);
        finish();
    }

    @Override
    public void onBackPressed() {
        GoBack_toMain();
    }
}