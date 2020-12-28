package com.example.coffeeforcodeapp.DataBases.Clientes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

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
                "PARCEIRO CHAR(3) ," +
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
        values.put("PARCEIRO", clientes.getParceiro());
        values.put("SENHACLI", clientes.getSenhacliente());

        return getWritableDatabase().insert(TABELA, null, values);
    }

    public ArrayList<DtoClientes> consultarTodos(){
        String comando = "SELECT * FROM " + TABELA;
        Cursor cursor = getWritableDatabase().rawQuery(comando, null);
        ArrayList<DtoClientes> ArrayListclientes = new ArrayList<>();

        while (cursor.moveToNext()){
            DtoClientes dtoClientes = new DtoClientes();
            dtoClientes.setId(cursor.getInt(0));
            dtoClientes.setNomecliente(cursor.getString(1));
            dtoClientes.setCpfcliente(cursor.getString(2));
            dtoClientes.setEmailcliente(cursor.getString(3));
            dtoClientes.setCelularcliente(cursor.getString(4));
            dtoClientes.setEnderecocliente(cursor.getString(5));
            dtoClientes.setComplementocliente(cursor.getString(6));
            dtoClientes.setParceiro(cursor.getString(7));
            dtoClientes.setSenhacliente(cursor.getString(8));

            ArrayListclientes.add(dtoClientes);
        }
        return  ArrayListclientes;
    }

    public DtoClientes consultarclienteporcpf(String cpf){
        String comando = "SELECT * FROM " + TABELA + " WHERE  EMAILCLI=?";
        String[] parametros = {cpf};
        Cursor cursor = getWritableDatabase().rawQuery(comando, parametros);
        DtoClientes dtoClientes = new DtoClientes();

        while (cursor.moveToNext()){
            dtoClientes.setId(cursor.getInt(0));
            dtoClientes.setNomecliente(cursor.getString(1));
            dtoClientes.setCpfcliente(cursor.getString(2));
            dtoClientes.setEmailcliente(cursor.getString(3));
            dtoClientes.setCelularcliente(cursor.getString(4));
            dtoClientes.setEnderecocliente(cursor.getString(5));
            dtoClientes.setComplementocliente(cursor.getString(6));
            dtoClientes.setParceiro(cursor.getString(7));
            dtoClientes.setSenhacliente(cursor.getString(8));

        }
        return dtoClientes;
    }


    public DtoClientes consultarclienteporemail(String email){
        String comando = "SELECT * FROM " + TABELA + " WHERE  EMAILCLI=?";
        String[] parametros = {email};
        Cursor cursor = getWritableDatabase().rawQuery(comando, parametros);
        DtoClientes dtoClientes = new DtoClientes();

        while (cursor.moveToNext()){
            dtoClientes.setId(cursor.getInt(0));
            dtoClientes.setNomecliente(cursor.getString(1));
            dtoClientes.setCpfcliente(cursor.getString(2));
            dtoClientes.setEmailcliente(cursor.getString(3));
            dtoClientes.setCelularcliente(cursor.getString(4));
            dtoClientes.setEnderecocliente(cursor.getString(5));
            dtoClientes.setComplementocliente(cursor.getString(6));
            dtoClientes.setParceiro(cursor.getString(7));
            dtoClientes.setSenhacliente(cursor.getString(8));

        }
        return dtoClientes;
    }

    public int atualizarparceira(DtoClientes clientes){
        ContentValues values = new ContentValues();
        values.put("PARCEIRO", clientes.getParceiro());

        String id = "id=?";
        String[] args = {clientes.getId()+""};

        return getWritableDatabase().update(TABELA,values,id,args);
    }

    public int atualizarendereco(DtoClientes clientes){
        ContentValues values = new ContentValues();
        values.put("ENDERECOCLI", clientes.getEnderecocliente());
        values.put("COMPLEMENTO", clientes.getComplementocliente());

        String id = "id=?";
        String[] args = {clientes.getId()+""};

        return getWritableDatabase().update(TABELA,values,id,args);
    }

    public int atualizar_dados(DtoClientes clientes){
        ContentValues values = new ContentValues();

        String id = "id=?";
        String[] args = {clientes.getId()+""};

        return getWritableDatabase().update(TABELA,values,id,args);
    }
}
