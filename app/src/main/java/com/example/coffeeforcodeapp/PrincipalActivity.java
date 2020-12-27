package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DtoClientes;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class PrincipalActivity extends AppCompatActivity {
    LottieAnimationView icone_perfil_principal;
    TextView txtnomedocliente;
    CardView cardviewnotpartner, cardbepartner, cardvercartoes, cardvercarrinhodecompra;
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    Dialog avisoendereco;
    private BottomSheetDialog bottomSheetDialog;
    String emaillogado, apareceravisoendereco, nomedousuario;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        txtnomedocliente = findViewById(R.id.txtnomedocliente);
        cardviewnotpartner = findViewById(R.id.cardviewnotpartner);
        cardbepartner = findViewById(R.id.cardbepartner);
        cardvercartoes = findViewById(R.id.cardvercartoes);
        cardvercarrinhodecompra = findViewById(R.id.cardvercarrinhodecompra);
        icone_perfil_principal = findViewById(R.id.icone_perfil_principal);
        avisoendereco = new Dialog(this);

        //  Get information for login of client
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null){
            apareceravisoendereco = "ativado";
        }else {
            emaillogado  = bundle.getString("emailuser");
            apareceravisoendereco = bundle.getString("statusavisoend");
        }

        //  Set somethings with gone and visible

        recebendodadosiniciaisdocliente();


        //  When click in this card user will to SejaParceiroActivity
        cardbepartner.setOnClickListener(v -> {
            Intent irparavirarparceiro = new Intent(PrincipalActivity.this,SejaParceiroActivity.class);
            irparavirarparceiro.putExtra("emailuser",emaillogado);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerdarapido, R.anim.mover_direitarapido);
            ActivityCompat.startActivity(PrincipalActivity.this,irparavirarparceiro, activityOptionsCompat.toBundle());
            finish();
        });

        //  When click in this card user will to CartoesActivity
        cardvercartoes.setOnClickListener(v -> {
            Intent irparacartoes = new Intent(PrincipalActivity.this,CartoesActivity.class);
            irparacartoes.putExtra("emailuser",emaillogado);
            startActivity(irparacartoes);
            finish();
        });

        //  When click in this card user will to cardvercarrinhodecompra
        cardvercarrinhodecompra.setOnClickListener(v -> {
            Intent vercarrinhodecompra = new Intent(PrincipalActivity.this,CarrinhoDeCompraActivity.class);
            vercarrinhodecompra.putExtra("emailuser", emaillogado);
            startActivity(vercarrinhodecompra);
            finish();

        });

        //  When click here will show info_perfil
        icone_perfil_principal.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(PrincipalActivity.this, R.style.BottomSheetTheme);

            View sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_sheet,
                    findViewById(R.id.menu_sheet_principal));

            //  When click in this linear will to profile information
            sheetview.findViewById(R.id.btnperfil).setOnClickListener(v1 -> {
                Intent irpara_perfil = new Intent(PrincipalActivity.this, PerfilActivity.class);
                irpara_perfil.putExtra("emailuser", emaillogado);
                startActivity(irpara_perfil);
                finish();
                bottomSheetDialog.dismiss();
            });

            //  When click in this linear will to app information
            sheetview.findViewById(R.id.btninfoapp).setOnClickListener(v1 -> {
                Toast.makeText(PrincipalActivity.this, "Em desenvolvimento!!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

            //  When click in this linear will to configuration of app
            sheetview.findViewById(R.id.btnconfig).setOnClickListener(v1 -> {
                Toast.makeText(PrincipalActivity.this, "Em desenvolvimento!!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

            //  When click in this linear will to LoginActivity
            sheetview.findViewById(R.id.btnlogout).setOnClickListener(v1 -> {
                AlertDialog.Builder aviso = new AlertDialog.Builder(PrincipalActivity.this);
                aviso.setTitle("Deslogar");
                aviso.setMessage("Deseja realmente deslogar?");
                aviso.setPositiveButton("Sim", (dialog, which) -> {
                    Intent voltaraologin = new Intent(PrincipalActivity.this, LoginActivity.class);
                    startActivity(voltaraologin);
                    finish();
                });
                aviso.setNegativeButton("Não", null);
                aviso.show();
            });

            TextView nomeusermenusheet;
            nomeusermenusheet = sheetview.findViewById(R.id.nomeusermenusheet);
            String[]  nomecliente = nomedousuario.split(" ");
            String primeironome = nomecliente[0];
            String segundonome = nomecliente[1];
            nomeusermenusheet.setText(primeironome + " " + segundonome);

            bottomSheetDialog.setContentView(sheetview);
            bottomSheetDialog.show();
        });
    }

    //  Create method to check customer first things
    private void recebendodadosiniciaisdocliente() {
        DaoClientes daoClientes = new DaoClientes(PrincipalActivity.this);
        DtoClientes dtoClientes = daoClientes.consultarclienteporemail(emaillogado);
        String nomeclientecompleto = dtoClientes.getNomecliente();
        String[]  nomecliente = nomeclientecompleto.split(" ");
        String primeironome = nomecliente[0];

        //  Set first name of client
        nomedousuario = dtoClientes.getNomecliente();
        txtnomedocliente.setText(primeironome);

        if (dtoClientes.getParceiro().equals("nao")){
            animacaonotpartner();
        }else {
            cardviewnotpartner.setVisibility(View.GONE);
        }

        if (dtoClientes.getEnderecocliente() == null || dtoClientes.getEnderecocliente().equals("")){
            if (apareceravisoendereco.equals("desativado")){
                avisoendereco.dismiss();
            }else {
                mostraavisoend();
            }
        }else{
            avisoendereco.dismiss();
        }
    }

    //  Create Method for show alert of no adress register
    private void mostraavisoend(){
        ConstraintLayout btncadastraragoraend, btncadastrardepoisend;
        avisoendereco.setContentView(R.layout.aviso_sem_endereco_cadatrado);
        avisoendereco.setCancelable(false);
        btncadastraragoraend = avisoendereco.findViewById(R.id.btncadastraragoraend);
        btncadastrardepoisend = avisoendereco.findViewById(R.id.btncadastrardepoisend);

        btncadastraragoraend.setOnClickListener(v -> {
           Intent ircadastrarendereco = new Intent(PrincipalActivity.this,CadastrarenderecoActivity.class);
           ircadastrarendereco.putExtra("emailuser",emaillogado);
           startActivity(ircadastrarendereco);
           finish();
        });

        btncadastrardepoisend.setOnClickListener(v -> avisoendereco.dismiss());

        avisoendereco.show();
    }

    //  Create Method for do animation in Not Partner Card
    private void  animacaonotpartner(){
        ConstraintLayout animationnotpartner, constraintdescnotpartner;
        animationnotpartner = findViewById(R.id.animationnotpartner);
        constraintdescnotpartner = findViewById(R.id.constraintdescnotpartner);

        cardviewnotpartner.setVisibility(View.VISIBLE);
        animationnotpartner.setVisibility(View.VISIBLE);
        constraintdescnotpartner.setVisibility(View.GONE);
        timer.postDelayed(() -> {
            animationnotpartner.setVisibility(View.GONE);
            constraintdescnotpartner.setVisibility(View.VISIBLE);
        },2950);
    }

}