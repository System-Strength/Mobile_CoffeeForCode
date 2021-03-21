package co.ex.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import co.ex.coffeeforcodeapp.Api.UserImage.AsyncUserImage;

import de.hdodenhof.circleimageview.CircleImageView;

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
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
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
        //noinspection deprecation
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