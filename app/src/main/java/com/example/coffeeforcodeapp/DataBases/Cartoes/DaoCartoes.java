package com.example.coffeeforcodeapp.DataBases.Cartoes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class DaoCartoes extends SQLiteOpenHelper {
    private final String TABELA = "TB_CARTOES";

    public DaoCartoes(@Nullable Context context) { super(context, "DB_CARTOES", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //  Create DataBase for Cards
        String comando = "CREATE TABLE " + TABELA + "(" +
                "ID INTEGER PRIMARY KEY," +
                "CPFPROPRIETARIO CHAR(14) NOT NULL," +
                "BANDEIRA VARCHAR(20) ," +
                "NUMERO CHAR(19) NOT NULL," +
                "NOMETITULAR VARCHAR(100) NOT NULL," +
                "VALIDADE CHA(5) ," +
                "CCC CHAR(3) NOT NULL)";

        db.execSQL(comando);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //  Method create new card
    public long cadastrar_novo_cartao(DtoCartoes cartoes){
        ContentValues values = new ContentValues();
        values.put("CPFPROPRIETARIO", cartoes.getCpfproprietario());
        values.put("BANDEIRA", cartoes.getBandeira());
        values.put("NUMERO", cartoes.getNumero());
        values.put("NOMETITULAR", cartoes.getNomedotitular());
        values.put("VALIDADE", cartoes.getValidade());
        values.put("CCC", cartoes.getCcc());

        return getWritableDatabase().insert(TABELA, null, values);
    }

    //  Method to Search for card by cpf of client
    public ArrayList<DtoCartoes> consultar_cartao_porcpf(String cpf){
        String comando = "SELECT * FROM " + TABELA + " WHERE CPFPROPRIETARIO=?";
        String[] parametros = {cpf};
        Cursor cursor = getWritableDatabase().rawQuery(comando, parametros);
        ArrayList<DtoCartoes> cartoes = new ArrayList<>();

        while (cursor.moveToNext()){
            DtoCartoes dtoCartoes = new DtoCartoes();
            dtoCartoes.setId(cursor.getInt(0));
            dtoCartoes.setCpfproprietario(cursor.getString(1));
            dtoCartoes.setBandeira(cursor.getString(2));
            dtoCartoes.setNumero(cursor.getString(3));
            dtoCartoes.setNomedotitular(cursor.getString(4));
            dtoCartoes.setValidade(cursor.getString(5));
            dtoCartoes.setCcc(cursor.getString(6));

            cartoes.add(dtoCartoes);
        }
        return  cartoes;
    }

    //  Method to Search for card by id of client
    public DtoCartoes consultar_cartao_pelo_id(int id){
        String comando = "SELECT * FROM " + TABELA + " WHERE  ID=?";
        String[] parametros = {id+""};
        Cursor cursor = getWritableDatabase().rawQuery(comando, parametros);
        DtoCartoes dtoCartoes = new DtoCartoes();

        while (cursor.moveToNext()){
            dtoCartoes.setId(cursor.getInt(0));
            dtoCartoes.setCpfproprietario(cursor.getString(1));
            dtoCartoes.setBandeira(cursor.getString(2));
            dtoCartoes.setNumero(cursor.getString(3));
            dtoCartoes.setNomedotitular(cursor.getString(4));
            dtoCartoes.setValidade(cursor.getString(5));
            dtoCartoes.setCcc(cursor.getString(6));

        }
        return dtoCartoes;
    }

    //  Method to Search for delete card by id of client
    public  int excluir_cartao(DtoCartoes cartoes){
        String id = "id=?";
        String[] args  = {cartoes.getId()+""};
        return  getWritableDatabase().delete(TABELA,id,args);
    }
}
