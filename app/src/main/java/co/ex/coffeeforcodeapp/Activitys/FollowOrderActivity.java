package co.ex.coffeeforcodeapp.Activitys;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.Orders.DtoOrders;
import co.ex.coffeeforcodeapp.Api.Orders.OrdersServices;
import co.ex.coffeeforcodeapp.Api.User.DtoUsers;
import co.ex.coffeeforcodeapp.Api.User.UsersService;
import co.ex.coffeeforcodeapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FollowOrderActivity extends FragmentActivity implements OnMapReadyCallback {
    CardView _btnGoBackMain_followorder;
    TextView _txtCd_order_followorder, _txt_address_followorder, _txt_deliveryForecast;
    ProgressBar _progress_state01, _progress_state02, _progress_state03, _progress_state04, _progress_state05, _progress_state06;

    private GoogleMap _mMap;
    LoadingDialog _loadingDialog = new LoadingDialog(FollowOrderActivity.this);

    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;

    //  Order information
    int cd_order;
    private double latitude = -23.5636351;
    private double longitude = -46.8130342;

    //  Retrofit's
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    final Retrofit retrofitOrder = new Retrofit.Builder()
            .baseUrl(baseurl + "orders/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/info/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_order);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        _btnGoBackMain_followorder = findViewById(R.id.btnGoBackMain_followorder);
        _txtCd_order_followorder = findViewById(R.id.txtCd_order_followorder);
        _txt_address_followorder = findViewById(R.id.txt_address_followorder);
        _txt_deliveryForecast = findViewById(R.id.txt_deliveryForecast);
        _progress_state01 = findViewById(R.id.progress_state01);
        _progress_state02 = findViewById(R.id.progress_state02);
        _progress_state03 = findViewById(R.id.progress_state03);
        _progress_state04 = findViewById(R.id.progress_state04);
        _progress_state05 = findViewById(R.id.progress_state05);
        _progress_state06 = findViewById(R.id.progress_state06);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        cd_order = bundle.getInt("cd_order");
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
        getOrderInformation();
        loadUserInformation();

        _txtCd_order_followorder.setText(cd_order + "");

        _btnGoBackMain_followorder.setOnClickListener(v -> finish());

    }

    public void loadUserInformation() {
        _loadingDialog.startLoading();
        UsersService usersService = retrofitUser.create(UsersService.class);
        Call<DtoUsers> call = usersService.infoUser(email_user);
        call.enqueue(new Callback<DtoUsers>() {
            @Override
            public void onResponse(@NotNull Call<DtoUsers> call, @NotNull Response<DtoUsers> response) {
                if (response.code() == 200) {
                    _loadingDialog.dimissDialog();
                    assert response.body() != null;
                    id_user = response.body().getId_user();
                    nm_user = response.body().getNm_user();
                    phone_user = response.body().getPhone_user();
                    cpf_user = response.body().getCpf_user();
                    partner = response.body().getPartner();
                    partner_Startdate = response.body().getPartner_Startdate();
                    address_user = response.body().getAddress_user();
                    complement = response.body().getComplement();
                    zipcode = response.body().getZipcode();
                    img_user = response.body().getImg_user();

                    final Geocoder geocoder = new Geocoder(FollowOrderActivity.this);
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(zipcode, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            // Use the address as needed
                            latitude = address.getLatitude();
                            longitude = address.getLongitude();
                            onMapReady(_mMap);
                        } else {
                            // Display appropriate message when Geocoder services are not available
                            latitude = -23.5636351;
                            longitude = -46.8130342;
                            onMapReady(_mMap);
                            Log.e("GeocoderStatus", "Address is null");
                        }
                    } catch (IOException e) {
                        latitude = -23.5636351;
                        longitude = -46.8130342;
                        onMapReady(_mMap);
                        Log.e("GeocoderStatus", e.toString());
                    }
                } else {
                    _loadingDialog.dimissDialog();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DtoUsers> call, @NotNull Throwable t) {
                _loadingDialog.dimissDialog();
                Log.d("GeoStatus", t.getMessage());
            }
        });
    }

    private void getOrderInformation() {
        OrdersServices ordersServices = retrofitOrder.create(OrdersServices.class);
        Call<DtoOrders> ordersCall = ordersServices.getOrderByCode(cd_order);
        ordersCall.enqueue(new Callback<DtoOrders>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DtoOrders> call, @NotNull Response<DtoOrders> response) {
                if (response.code() == 200) {
                    switch (Objects.requireNonNull(response.body()).getStatus()) {
                        case "Pendente":
                            _progress_state01.setProgress(100);
                            break;
                        case "Em preparo":
                            _progress_state01.setProgress(100);
                            _progress_state02.setProgress(100);
                            break;
                        case "Entregador à caminho":
                            _progress_state01.setProgress(100);
                            _progress_state02.setProgress(100);
                            _progress_state03.setProgress(100);
                            break;
                        case "Entregue":
                            _progress_state01.setProgress(100);
                            _progress_state02.setProgress(100);
                            _progress_state03.setProgress(100);
                            _progress_state04.setProgress(100);
                            _progress_state05.setProgress(100);
                            break;
                        case "Concluído":
                            _progress_state01.setProgress(100);
                            _progress_state02.setProgress(100);
                            _progress_state03.setProgress(100);
                            _progress_state04.setProgress(100);
                            _progress_state05.setProgress(100);
                            _progress_state06.setProgress(100);
                            _txtCd_order_followorder.setText(cd_order + " (Concluído)");
                            break;
                    }

                    String fullAddress = response.body().getAddress_user() + ", " + response.body().getComplement() + ", " + response.body().getZipcode();
                    _txt_address_followorder.setText(fullAddress);
                    if (response.body().getStatus().equals("Concluído")) {
                        _txt_deliveryForecast.setText(R.string.orderdelivered);
                    } else {
                        _txt_deliveryForecast.setText(response.body().getDelivery_time());
                    }
                } else if (response.code() == 410) {
                    Toast.makeText(FollowOrderActivity.this, R.string.order_not_found, Toast.LENGTH_SHORT).show();
                    finish();

                } else if (response.code() == 500) {
                    Toast.makeText(FollowOrderActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DtoOrders> call, @NotNull Throwable t) {
                Toast.makeText(FollowOrderActivity.this, R.string.wehaveaproblem, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        _mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(latitude, longitude);
        _mMap.clear();
        _mMap.addMarker(new MarkerOptions().position(location).title("Pedido: " + cd_order));
        _mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        _mMap.isMyLocationEnabled();
        _mMap.setMyLocationEnabled(true);
    }
}