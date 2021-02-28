package com.example.coffeeforcodeapp.LocalDataBases.Parceiro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DaoParceiro extends SQLiteOpenHelper {
    private final String TABELA = "TB_PARCEIRO";

    public DaoParceiro(@Nullable Context context) { super(context, "DB_PARCEIRO", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //  Create DataBase for Partner
        String comando = "CREATE TABLE " + TABELA + "(" +
                "ID INTEGER PRIMARY KEY," +
                "NOMECLIENTE VARCHAR(100) NOT NULL," +
                "CPFCLI VARCHAR(14) NOT NULL," +
                "EMAILCLI VARCHAR(50) NOT NULL," +
                "NUMEROCARTAO VARCHAR(19) NOT NULL," +
                "CCCCARTAO CHAR(3) NOT NULL," +
                "DATA_ATIVACAO DATETIME NOT NULL," +
                "DATA_CANCELAMENTO DATETIME ," +
                "TERMOSACEITO CHAR(3) NOT NULL)" ;

        db.execSQL(comando);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //  Method create new partner in database
    public long gerarassinatura(DtoParceiro parceiro){
        ContentValues values = new ContentValues();
        values.put("NOMECLIENTE", parceiro.getNomecliente());
        values.put("CPFCLI", parceiro.getCpfcliente());
        values.put("EMAILCLI", parceiro.getEmailcliente());
        values.put("NUMEROCARTAO", parceiro.getNumerocartao());
        values.put("CCCCARTAO", parceiro.getCcccartaocfc());
        values.put("DATA_ATIVACAO", parceiro.getData_ativacao());
        values.put("DATA_CANCELAMENTO", parceiro.getData_cancelamento());
        values.put("TERMOSACEITO", parceiro.getTermosaceito());

        return getWritableDatabase().insert(TABELA, null, values);
    }

    public DtoParceiro consultarclienteporcpf(String cpf){
        String comando = "SELECT * FROM " + TABELA + " WHERE  CPFCLI=?";
        String[] parametros = {cpf};
        Cursor cursor = getWritableDatabase().rawQuery(comando, parametros);
        DtoParceiro dtoParceiro = new DtoParceiro();

        while (cursor.moveToNext()){
            dtoParceiro.setId(cursor.getInt(0));
            dtoParceiro.setNomecliente(cursor.getString(1));
            dtoParceiro.setCpfcliente(cursor.getString(2));
            dtoParceiro.setEmailcliente(cursor.getString(3));
            dtoParceiro.setNumerocartao(cursor.getString(4));
            dtoParceiro.setCcccartaocfc(cursor.getString(5));
            dtoParceiro.setData_ativacao(cursor.getString(6));
            dtoParceiro.setData_cancelamento(cursor.getString(7));
            dtoParceiro.setTermosaceito(cursor.getString(8));

        }
        return dtoParceiro;
    }
}
