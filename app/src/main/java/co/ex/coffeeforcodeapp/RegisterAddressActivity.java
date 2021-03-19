package co.ex.coffeeforcodeapp;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.security.Permission;
import java.util.List;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.User.DtoUsers;
import co.ex.coffeeforcodeapp.Api.User.UsersService;
import co.ex.coffeeforcodeapp.Api.zipcode.DtoZipCode;
import co.ex.coffeeforcodeapp.Api.zipcode.ZipCodeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterAddressActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText edittext_zipcode_cliente, edittext_address_cliente, edittext_mumber_cliente, edittext_complement_cliente;
    CardView BtnConfirmRegisterAddress;
    LottieAnimationView animation_address_confirm;
    TextView txtBtnconfirm;
    //  User info
    int id_user, partner;
    String nm_user, email_user, phone_user, address_user, complement, img_user, cpf_user, partner_Startdate;
    String zipcode = "01310-100";
    private double latitude;
    private double longitude;

    //  Api Retrofit
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);
        edittext_zipcode_cliente = findViewById(R.id.edittext_zipcode_cliente);
        edittext_address_cliente = findViewById(R.id.edittext_address_cliente);
        edittext_mumber_cliente = findViewById(R.id.edittext_mumber_cliente);
        edittext_complement_cliente = findViewById(R.id.edittext_complement_cliente);
        BtnConfirmRegisterAddress = findViewById(R.id.BtnConfirmRegisterAddress);
        animation_address_confirm = findViewById(R.id.animation_address_confirm);
        txtBtnconfirm = findViewById(R.id.txtBtnconfirm);
        //  Set Mask
        edittext_zipcode_cliente.addTextChangedListener(MaskEditUtil.mask(edittext_zipcode_cliente, MaskEditUtil.FORMAT_CEP));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        animation_address_confirm.setVisibility(View.GONE);
        txtBtnconfirm.setVisibility(View.VISIBLE);

        GetUserInformation();

        //  Set somethings with disable
        edittext_address_cliente.setEnabled(false);
        edittext_mumber_cliente.setEnabled(false);
        edittext_complement_cliente.setEnabled(false);

        goToZipCode();

        edittext_zipcode_cliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittext_zipcode_cliente.getText().length() == 9){
                    zipcode = edittext_zipcode_cliente.getText().toString();
                    ZipCodeService zipCodeService = retrofitZipCode.create(ZipCodeService.class);
                    Call<DtoZipCode> zipcodeCall = zipCodeService.getAddress(zipcode);
                    zipcodeCall.enqueue(new Callback<DtoZipCode>() {
                        @Override
                        public void onResponse(Call<DtoZipCode> call, Response<DtoZipCode> response) {
                            if (response.isSuccessful()){
                                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                                        edittext_zipcode_cliente.getWindowToken(), 0);
                                assert response.body() != null;
                                zipcode = response.body().getCep();
                                edittext_address_cliente.setText(response.body().getLogradouro());
                                edittext_complement_cliente.setText(response.body().getComplemento());
                                goToZipCode();
                                onMapReady(mMap);
                                edittext_mumber_cliente.requestFocus();

                                //  Set Edits Enables
                                edittext_address_cliente.setEnabled(true);
                                edittext_mumber_cliente.setEnabled(true);
                                edittext_complement_cliente.setEnabled(true);
                            }else{
                                Toast.makeText(RegisterAddressActivity.this, "Error in get your address\nTry later\nErro em receber seu endereço\nTente mais tarde", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<DtoZipCode> call, Throwable t) {
                            Toast.makeText(RegisterAddressActivity.this, "Error in get your address\nErro em receber seu endereço", Toast.LENGTH_SHORT).show();
                            Log.d("NetWorkError", t.getMessage());
                        }
                    });
                }
            }
        });

        BtnConfirmRegisterAddress.setOnClickListener(v -> {
            if (edittext_zipcode_cliente.getText() == null || edittext_zipcode_cliente.getText().length() < 9){
                Toast.makeText(this, "You need to enter your zip code\nNecessário inserir seu CEP", Toast.LENGTH_SHORT).show();
                edittext_zipcode_cliente.requestFocus();
                imm.showSoftInput(edittext_zipcode_cliente, InputMethodManager.SHOW_IMPLICIT);

            }else if(edittext_address_cliente.getText() == null || edittext_address_cliente.getText().length() < 5){
                Toast.makeText(this, "You need to enter your address\nNecessário inserir seu Endereço", Toast.LENGTH_SHORT).show();
                edittext_address_cliente.requestFocus();
                imm.showSoftInput(edittext_address_cliente, InputMethodManager.SHOW_IMPLICIT);

            }else if(edittext_mumber_cliente.getText() == null || edittext_mumber_cliente.getText().length() == 0){
                Toast.makeText(this, "You need to enter your address number\nNecessário inserir seu Numero de Endereço", Toast.LENGTH_SHORT).show();
                edittext_mumber_cliente.requestFocus();
                imm.showSoftInput(edittext_mumber_cliente, InputMethodManager.SHOW_IMPLICIT);
            }else{
                txtBtnconfirm.setVisibility(View.GONE);
                animation_address_confirm.setVisibility(View.VISIBLE);
                animation_address_confirm.playAnimation();
                UsersService usersService = retrofitUserUpdate.create(UsersService.class);
                String new_address_user = edittext_address_cliente.getText().toString() + " " + edittext_mumber_cliente.getText().toString();
                String new_complement = edittext_complement_cliente.getText().toString();
                DtoUsers newAddress = new DtoUsers(new_address_user, new_complement);
                Call<DtoUsers> callUser = usersService.UpdateAddress(id_user, newAddress);
                callUser.enqueue(new Callback<DtoUsers>() {
                    @Override
                    public void onResponse(Call<DtoUsers> call, Response<DtoUsers> response) {
                        address_user = new_address_user;
                        complement = new_complement;
                        Toast.makeText(RegisterAddressActivity.this, "Endereço cadastrado com sucesso!!", Toast.LENGTH_SHORT).show();
                        GoBack_toMain();
                    }

                    @Override
                    public void onFailure(Call<DtoUsers> call, Throwable t) {
                        Toast.makeText(RegisterAddressActivity.this, "Error trying to register your address !!\nErro ao tentar registrar seu endereço!!", Toast.LENGTH_LONG).show();
                        GoBack_toMain();
                        txtBtnconfirm.setVisibility(View.VISIBLE);
                        animation_address_confirm.setVisibility(View.GONE);
                        animation_address_confirm.pauseAnimation();
                    }
                });
            }
        });


    }

    private void goToZipCode() {
        final Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipcode, 2);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Use the address as needed
                @SuppressLint("DefaultLocale") String message = String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude());
                latitude = address.getLatitude();
                longitude = address.getLongitude();
                // Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                // Display appropriate message when Geocoder services are not available
                Toast.makeText(this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            // handle exception
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in location and move the camera
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title(zipcode));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);
    }

    private void GetUserInformation() {
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
        Intent GoBack_ToMain = new Intent(RegisterAddressActivity.this, MainActivity.class);
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