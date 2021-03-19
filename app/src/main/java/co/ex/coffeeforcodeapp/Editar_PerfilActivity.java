package co.ex.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import co.ex.coffeeforcodeapp.Api.User.DtoUsers;
import co.ex.coffeeforcodeapp.Api.User.UsersService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Editar_PerfilActivity extends AppCompatActivity {
    EditText edit_nome_edicaoperfil, edit_cpf_edicaoperfil, edit_email_edicaopefil, edit_celular_edicaopefil, edit_endereco_edicaopefil , edit_complemento_edicaopefil;
    ConstraintLayout base_dados_primarios;
    TextView txt_alter_senha, txt_btn_confirmar01, txtChangeProfileImage;
    CardView card_confirmar_edicao;
    LottieAnimationView btn_voltaraoperfil, animationloading_dados01;
    Handler timer = new Handler();
    int id_user, partner;
    String nm_user, email_user, phone_user, address_user, complement, img_user, cpf_user, partner_Startdate;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";
    final Retrofit retrofitUserUpdate = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar__perfil);
        btn_voltaraoperfil = findViewById(R.id.btn_voltaraoperfil);
        edit_nome_edicaoperfil = findViewById(R.id.edit_name_profileEditing);
        edit_cpf_edicaoperfil = findViewById(R.id.edit_cpf_profileEditing);
        edit_email_edicaopefil = findViewById(R.id.edit_email_profileEditing);
        edit_celular_edicaopefil = findViewById(R.id.edit_phone_profileEditing);
        edit_endereco_edicaopefil = findViewById(R.id.edit_address_profileEditing);
        edit_complemento_edicaopefil = findViewById(R.id.edit_complement_edicaopefil);
        card_confirmar_edicao = findViewById(R.id.card_confirmar_edicao);
        txt_alter_senha = findViewById(R.id.txt_alter_senha);
        txtChangeProfileImage = findViewById(R.id.txtChangeProfileImage);
        base_dados_primarios = findViewById(R.id.base_dados_primarios);
        animationloading_dados01 = findViewById(R.id.animationloading_dados01);
        txt_btn_confirmar01 = findViewById(R.id.txt_btn_confirmar01);
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set Mask
        edit_cpf_edicaoperfil.addTextChangedListener(MaskEditUtil.mask(edit_cpf_edicaoperfil, MaskEditUtil.FORMAT_CPF));
        edit_celular_edicaopefil.addTextChangedListener(MaskEditUtil.mask(edit_celular_edicaopefil, MaskEditUtil.FORMAT_FONE));

        //  Get some information
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

        //  Set somethings with gone or visible
        base_dados_primarios.setVisibility(View.VISIBLE);
        animationloading_dados01.pauseAnimation();
        animationloading_dados01.setVisibility(View.GONE);

        //  Verification of user preference information
        /*mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email") && sp.contains("pref_password"))
            Toast.makeText(this, ""+ sp.getString("pref_password", "password"), Toast.LENGTH_SHORT).show();*/

        buscar_informacoes();

        //  Btn go Back
        btn_voltaraoperfil.setOnClickListener(v -> GoBack_to_Profile());

        //  Card to confirm edit
        card_confirmar_edicao.setOnClickListener(v -> {
            if (edit_nome_edicaoperfil.getText() == null || edit_nome_edicaoperfil.getText().length() < 4){
                Toast.makeText(this, "Nome obrigatorio!!", Toast.LENGTH_SHORT).show();
                edit_nome_edicaoperfil.requestFocus();
                imm.showSoftInput(edit_nome_edicaoperfil, InputMethodManager.SHOW_IMPLICIT);
            }else if(edit_cpf_edicaoperfil.getText() == null || edit_cpf_edicaoperfil.getText().length() < 14){
                Toast.makeText(this, "CPF obrigatorio!!", Toast.LENGTH_SHORT).show();
                edit_cpf_edicaoperfil.requestFocus();
                imm.showSoftInput(edit_cpf_edicaoperfil, InputMethodManager.SHOW_IMPLICIT);
            }else if(edit_email_edicaopefil.getText() == null || edit_email_edicaopefil.getText().length() == 0){
                Toast.makeText(this, "Necessário preencher o campo: EMAIL", Toast.LENGTH_SHORT).show();
                edit_email_edicaopefil.requestFocus();
                imm.showSoftInput(edit_email_edicaopefil, InputMethodManager.SHOW_IMPLICIT);

            }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit_email_edicaopefil.getText()).matches()){
                Toast.makeText(this, "Preencha corretamente seu email", Toast.LENGTH_SHORT).show();
                edit_email_edicaopefil.requestFocus();
                imm.showSoftInput(edit_email_edicaopefil, InputMethodManager.SHOW_IMPLICIT);
            }else {
                animationloading_dados01.setVisibility(View.VISIBLE);
                txt_btn_confirmar01.setVisibility(View.GONE);
                animationloading_dados01.playAnimation();
                desable_edit();
                String newNm_user = edit_nome_edicaoperfil.getText().toString();
                String newCpf_user = edit_cpf_edicaoperfil.getText().toString();
                String newPhone_User = edit_celular_edicaopefil.getText().toString();
                String newAddress_User = edit_endereco_edicaopefil.getText().toString();
                String newComplement_User = edit_complemento_edicaopefil.getText().toString();
                UsersService usersService = retrofitUserUpdate.create(UsersService.class);
                DtoUsers newUserInfo = new DtoUsers(newNm_user, newCpf_user, newPhone_User, newAddress_User, newComplement_User);
                Call<DtoUsers> resultUpdate = usersService.UpdateUser(id_user, newUserInfo);

                resultUpdate.enqueue(new Callback<DtoUsers>() {
                    @Override
                    public void onResponse(Call<DtoUsers> call, Response<DtoUsers> response) {
                        if (response.code() == 202){
                            Toast.makeText(Editar_PerfilActivity.this, "Updated successfully\nAtualizado com sucesso", Toast.LENGTH_SHORT).show();
                            nm_user = newNm_user;
                            cpf_user = newCpf_user;
                            phone_user = newPhone_User;
                            address_user = newAddress_User;
                            complement = newComplement_User;
                            GoBack_to_Profile();
                        }else{
                            Toast.makeText(Editar_PerfilActivity.this, "Error try later\nCode: " + response.code(), Toast.LENGTH_SHORT).show();
                            GoBack_to_Profile();
                            Log.d("NetWorkError", String.valueOf(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<DtoUsers> call, Throwable t) {
                        Toast.makeText(Editar_PerfilActivity.this, "Error try later\n" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        //  Text to start seconds dates edit
        txt_alter_senha.setOnClickListener(v -> {
            Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
        });

        txtChangeProfileImage.setOnClickListener(v ->{
            Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
        });


    }

    /*public void uploadImage(){
        android.app.AlertDialog.Builder selectImage = new AlertDialog.Builder(Editar_PerfilActivity.this);
        selectImage.setTitle("Origem da foto");
        selectImage.setMessage("Por favor, selecione a origem da foto!");
        selectImage.setPositiveButton("Galeria", (dialog, which) -> {
            Intent galeria = new Intent(Intent.ACTION_GET_CONTENT);
            galeria.setType("image/*");
            startActivityForResult(galeria, 2);
        });
        selectImage.setNegativeButton("Camera", (dialog, which) -> {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, 1);

        });
        selectImage.show();
    }*/

    public void buscar_informacoes(){
        try {
            edit_nome_edicaoperfil.setText(nm_user);
            edit_cpf_edicaoperfil.setText(cpf_user);
            edit_email_edicaopefil.setText(email_user);
            edit_celular_edicaopefil.setText(phone_user);
            edit_endereco_edicaopefil.setText(address_user);
            edit_complemento_edicaopefil.setText(complement);
        }catch (Exception ex){
            Toast.makeText(this, "Erro ao buscar informaçoes: " + ex , Toast.LENGTH_SHORT).show();
            GoBack_to_Profile();

        }
    }

    public void GoBack_to_Profile(){
        Intent GoTo_profile = new Intent(Editar_PerfilActivity.this, ProfileActivity.class);
        GoTo_profile.putExtra("id_user", id_user);
        GoTo_profile.putExtra("email_user", email_user);
        GoTo_profile.putExtra("nm_user", nm_user);
        GoTo_profile.putExtra("cpf_user", cpf_user);
        GoTo_profile.putExtra("phone_user", phone_user);
        GoTo_profile.putExtra("address_user", address_user);
        GoTo_profile.putExtra("complement", complement);
        GoTo_profile.putExtra("img_user", img_user);
        GoTo_profile.putExtra("partner", partner);
        GoTo_profile.putExtra("partner_Startdate", partner_Startdate);
        startActivity(GoTo_profile);
        finish();
    }

    public void desable_edit(){
        card_confirmar_edicao.setEnabled(false);
        edit_nome_edicaoperfil.setEnabled(false);
        edit_cpf_edicaoperfil.setEnabled(false);
        edit_email_edicaopefil.setEnabled(false);
        edit_celular_edicaopefil.setEnabled(false);
        edit_endereco_edicaopefil.setEnabled(false);
        edit_complemento_edicaopefil.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        GoBack_to_Profile();
    }
}