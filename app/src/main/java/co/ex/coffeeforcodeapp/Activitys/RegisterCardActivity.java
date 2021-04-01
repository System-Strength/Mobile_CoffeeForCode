package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.Card.CardService;
import co.ex.coffeeforcodeapp.Api.Card.DtoCard;
import co.ex.coffeeforcodeapp.MaskEditUtil;
import co.ex.coffeeforcodeapp.R;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterCardActivity extends AppCompatActivity {
    CircleImageView icon_ProfileUser_registerCard;
    CardView btnGoBackPurchase, cardBtn_addCard, cardBtn_generateCard;

    //  Cards Flags
    ImageView img_btnMasterCard, img_btnVisaCard, img_btnMaestroCard;
    ConstraintLayout cardBtn_MasterCard, cardBtnVisa, cardBtn_Maestro;

    //  Card Pre View
    CardView cardPreView_register;
    ImageView imgFlag_Pre_view_Card;
    TextView txt_shelflife_cardregister, txt_numbercard_cardregister, txt_nmcard_cardregister;

    //  Edits
    EditText edit_cardnumber_register, edit_shelf_life_register, edit_cvv_register, edit_namecard_register;

    //  Card Information
    String card_flag, card_number, card_namePrinted, card_shelflife, card_cvv;

    //  Flags Images
    String flag_visa = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fvisa.png?alt=media&token=e9dd2e2b-dd30-444e-b745-2a1dd2273db9";
    String flag_mastercard = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmastercard.png?alt=media&token=79df43fd-494c-4160-93f1-7194266f76b9";
    String flag_maestro = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmaestro.png?alt=media&token=28fd5789-f277-4027-8b0f-9ff1f38b2d5d";


    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;

    Dialog warningcardregister;
    Handler timer = new Handler();
    LoadingDialog loadingDialog = new LoadingDialog(RegisterCardActivity.this);

    //  Retrofit
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "card/insert/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_card);
        ids();
        warningcardregister = new Dialog(this);
        ShowWarning();
        cardBtn_addCard.setElevation(20);
        cardBtn_generateCard.setElevation(20);
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set Mask
        edit_cardnumber_register.addTextChangedListener(MaskEditUtil.mask(edit_cardnumber_register, MaskEditUtil.FORMAT_NUMCARD));
        edit_shelf_life_register.addTextChangedListener(MaskEditUtil.mask(edit_shelf_life_register, MaskEditUtil.FORMAT_DATAVALI));

        //noinspection deprecation
        @SuppressLint("UseCompatLoadingForDrawables") Drawable cardFlagSelected = getResources().getDrawable(R.drawable.card_flag_selected);
        //noinspection deprecation
        int colorMasterColor = getResources().getColor(R.color.mastercard);
        //noinspection deprecation
        int colorVisaColor = getResources().getColor(R.color.visacard);
        //noinspection deprecation
        int colorMaestroColor = getResources().getColor(R.color.maestrocard);



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
        Picasso.get().load(img_user).into(icon_ProfileUser_registerCard);
        Picasso.get().load(flag_mastercard).into(img_btnMasterCard);
        Picasso.get().load(flag_visa).into(img_btnVisaCard);
        Picasso.get().load(flag_maestro).into(img_btnMaestroCard);
        cardBtn_MasterCard.setBackground(cardFlagSelected);
        cardPreView_register.setCardBackgroundColor(colorMasterColor);
        card_flag = "MasterCard";
        Picasso.get().load(flag_mastercard).into(imgFlag_Pre_view_Card);

        cardBtn_MasterCard.setOnClickListener(v -> {
            removeAllFlagSelected();
            card_flag = "MasterCard";
            cardBtn_MasterCard.setBackground(cardFlagSelected);
            cardPreView_register.setCardBackgroundColor(colorMasterColor);
            Picasso.get().load(flag_mastercard).into(imgFlag_Pre_view_Card);
        });

        cardBtnVisa.setOnClickListener(v -> {
            removeAllFlagSelected();
            card_flag = "Visa";
            cardBtnVisa.setBackground(cardFlagSelected);
            cardPreView_register.setCardBackgroundColor(colorVisaColor);
            Picasso.get().load(flag_visa).into(imgFlag_Pre_view_Card);
        });

        cardBtn_Maestro.setOnClickListener(v -> {
            removeAllFlagSelected();
            card_flag = "Maestro";
            cardBtn_Maestro.setBackground(cardFlagSelected);
            cardPreView_register.setCardBackgroundColor(colorMaestroColor);
            Picasso.get().load(flag_maestro).into(imgFlag_Pre_view_Card);
        });

        btnGoBackPurchase.setOnClickListener(v -> goTo_main());

        cardBtn_generateCard.setOnClickListener(v -> {
            cardBtn_generateCard.setElevation(0);
            GenerateRandomCard();
            timer.postDelayed(() -> cardBtn_generateCard.setElevation(20),1500);
        });

        cardBtn_addCard.setOnClickListener(v -> {
            cardBtn_addCard.setElevation(0);
            if (card_flag == null || card_flag.equals(" ")){
                Toast.makeText(this, R.string.no_flag_selected, Toast.LENGTH_SHORT).show();
            }else if (card_number == null || card_number.length() < 19){
                Toast.makeText(this, R.string.cardNumber_worginsert, Toast.LENGTH_SHORT).show();
                edit_cardnumber_register.requestFocus();
                imm.showSoftInput(edit_cardnumber_register, InputMethodManager.SHOW_IMPLICIT);
            } else if (card_shelflife == null || card_shelflife.length() < 5) {
                Toast.makeText(this, R.string.shelflife_incorrectly, Toast.LENGTH_SHORT).show();
                edit_shelf_life_register.requestFocus();
                imm.showSoftInput(edit_shelf_life_register, InputMethodManager.SHOW_IMPLICIT);
            }else if (card_cvv == null || card_cvv.length() < 3){
                Toast.makeText(this, R.string.cvv_inserted_incorrectly, Toast.LENGTH_SHORT).show();
                edit_cvv_register.requestFocus();
                imm.showSoftInput(edit_cvv_register, InputMethodManager.SHOW_IMPLICIT);
            }else if(card_namePrinted == null || card_namePrinted.length() < 5){
                Toast.makeText(this, R.string.name_incorrectly_inserted, Toast.LENGTH_SHORT).show();
                edit_namecard_register.requestFocus();
                imm.showSoftInput(edit_namecard_register, InputMethodManager.SHOW_IMPLICIT);
            }else{
                cardBtn_addCard.setEnabled(false);
                loadingDialog.startLoading();
                CardService cardService = retrofitCard.create(CardService.class);
                Call<DtoCard> cardCall = cardService.insertNewCard(email_user, card_flag, card_number, card_shelflife, card_cvv, card_namePrinted);
                cardCall.enqueue(new Callback<DtoCard>() {
                    @Override
                    public void onResponse(@NotNull Call<DtoCard> call, @NotNull Response<DtoCard> response) {
                        if (response.code() == 201){
                            loadingDialog.dimissDialog();
                            goTo_allCard();
                        }else if(response.code() == 207){
                            loadingDialog.dimissDialog();
                            Toast.makeText(RegisterCardActivity.this, R.string.you_have_moreOf_theree_cards, Toast.LENGTH_SHORT).show();
                            goTo_allCard();
                        }else if(response.code() == 409){
                            loadingDialog.dimissDialog();
                            cardBtn_addCard.setEnabled(true);
                            edit_shelf_life_register.setEnabled(true);
                            edit_cardnumber_register.setEnabled(true);
                            edit_cvv_register.setEnabled(true);
                            Toast.makeText(RegisterCardActivity.this, R.string.card_already_inserted, Toast.LENGTH_SHORT).show();
                        }else{
                            loadingDialog.dimissDialog();
                            goTo_main();
                            Toast.makeText(RegisterCardActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(@NotNull Call<DtoCard> call, @NotNull Throwable t) {
                        loadingDialog.dimissDialog();
                        cardBtn_addCard.setEnabled(true);
                        cardBtn_addCard.setElevation(20);
                        Toast.makeText(RegisterCardActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                        Log.d("ErroNetWork", t.getMessage());
                        goTo_main();
                    }
                });
            }
        });

        edit_cardnumber_register.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_cardnumber_register.getText().length() > 0){
                    String numberCard = edit_cardnumber_register.getText().toString();
                    card_number = numberCard;
                    txt_numbercard_cardregister.setText(numberCard);
                }else{
                    txt_numbercard_cardregister.setText(R.string.ex_numbercard_card);
                    card_number = null;
                }
            }
        });

        edit_shelf_life_register.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_shelf_life_register.getText().length() > 0){
                    String shelflife = edit_shelf_life_register.getText().toString();
                    card_shelflife = shelflife;
                    txt_shelflife_cardregister.setText(shelflife);
                }else{
                    txt_shelflife_cardregister.setText(R.string.ex_shelflife_card);
                    card_shelflife = null;
                }
            }
        });

        edit_cvv_register.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_cvv_register.getText().length() > 0){
                    card_cvv = edit_cvv_register.getText().toString();
                }else{
                    card_cvv = null;
                }
            }
        });

        edit_namecard_register.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_namecard_register.getText().length() > 0){
                    String namePrintedCard = edit_namecard_register.getText().toString();
                    card_namePrinted = namePrintedCard;
                    txt_nmcard_cardregister.setText(namePrintedCard);
                }else{
                    txt_nmcard_cardregister.setText(R.string.fulanodetal);
                    card_namePrinted = null;
                }
            }
        });

    }

    private void goTo_allCard() {
        Intent goToCards = new Intent(RegisterCardActivity.this, CardsActivity.class);
        goToCards.putExtra("id_user", id_user);
        goToCards.putExtra("nm_user", nm_user);
        goToCards.putExtra("email_user", email_user);
        goToCards.putExtra("phone_user", phone_user);
        goToCards.putExtra("zipcode", zipcode);
        goToCards.putExtra("address_user", address_user);
        goToCards.putExtra("complement", complement);
        goToCards.putExtra("img_user", img_user);
        goToCards.putExtra("address_user", address_user);
        goToCards.putExtra("cpf_user", cpf_user);
        goToCards.putExtra("partner", partner);
        goToCards.putExtra("partner_Startdate", partner_Startdate);
        goToCards.putExtra("statusavisoend","desativado");
        startActivity(goToCards);
        finish();
    }

    public void ShowWarning(){
        warningcardregister.setContentView(R.layout.adapter_warning_card_register);
        warningcardregister.setCancelable(false);

        CardView cardviewOk_warningregistercard;
        cardviewOk_warningregistercard = warningcardregister.findViewById(R.id.cardviewOk_warningregistercard);

        cardviewOk_warningregistercard.setOnClickListener(v -> warningcardregister.dismiss());

        warningcardregister.show();
    }

    @SuppressLint("SetTextI18n")
    public void GenerateRandomCard(){
        //noinspection deprecation
        @SuppressLint("UseCompatLoadingForDrawables") Drawable cardFlagSelected = getResources().getDrawable(R.drawable.card_flag_selected);
        //noinspection deprecation
        int colorVisaColor = getResources().getColor(R.color.visacard);
        removeAllFlagSelected();
        cardBtnVisa.setBackground(cardFlagSelected);
        cardPreView_register.setCardBackgroundColor(colorVisaColor);
        Picasso.get().load(flag_visa).into(imgFlag_Pre_view_Card);
        //  Gen NumberCard
        Random rdm = new Random();
        String numB1 = rdm.nextInt(10) + "";
        String numB1v1 = rdm.nextInt(10) + "";
        String numB1v2 = rdm.nextInt(10) + "";
        String numB2 = rdm.nextInt(10) + "";
        String numB2v1 = rdm.nextInt(10) + "";
        String numB3 = rdm.nextInt(10) + "";
        String numB4 = rdm.nextInt(10) + "";
        String numB4v1 = rdm.nextInt(10) + "";
        String numB4v2 = rdm.nextInt(10) + "";
        card_number = "4" + numB2 + "5"+ numB2v1 +" 4" + numB3 + "4" + numB4v1 + " 4"+ numB4v2 + numB4 + "1 " + numB1v1 + "4" + numB1v2 + numB1;
        txt_numbercard_cardregister.setText(card_number);
        edit_shelf_life_register.setEnabled(false);
        edit_cardnumber_register.setEnabled(false);
        edit_cvv_register.setEnabled(false);
        edit_namecard_register.setText(nm_user);
        txt_shelflife_cardregister.setText("12/36");
        txt_nmcard_cardregister.setText(nm_user);
        removeAllFlagSelected();
        card_flag = "Visa";
        card_shelflife = "12/36";
        card_cvv = numB1 + numB2 + numB1v2 + "";
        card_namePrinted = nm_user;
    }

    private void ids() {
        icon_ProfileUser_registerCard = findViewById(R.id.icon_ProfileUser_registerCard);
        btnGoBackPurchase = findViewById(R.id.btnGoBackPurchase);
        img_btnMasterCard = findViewById(R.id.img_btnMasterCard);
        img_btnVisaCard = findViewById(R.id.img_btnVisaCard);
        img_btnMaestroCard = findViewById(R.id.img_btnMaestroCard);
        cardBtn_MasterCard = findViewById(R.id.cardBtn_MasterCard);
        cardBtnVisa = findViewById(R.id.cardBtnVisa);
        cardBtn_Maestro = findViewById(R.id.cardBtn_Maestro);
        txt_shelflife_cardregister = findViewById(R.id.txt_shelflife_cardregister);
        txt_numbercard_cardregister = findViewById(R.id.txt_numbercard_cardregister);
        txt_nmcard_cardregister = findViewById(R.id.txt_nmcard_cardregister);
        imgFlag_Pre_view_Card = findViewById(R.id.imgFlag_Pre_view_Card);
        edit_cardnumber_register = findViewById(R.id.edit_cardnumber_register);
        edit_shelf_life_register = findViewById(R.id.edit_shelf_life_register);
        edit_cvv_register = findViewById(R.id.edit_cvv_register);
        edit_namecard_register = findViewById(R.id.edit_namecard_register);
        cardPreView_register = findViewById(R.id.cardPreView_register);
        cardBtn_addCard = findViewById(R.id.cardBtn_addCard);
        cardBtn_generateCard = findViewById(R.id.cardBtn_generateCard);
    }

    private void removeAllFlagSelected() {
        //noinspection deprecation
        @SuppressLint("UseCompatLoadingForDrawables") Drawable cardFlagDefault = getResources().getDrawable(R.drawable.default_card_flag);
        cardBtn_MasterCard.setBackground(cardFlagDefault);
        cardBtnVisa.setBackground(cardFlagDefault);
        cardBtn_Maestro.setBackground(cardFlagDefault);
    }

    private void goTo_main() {
        Intent GoBack_ToMain = new Intent(RegisterCardActivity.this, MainActivity.class);
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

    @Override
    public void onBackPressed() {
        goTo_main();
    }
}