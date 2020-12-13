package com.example.coffeeforcodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeforcodeapp.Adapters.LoadingDialog;
import com.example.coffeeforcodeapp.DataBases.Clientes.DaoClientes;
import com.example.coffeeforcodeapp.DataBases.Clientes.DtoClientes;

public class CadastrarenderecoActivity extends AppCompatActivity {
    @SuppressWarnings("deprecation")
    Handler timer = new Handler();
    LottieAnimationView animationenderecoconfirm, animationenderecodepois;
    TextView txtbtnconfirmar;
    EditText edittextenderecocliente, edittextenderecoclientecomp;
    CardView cardviewenderecoconfirmar, cardviewenderecodepois;
    String emaillogado;
    int  iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrarendereco);
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emaillogado = bundle.getString("emailuser");

        //  Set somethings with gone and visible
        animationenderecoconfirm.setVisibility(View.GONE);
        animationenderecodepois.setVisibility(View.GONE);
        txtbtnconfirmar.setVisibility(View.VISIBLE);

        recebendo_dados_iniciais_do_cliente();

        //  When click at this, it will trying to register address
        cardviewenderecoconfirmar.setOnClickListener(v -> {
            if (edittextenderecocliente.getText() == null ||edittextenderecocliente.getText().length() <3){
                Toast.makeText(CadastrarenderecoActivity.this, "Preencha corretamente o campo: Endereço", Toast.LENGTH_SHORT).show();
                edittextenderecocliente.requestFocus();
                imm.showSoftInput(edittextenderecocliente, InputMethodManager.SHOW_IMPLICIT);
            }else {
                try {
                    txtbtnconfirmar.setVisibility(View.GONE);
                    animationenderecoconfirm.setVisibility(View.VISIBLE);
                    animationenderecoconfirm.playAnimation();
                    edittextenderecocliente.setEnabled(false);
                    edittextenderecoclientecomp.setEnabled(false);
                    timer.postDelayed(() -> {
                        DtoClientes dtoClientes = new DtoClientes();
                        dtoClientes.setId(iduser);
                        dtoClientes.setEnderecocliente(edittextenderecocliente.getText().toString());
                        dtoClientes.setComplementocliente(edittextenderecoclientecomp.getText().toString());
                        DaoClientes daoClientes = new DaoClientes(CadastrarenderecoActivity.this);
                        long linhasinseridas = daoClientes.atualizarendereco(dtoClientes);
                        if (linhasinseridas > 0){
                            Toast.makeText(this, "Endereço cadastrado com sucesso!!", Toast.LENGTH_SHORT).show();
                            Intent voltarcadastrarendereco = new Intent(CadastrarenderecoActivity.this,PrincipalActivity.class);
                            voltarcadastrarendereco.putExtra("emailuser",emaillogado);
                            voltarcadastrarendereco.putExtra("statusavisoend","desativado");
                            startActivity(voltarcadastrarendereco);
                            finish();
                        }else {
                            txtbtnconfirmar.setVisibility(View.VISIBLE);
                            animationenderecoconfirm.setVisibility(View.GONE);
                            animationenderecoconfirm.pauseAnimation();
                            edittextenderecocliente.setEnabled(true);
                            edittextenderecoclientecomp.setEnabled(true);
                            Toast.makeText(this, "Ouve algum problema em cadastrar seu endereço\\nTente novamente mais tarde!!", Toast.LENGTH_SHORT).show();
                        }
                    },2000);
                }catch (Exception ex){
                    Toast.makeText(CadastrarenderecoActivity.this, "Erro em cadastrar endereço: "+ex, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //  When click here will to PrincipalActivity
        cardviewenderecodepois.setOnClickListener(v -> {

            Intent voltandoparaprincipal = new Intent(CadastrarenderecoActivity.this,PrincipalActivity.class);
            voltandoparaprincipal.putExtra("emailuser",emaillogado);
            voltandoparaprincipal.putExtra("statusavisoend","desativado");
            startActivity(voltandoparaprincipal);
            finish();

        });

    }

    private void recebendo_dados_iniciais_do_cliente() {
        DaoClientes daoClientes = new DaoClientes(CadastrarenderecoActivity.this);
        DtoClientes dtoClientes = daoClientes.consultarclienteporemail(emaillogado);
        iduser = dtoClientes.getId();
    }

    @Override
    public void onBackPressed() {
        Intent voltarcadastrarendereco = new Intent(CadastrarenderecoActivity.this,PrincipalActivity.class);
        voltarcadastrarendereco.putExtra("emailuser",emaillogado);
        voltarcadastrarendereco.putExtra("statusavisoend","desativado");
        startActivity(voltarcadastrarendereco);
        finish();
    }
}