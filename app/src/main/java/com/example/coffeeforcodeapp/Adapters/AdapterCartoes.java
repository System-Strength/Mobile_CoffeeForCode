package com.example.coffeeforcodeapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coffeeforcodeapp.DataBases.Cartoes.DtoCartoes;
import com.example.coffeeforcodeapp.R;

import java.util.List;

public class AdapterCartoes extends ArrayAdapter<DtoCartoes> {

    private final List<DtoCartoes> mlist;
    private final Context mcontext;

    public AdapterCartoes(@NonNull Context context, int resource, List<DtoCartoes> objects) {
        super(context, resource, objects);
        this.mlist = objects;
        this.mcontext = context;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(mcontext).inflate(R.layout.modelo_cartoes,null);
        }

        DtoCartoes dtoCartoes = mlist.get(position);

        TextView numerocartaocliente, nomeproprietariocartaocliente, txtbandeiracardlista;
        numerocartaocliente = view.findViewById(R.id.numerocartaocliente);
        nomeproprietariocartaocliente = view.findViewById(R.id.nomeproprietariocartaocliente);
        txtbandeiracardlista = view.findViewById(R.id.txtbandeiracardlista);

        String numerodocartaocompleto = dtoCartoes.getNumero() + " ";
        String[]  numerodocartao = numerodocartaocompleto.split(" ");
        String ultimonumero = numerodocartao[3];

        numerocartaocliente.setText("**** " + ultimonumero);
        nomeproprietariocartaocliente.setText(dtoCartoes.getNomedotitular());
        txtbandeiracardlista.setText(dtoCartoes.getBandeira());

        return view;

    }
}
