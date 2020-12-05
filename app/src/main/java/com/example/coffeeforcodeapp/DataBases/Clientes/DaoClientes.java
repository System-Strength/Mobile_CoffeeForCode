package com.example.coffeeforcodeapp.DataBases.Clientes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DaoClientes extends SQLiteOpenHelper {
    private final String TABELA ="TB_CLIENTES";

    public DaoClientes(@Nullable Context context) { super(context, "DB_CLIENTES", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //  Create DataBase for Clients
        String comando = "CREATE TABLE " + TABELA + "(" +
                "ID INTEGER PRIMARY KEY," +
                "NOMECLIENTE VARCHAR(100) NOT NULL," +
                "CPFCLI VARCHAR(14) NOT NULL," +
                "EMAILCLI VARCHAR(50) NOT NULL," +
                "CELULARCLI VARCHAR(15) ," +
                "ENDERECOCLI VARCHAR(50) ," +
                "COMPLEMENTO VARCHAR(50) ," +
                "SENHACLI VARCHAR(28) NOT NULL)";

        db.execSQL(comando);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //  Method login in database
    public boolean onLogin(String email,String senha){
        String comando = "SELECT * FROM " + TABELA + " WHERE EMAILCLI =? and SENHACLI =?  ";
        String[] parametros = {email,senha};
        Cursor cursor = getWritableDatabase().rawQuery(comando, parametros);

        return cursor.moveToNext();
    }

    //  Method create new account in database
    public long cadastrar(DtoClientes clientes){
        ContentValues values = new ContentValues();
        values.put("NOMECLIENTE", clientes.getNomecliente());
        values.put("CPFCLI", clientes.getCpfcliente());
        values.put("EMAILCLI", clientes.getEmailcliente());
        values.put("CELULARCLI", clientes.getCelularcliente());
        values.put("ENDERECOCLI", clientes.getEnderecocliente());
        values.put("COMPLEMENTO", clientes.getComplementocliente());
        values.put("SENHACLI", clientes.getSenhacliente());

        return getWritableDatabase().insert(TABELA, null, values);
    }
}
