package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import co.ex.coffeeforcodeapp.Api.User.DtoUsers;
import co.ex.coffeeforcodeapp.Api.User.UsersService;

import co.ex.coffeeforcodeapp.Api.zipcode.DtoZipCode;
import co.ex.coffeeforcodeapp.Api.zipcode.ZipCodeService;
import co.ex.coffeeforcodeapp.MaskEditUtil;
import co.ex.coffeeforcodeapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Edit_ProfileActivity extends AppCompatActivity {
    EditText edit_nome_edicaoperfil, edit_cpf_edicaoperfil, edit_email_edicaopefil, edit_celular_edicaopefil,
            edit_zipcode_profileEditing, edit_address_profileEditing, edit_complement_edicaopefil;
    ConstraintLayout base_dados_primarios;
    TextView txt_alter_senha, txt_btn_confirmar01, txtChangeProfileImage, txtSearchAddress;
    CardView card_confirmar_edicao;
    LottieAnimationView btn_voltaraoperfil, animationloading_dados01;
    //Handler timer = new Handler();
    int id_user, partner;
    String nm_user, email_user, phone_user, address_user, complement, img_user, cpf_user, partner_Startdate;
    String zipcode;
    //private SharedPreferences mPrefs;
    //private static final String PREFS_NAME = "PrefsFile";
    //  Apis
    final Retrofit retrofitUserUpdate = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final Retrofit retrofitZipCode = new Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://coffeeforcode.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        Ids();
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set Mask
        edit_cpf_edicaoperfil.addTextChangedListener(MaskEditUtil.mask(edit_cpf_edicaoperfil, MaskEditUtil.FORMAT_CPF));
        edit_celular_edicaopefil.addTextChangedListener(MaskEditUtil.mask(edit_celular_edicaopefil, MaskEditUtil.FORMAT_FONE));
        edit_zipcode_profileEditing.addTextChangedListener(MaskEditUtil.mask(edit_zipcode_profileEditing, MaskEditUtil.FORMAT_CEP));

        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_user = bundle.getInt("id_user");
        email_user = bundle.getString("email_user");
        nm_user = bundle.getString("nm_user");
        cpf_user = bundle.getString("cpf_user");
        phone_user = bundle.getString("phone_user");
        zipcode = bundle.getString("zipcode");
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

        //  Text to start seconds dates edit
        txt_alter_senha.setOnClickListener(v -> {
            Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
        });

        txtChangeProfileImage.setOnClickListener(v ->{
            pd = new ProgressDialog(this);
            pd.setMessage("Uploading....");
            Intent openGallery = new Intent();
            openGallery.setType("image/*");
            openGallery.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(openGallery, "Select Image"), PICK_IMAGE_REQUEST);
        });

        txtSearchAddress.setOnClickListener(v -> {
            if(edit_zipcode_profileEditing.getText().length() < 9){
                Toast.makeText(this, "Zip code required !!\nObrigatório informar o CEP !!", Toast.LENGTH_SHORT).show();
                edit_zipcode_profileEditing.requestFocus();
                imm.showSoftInput(edit_zipcode_profileEditing, InputMethodManager.SHOW_IMPLICIT);
            }else if (edit_zipcode_profileEditing.getText().length() == 9) {
                zipcode = edit_zipcode_profileEditing.getText().toString();
                ZipCodeService zipCodeService = retrofitZipCode.create(ZipCodeService.class);
                Call<DtoZipCode> zipcodeCall = zipCodeService.getAddress(zipcode);
                zipcodeCall.enqueue(new Callback<DtoZipCode>() {
                    @Override
                    public void onResponse(Call<DtoZipCode> call, Response<DtoZipCode> response) {
                        if (response.isSuccessful()) {
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                                    edit_zipcode_profileEditing.getWindowToken(), 0);
                            assert response.body() != null;
                            zipcode = response.body().getCep();
                            edit_address_profileEditing.setText(response.body().getLogradouro());
                            edit_complement_edicaopefil.setText(response.body().getComplemento());
                        } else {
                            Toast.makeText(Edit_ProfileActivity.this, "Error in get your address\nTry later\nErro em receber seu endereço\nTente mais tarde", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<DtoZipCode> call, Throwable t) {
                        Toast.makeText(Edit_ProfileActivity.this, "Error in get your address\nErro em receber seu endereço", Toast.LENGTH_SHORT).show();
                        Log.d("NetWorkError", t.getMessage());
                    }
                });
            }
        });

        //  Card to confirm edit
        card_confirmar_edicao.setOnClickListener(v -> {
            if (edit_nome_edicaoperfil.getText() == null || edit_nome_edicaoperfil.getText().length() < 4){
                Toast.makeText(this, "Nome obrigatorio!!", Toast.LENGTH_SHORT).show();
                edit_nome_edicaoperfil.requestFocus();
                imm.showSoftInput(edit_nome_edicaoperfil, InputMethodManager.SHOW_IMPLICIT);
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
                String new_Zipcode = edit_zipcode_profileEditing.getText().toString();
                String newAddress_User = edit_address_profileEditing.getText().toString();
                String newComplement_User = edit_complement_edicaopefil.getText().toString();
                UsersService usersService = retrofitUserUpdate.create(UsersService.class);
                DtoUsers newUserInfo = new DtoUsers(newNm_user, newCpf_user, newPhone_User, new_Zipcode, newAddress_User, newComplement_User);
                Call<DtoUsers> resultUpdate = usersService.UpdateUser(id_user, newUserInfo);

                resultUpdate.enqueue(new Callback<DtoUsers>() {
                    @Override
                    public void onResponse(Call<DtoUsers> call, Response<DtoUsers> response) {
                        if (response.code() == 202){
                            Toast.makeText(Edit_ProfileActivity.this, "Updated successfully\nAtualizado com sucesso", Toast.LENGTH_SHORT).show();
                            nm_user = newNm_user;
                            cpf_user = newCpf_user;
                            phone_user = newPhone_User;
                            zipcode = new_Zipcode;
                            address_user = newAddress_User;
                            complement = newComplement_User;
                            GoBack_to_Profile();
                        }else{
                            Toast.makeText(Edit_ProfileActivity.this, "Error try later\nCode: " + response.code(), Toast.LENGTH_SHORT).show();
                            GoBack_to_Profile();
                            Log.d("NetWorkError", String.valueOf(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<DtoUsers> call, Throwable t) {
                        Toast.makeText(Edit_ProfileActivity.this, "Error try later\n" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                if(filePath != null) {
                    pd.show();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                    //   + "_" + bitmap.getByteCount()
                    StorageReference storageRef = this.storageRef.child("ProfileImage_" + id_user);

                    //uploading the image
                    storageRef.putFile(filePath).continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(Edit_ProfileActivity.this, R.string.couldnt_insert, Toast.LENGTH_SHORT).show();
                        }
                        return storageRef.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            UsersService usersService = retrofitUserUpdate.create(UsersService.class);
                            DtoUsers newimg = new DtoUsers(downloadUri.toString());
                            Call<DtoUsers> usersCall = usersService.UpdateImgUser(id_user, newimg);
                            usersCall.enqueue(new Callback<DtoUsers>() {
                                @Override
                                public void onResponse(Call<DtoUsers> call, Response<DtoUsers> response) {
                                    if (response.code() == 202){
                                        pd.dismiss();
                                        img_user = downloadUri.toString();

                                        Toast.makeText(Edit_ProfileActivity.this, R.string.upload_successful, Toast.LENGTH_SHORT).show();
                                    }else{
                                        pd.dismiss();
                                        Toast.makeText(Edit_ProfileActivity.this, R.string.couldnt_insert, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<DtoUsers> call, Throwable t) {
                                    Toast.makeText(Edit_ProfileActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                                    Log.d("ServerErrorUpload", t.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(Edit_ProfileActivity.this, "upload failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(Edit_ProfileActivity.this, R.string.select_an_image, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void Ids() {
        btn_voltaraoperfil = findViewById(R.id.btn_voltaraoperfil);
        edit_nome_edicaoperfil = findViewById(R.id.edit_name_profileEditing);
        edit_cpf_edicaoperfil = findViewById(R.id.edit_cpf_profileEditing);
        edit_email_edicaopefil = findViewById(R.id.edit_email_profileEditing);
        edit_celular_edicaopefil = findViewById(R.id.edit_phone_profileEditing);
        edit_address_profileEditing = findViewById(R.id.edit_address_profileEditing);
        edit_complement_edicaopefil = findViewById(R.id.edit_complement_edicaopefil);
        edit_zipcode_profileEditing = findViewById(R.id.edit_zipcode_profileEditing);
        txtSearchAddress = findViewById(R.id.txtSearchAddress);
        card_confirmar_edicao = findViewById(R.id.card_confirmar_edicao);
        txt_alter_senha = findViewById(R.id.txt_alter_senha);
        txtChangeProfileImage = findViewById(R.id.txtChangeProfileImage);
        base_dados_primarios = findViewById(R.id.base_dados_primarios);
        animationloading_dados01 = findViewById(R.id.animationloading_dados01);
        txt_btn_confirmar01 = findViewById(R.id.txt_btn_confirmar01);
    }

    public void buscar_informacoes(){
        try {
            edit_nome_edicaoperfil.setText(nm_user);
            edit_cpf_edicaoperfil.setText(cpf_user);
            edit_email_edicaopefil.setText(email_user);
            edit_celular_edicaopefil.setText(phone_user);
            edit_zipcode_profileEditing.setText(zipcode);
            edit_address_profileEditing.setText(address_user);
            edit_complement_edicaopefil.setText(complement);
        }catch (Exception ex){
            Toast.makeText(this, "Erro ao buscar informaçoes: " + ex , Toast.LENGTH_SHORT).show();
            GoBack_to_Profile();
        }
    }

    public void GoBack_to_Profile(){
        Intent GoTo_profile = new Intent(Edit_ProfileActivity.this, ProfileActivity.class);
        GoTo_profile.putExtra("id_user", id_user);
        GoTo_profile.putExtra("email_user", email_user);
        GoTo_profile.putExtra("nm_user", nm_user);
        GoTo_profile.putExtra("cpf_user", cpf_user);
        GoTo_profile.putExtra("phone_user", phone_user);
        GoTo_profile.putExtra("zipcode", zipcode);
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
        edit_address_profileEditing.setEnabled(false);
        edit_complement_edicaopefil.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        GoBack_to_Profile();
    }
}