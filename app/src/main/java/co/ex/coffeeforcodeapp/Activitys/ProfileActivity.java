package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import co.ex.coffeeforcodeapp.Api.User.DtoUsers;
import co.ex.coffeeforcodeapp.Api.User.UsersService;
import co.ex.coffeeforcodeapp.Api.UserImage.AsyncUserImage;

import co.ex.coffeeforcodeapp.R;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    //  Text Header
    TextView txt_Name_user_profile, txt_Email_user;
    //  Text Body
    TextView txt_cpf_profile, txt_phone_profile, txt_address_profile, txt_complement_erfil, txtstatuscard_profile, date_activationPartner_profile;
    //  Bases
    ConstraintLayout base_notHave_phone_registred, base_have_phone_registred, base_have_address_registraded, base_notHave_address_registraded,
            iam_notPartner_profile, client_is_partner, base_desc_partner_profile;
    LottieAnimationView animation_giftcard_profile;
    CardView btn_be_partner_profile, card_registerphone_profile, cardbtn_registerAddress_profile, cardbtn_edit_profile;
    CircleImageView Profile_image;
    Handler timer = new Handler();


    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    @SuppressWarnings("deprecation")
    ProgressDialog pd;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://coffeeforcode.appspot.com");

    final Retrofit retrofitUserUpdate = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTheme(R.style.Perfil);
        Ids();

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

        if (img_user == null){
            load_user_info();
        }else{
            loadProfileImage();
            load_user_info();
            Log.d("ProfileImageStatus", "loading image");
        }


        btn_be_partner_profile.setOnClickListener(v -> {
            Intent irpara_sejaparceira = new Intent(ProfileActivity.this, SejaParceiroActivity.class);
            irpara_sejaparceira.putExtra("id_user", id_user);
            irpara_sejaparceira.putExtra("email_user", email_user);
            irpara_sejaparceira.putExtra("nm_user", nm_user);
            irpara_sejaparceira.putExtra("cpf_user", cpf_user);
            irpara_sejaparceira.putExtra("phone_user", phone_user);
            irpara_sejaparceira.putExtra("zipcode", zipcode);
            irpara_sejaparceira.putExtra("address_user", address_user);
            irpara_sejaparceira.putExtra("complement", complement);
            irpara_sejaparceira.putExtra("img_user", img_user);
            irpara_sejaparceira.putExtra("partner", partner);
            irpara_sejaparceira.putExtra("partner_Startdate", partner_Startdate);
            startActivity(irpara_sejaparceira);
            finish();
        });

        cardbtn_edit_profile.setOnClickListener(v -> GoTo_EditProfile());

        card_registerphone_profile.setOnClickListener(v -> GoTo_EditProfile());

        cardbtn_registerAddress_profile.setOnClickListener(v -> GoTo_EditProfile());

        //  Click to upload new profile image
        Profile_image.setOnClickListener(v -> {
            pd = new ProgressDialog(this);
            pd.setMessage("Uploading....");
            Intent openGallery = new Intent();
            openGallery.setType("image/*");
            openGallery.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(openGallery, "Select Image"), PICK_IMAGE_REQUEST);
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
                            Toast.makeText(ProfileActivity.this, R.string.couldnt_insert, Toast.LENGTH_SHORT).show();
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
                                        loadProfileImage();

                                        Toast.makeText(ProfileActivity.this, R.string.upload_successful, Toast.LENGTH_SHORT).show();
                                    }else{
                                        pd.dismiss();
                                        Toast.makeText(ProfileActivity.this, R.string.couldnt_insert, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<DtoUsers> call, Throwable t) {
                                    Toast.makeText(ProfileActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                                    Log.d("ServerErrorUpload", t.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(ProfileActivity.this, "upload failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(ProfileActivity.this, R.string.select_an_image, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void Ids() {
        txt_Name_user_profile = findViewById(R.id.txt_Name_user_profile);
        txt_Email_user = findViewById(R.id.txt_Email_user);
        txt_cpf_profile = findViewById(R.id.txt_cpf_profile);
        base_notHave_phone_registred = findViewById(R.id.base_notHave_phone_registred);
        base_have_phone_registred = findViewById(R.id.base_have_phone_registred);
        txt_phone_profile = findViewById(R.id.txt_phone_profile);
        txt_address_profile = findViewById(R.id.txt_address_profile);
        txt_complement_erfil = findViewById(R.id.txt_complement_erfil);
        base_have_address_registraded = findViewById(R.id.base_have_address_registraded);
        base_notHave_address_registraded = findViewById(R.id.base_notHave_address_registraded);
        iam_notPartner_profile = findViewById(R.id.iam_notPartner_profile);
        client_is_partner = findViewById(R.id.client_is_partner);
        animation_giftcard_profile = findViewById(R.id.animation_giftcard_profile);
        base_desc_partner_profile = findViewById(R.id.base_desc_partner_profile);
        txtstatuscard_profile = findViewById(R.id.txtstatuscard_profile);
        date_activationPartner_profile = findViewById(R.id.date_activationPartner_profile);
        btn_be_partner_profile = findViewById(R.id.btn_be_partner_profile);
        card_registerphone_profile = findViewById(R.id.card_registerphone_profile);
        cardbtn_registerAddress_profile = findViewById(R.id.cardbtn_registerAddress_profile);
        cardbtn_edit_profile = findViewById(R.id.cardbtn_edit_profile);
        Profile_image = findViewById(R.id.Profile_image);
    }

    private void loadProfileImage() {
        AsyncUserImage loadimage = new AsyncUserImage(img_user, Profile_image);
        loadimage.execute();
    }

    private void GoTo_EditProfile() {
        Intent GoTo_EditPofile = new Intent(ProfileActivity.this, Edit_ProfileActivity.class);
        GoTo_EditPofile.putExtra("id_user", id_user);
        GoTo_EditPofile.putExtra("email_user", email_user);
        GoTo_EditPofile.putExtra("nm_user", nm_user);
        GoTo_EditPofile.putExtra("cpf_user", cpf_user);
        GoTo_EditPofile.putExtra("phone_user", phone_user);
        GoTo_EditPofile.putExtra("zipcode", zipcode);
        GoTo_EditPofile.putExtra("address_user", address_user);
        GoTo_EditPofile.putExtra("complement", complement);
        GoTo_EditPofile.putExtra("img_user", img_user);
        GoTo_EditPofile.putExtra("partner", partner);
        startActivity(GoTo_EditPofile);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void load_user_info(){
        txt_Name_user_profile.setText(nm_user);
        txt_Email_user.setText(email_user);
        txt_cpf_profile.setText(cpf_user);
        if (phone_user == null || phone_user.equals(" ")){
            base_notHave_phone_registred.setVisibility(View.VISIBLE);
            base_have_phone_registred.setVisibility(View.GONE);
        }else {
            txt_phone_profile.setText(phone_user);
            base_notHave_phone_registred.setVisibility(View.GONE);
            base_have_phone_registred.setVisibility(View.VISIBLE);
        }
        if (address_user == null || address_user.equals(" ")){
            base_have_address_registraded.setVisibility(View.GONE);
            base_notHave_address_registraded.setVisibility(View.VISIBLE);

        }else {
            base_have_address_registraded.setVisibility(View.VISIBLE);
            base_notHave_address_registraded.setVisibility(View.GONE);
            txt_address_profile.setText(address_user);
            if (complement == null){
                txt_complement_erfil.setText(R.string.complement_not_registred);
            }else {
                txt_complement_erfil.setText(complement);
            }
        }
        info_parceiro();

    }

    @SuppressLint("SetTextI18n")
    private void info_parceiro(){
        if (partner == 0){
            client_is_partner.setVisibility(View.GONE);
            base_desc_partner_profile.setVisibility(View.GONE);
            iam_notPartner_profile.setVisibility(View.VISIBLE);
            animation_giftcard_profile.setVisibility(View.VISIBLE);
            animation_giftcard_profile.playAnimation();
            timer.postDelayed(() -> {
                animation_giftcard_profile.setVisibility(View.GONE);
                base_desc_partner_profile.setVisibility(View.VISIBLE);
            },2950);
        }else {
            client_is_partner.setVisibility(View.VISIBLE);
            iam_notPartner_profile.setVisibility(View.GONE);
            txtstatuscard_profile.setText(R.string.activated_card);
            date_activationPartner_profile.setText("Assinatura gerada em: " + partner_Startdate);
        }
    }

    @Override
    public void onBackPressed() {
        Intent GoBack_ToMain = new Intent(ProfileActivity.this, MainActivity.class);
        GoBack_ToMain.putExtra("id_user", id_user);
        GoBack_ToMain.putExtra("nm_user", nm_user);
        GoBack_ToMain.putExtra("email_user", email_user);
        GoBack_ToMain.putExtra("phone_user", phone_user);
        GoBack_ToMain.putExtra("zipcode", zipcode);
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
}