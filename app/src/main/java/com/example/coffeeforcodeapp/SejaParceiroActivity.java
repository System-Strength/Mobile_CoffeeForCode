package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.Adapters.LoadingDialog;
import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DtoClientes;
import com.example.coffeeforcodeapp.DataBases.Parceiro.DaoParceiro;
import com.example.coffeeforcodeapp.DataBases.Parceiro.DtoParceiro;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SejaParceiroActivity extends AppCompatActivity {
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    CardView btnserparceiro;
    ConstraintLayout primeirabasesejaparceito;
    ScrollView segundabasesejaparceito;
    TextView txttermosserparceito, txtproximabase, txtvoltar;
    EditText editnomeclienteserparceiro, editcpfclienteserparceiro, editemailclienteserparceiro;
    CheckBox checkboxtermosserparceiro;
    Dialog aviso;
    String emaillogado;
    //  Dados do Cartão
    String bandeira, numerodocartao, nomedotitular, datadevalidade, ccc;
    //  Dados Cartao CFC
    int numerodocartaocfc, nomedotitularcfc, datadevalidadecfc, ccccfc;
    //  Dados Cliente
    int id;
    LoadingDialog loadingDialog = new LoadingDialog(SejaParceiroActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seja_parceiro);
        primeirabasesejaparceito  = findViewById(R.id.primeirabasesejaparceito);
        segundabasesejaparceito  = findViewById(R.id.segundabasesejaparceito);
        checkboxtermosserparceiro  = findViewById(R.id.checkboxtermosserparceiro);
        txttermosserparceito  = findViewById(R.id.txttermosserparceito);
        txtproximabase  = findViewById(R.id.txtproximabase);
        txtvoltar  = findViewById(R.id.txtvoltar);
        btnserparceiro  = findViewById(R.id.btnserparceiro);
        editnomeclienteserparceiro  = findViewById(R.id.editnomeclienteserparceiro);
        editcpfclienteserparceiro  = findViewById(R.id.editcpfclienteserparceiro);
        editemailclienteserparceiro  = findViewById(R.id.editemailclienteserparceiro);
        aviso = new Dialog(this);

        //  Set Mask
        editcpfclienteserparceiro.addTextChangedListener(MaskEditUtil.mask(editcpfclienteserparceiro, MaskEditUtil.FORMAT_CPF));

        //  Set somethings with gone and visible
        primeirabasesejaparceito.setVisibility(View.VISIBLE);
        segundabasesejaparceito.setVisibility(View.GONE);

        //  Get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado  = bundle.getString("emailuser");

        animacaobaseum();

        //  When click here will show terms
        txttermosserparceito.setOnClickListener(v -> gerarcartaocfc());

        //  When click will show second base
        txtproximabase.setOnClickListener(v -> {
            primeirabasesejaparceito.setVisibility(View.GONE);
            segundabasesejaparceito.setVisibility(View.VISIBLE);
            carregardadoscliente();
        });

        //  When click will go back to first base
        txtvoltar.setOnClickListener(v -> {
            primeirabasesejaparceito.setVisibility(View.VISIBLE);
            segundabasesejaparceito.setVisibility(View.GONE);
        });

        //  When click here will try Create Signature
        btnserparceiro.setOnClickListener(v -> {
            if (!checkboxtermosserparceiro.isChecked()){
                Toast.makeText(this, "Obrigatório aceitar os termos!!", Toast.LENGTH_SHORT).show();
            }else if (bandeira == null || numerodocartao == null || nomedotitular == null || datadevalidade == null || ccc == null){
                Toast.makeText(this, "Não nenhum cartão selecionado, necessário selecionar um cartão.", Toast.LENGTH_SHORT).show();
            }else if (editnomeclienteserparceiro.getText() == null){
                Toast.makeText(this, "Seus dados pessoais não foram carregados tente reiniciar o aplicativo.", Toast.LENGTH_SHORT).show();
            }else {
                Date dataHoraAtual = new Date();
                @SuppressLint("SimpleDateFormat") String dataehora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataHoraAtual);
                loadingDialog.startLoading();
                DtoParceiro dtoParceiro = new DtoParceiro();
                dtoParceiro.setTermosaceito("sim");
                dtoParceiro.setNomecliente(editnomeclienteserparceiro.getText().toString());
                dtoParceiro.setCpfcliente(editcpfclienteserparceiro.getText().toString());
                dtoParceiro.setEmailcliente(editemailclienteserparceiro.getText().toString());
                dtoParceiro.setNumerocartao(numerodocartao);
                dtoParceiro.setData_ativacao(dataehora);
                DaoParceiro daoParceiro = new DaoParceiro(SejaParceiroActivity.this);
                try {
                    long linhasinseridas = daoParceiro.gerarassinatura(dtoParceiro);
                    if (linhasinseridas > 0){
                        try {
                            DtoClientes gerandoassinaturacliente = new DtoClientes();
                            gerandoassinaturacliente.setParceiro("sim");
                            gerandoassinaturacliente.setId(id);
                            DaoClientes clientes = new DaoClientes(SejaParceiroActivity.this);
                            long assinaturadefinida = clientes.atualizarparceira(gerandoassinaturacliente);
                            if (assinaturadefinida > 0){
                                timer.postDelayed(() -> {
                                    loadingDialog.dimissDialog();
                                    mostaravisoassinaturagaradocomsucesso();
                                },600);
                            }else {
                                Toast.makeText(this, "Ouve um problema em gerar sua assinatura em sua conta, por favor tente mais tarde.", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception ex){
                            Toast.makeText(this, "Erro: "+ ex, Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "Ouve um problema em gerar sua assinatura, por favor tente mais tarde.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(this, "Erro em gerar assinatura: "+ ex, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //  Create method to get client information
    private void carregardadoscliente(){
        if (editnomeclienteserparceiro.getText() == null || editnomeclienteserparceiro.getText().length() == 0 || editcpfclienteserparceiro.getText() == null || editcpfclienteserparceiro.getText().length() == 0 || editemailclienteserparceiro.getText() == null || editemailclienteserparceiro.getText().length() == 0){
            loadingDialog.startLoading();
            try {
                DaoClientes daoClientes = new DaoClientes(SejaParceiroActivity.this);
                DtoClientes dtoClientes = daoClientes.consultarclienteporemail(emaillogado);
                if (dtoClientes == null){
                    Toast.makeText(this, "Ouve algum problema em carregar seus dados, tente reiniciar o aplicativo e tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                    timer.postDelayed(() -> {
                        Intent voltaraoprincipal = new Intent(SejaParceiroActivity.this,PrincipalActivity.class);
                        voltaraoprincipal.putExtra("emailuser",emaillogado);
                        voltaraoprincipal.putExtra("statusavisoend","desativado");
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerdarapido, R.anim.mover_direitarapido);
                        ActivityCompat.startActivity(SejaParceiroActivity.this,voltaraoprincipal, activityOptionsCompat.toBundle());
                        finish();
                    },800);
                }else {
                    id = dtoClientes.getId();
                    editnomeclienteserparceiro.setText(dtoClientes.getNomecliente());
                    editcpfclienteserparceiro.setText(dtoClientes.getCpfcliente());
                    editemailclienteserparceiro.setText(dtoClientes.getEmailcliente());
                }
            }catch (Exception ex){
                Toast.makeText(this, "Ouve algum problema em carregar seus dados, tente reiniciar o aplicativo e tente novamente mais tarde. erro: "+ ex, Toast.LENGTH_LONG).show();
                timer.postDelayed(() -> {
                    Intent voltaraoprincipal = new Intent(SejaParceiroActivity.this,PrincipalActivity.class);
                    voltaraoprincipal.putExtra("emailuser",emaillogado);
                    voltaraoprincipal.putExtra("statusavisoend","desativado");
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerdarapido, R.anim.mover_direitarapido);
                    ActivityCompat.startActivity(SejaParceiroActivity.this,voltaraoprincipal, activityOptionsCompat.toBundle());
                    finish();
                },1000);
            }
            timer.postDelayed(() -> loadingDialog.dimissDialog(),500);
        }else {
            loadingDialog.dimissDialog();
        }
    }
    
    //  Method to control animation
    private void animacaobaseum(){
        LottieAnimationView animacaosacola, animacaobikedelivery, animacaoplus;
        animacaosacola = findViewById(R.id.animacaosacola);
        animacaobikedelivery = findViewById(R.id.animacaobikedelivery);
        animacaoplus = findViewById(R.id.animacaoplus);

        animacaosacola.setVisibility(View.VISIBLE);
        animacaosacola.playAnimation();

        timer.postDelayed(()-> {
            animacaosacola.setVisibility(View.GONE);
            animacaobikedelivery.setVisibility(View.VISIBLE);
            animacaobikedelivery.playAnimation();
            timer.postDelayed(() -> {
                animacaobikedelivery.setVisibility(View.GONE);
                animacaoplus.setVisibility(View.VISIBLE);
                animacaoplus.playAnimation();
            },2000);
        },3000);


    }

    //  Create method for show terms
    private void mostrartermosparaserparceiro() {
        CardView cardviewaceitoostermos;
        LottieAnimationView btnfechartermos;
        aviso.setContentView(R.layout.termos_ser_parceiro);
        aviso.setCancelable(false);
        cardviewaceitoostermos = aviso.findViewById(R.id.cardviewaceitoostermos);
        btnfechartermos = aviso.findViewById(R.id.btnfechartermos);

        cardviewaceitoostermos.setOnClickListener(v -> {
            checkboxtermosserparceiro.setChecked(true);
            aviso.dismiss();
        });

        btnfechartermos.setOnClickListener(v -> aviso.dismiss());

        aviso.show();
    }

    //  Create method for show subscription successfully generated
    private void mostaravisoassinaturagaradocomsucesso() {
        aviso.setContentView(R.layout.assinatura_gerada);
        aviso.setCancelable(false);

        timer.postDelayed(() -> {
            Intent voltaraoprincipal = new Intent(SejaParceiroActivity.this,PrincipalActivity.class);
            voltaraoprincipal.putExtra("emailuser",emaillogado);
            voltaraoprincipal.putExtra("statusavisoend","desativado");
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerdarapido, R.anim.mover_direitarapido);
            ActivityCompat.startActivity(SejaParceiroActivity.this,voltaraoprincipal, activityOptionsCompat.toBundle());
            finish();
            aviso.dismiss();
        },2800);

        aviso.show();
    }

    private void gerarcartaocfc(){
        Random random = new Random();
        numerodocartaocfc = random.nextInt(1000);
        // if (numerodocartaocf.to < )
        Toast.makeText(this, " "+ numerodocartaocfc, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (primeirabasesejaparceito.getVisibility() == View.VISIBLE){
            Intent voltaraoprincipal = new Intent(SejaParceiroActivity.this,PrincipalActivity.class);
            voltaraoprincipal.putExtra("emailuser",emaillogado);
            voltaraoprincipal.putExtra("statusavisoend","desativado");
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.mover_esquerdarapido, R.anim.mover_direitarapido);
            ActivityCompat.startActivity(SejaParceiroActivity.this,voltaraoprincipal, activityOptionsCompat.toBundle());
            finish();
        }else {
            primeirabasesejaparceito.setVisibility(View.VISIBLE);
            segundabasesejaparceito.setVisibility(View.GONE);
        }
    }
}