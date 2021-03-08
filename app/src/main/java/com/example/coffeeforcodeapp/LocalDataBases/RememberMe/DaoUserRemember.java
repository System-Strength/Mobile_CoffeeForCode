package com.example.coffeeforcodeapp.LocalDataBases.RememberMe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DaoUserRemember extends SQLiteOpenHelper {
    private final String TABLE = "TB_USER_REMEMBER";

    public DaoUserRemember(@Nullable Context context) { super(context, "DB_REMEMBER_USER", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  Create DataBase for REMEMBER_USER
        String comand = "CREATE TABLE " + TABLE + "(" +
                "email VARCHAR(120) NOT NULL," +
                "password VARCHAR(125) NOT NULL)";

        db.execSQL(comand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DtoUserRemember get_user_login(){
        String comand = " SELECT * FROM " + TABLE;
        Cursor cursor = getWritableDatabase().rawQuery(comand, null);
        DtoUserRemember userRemember = new DtoUserRemember();
        while (cursor.moveToNext()){
            userRemember.setEmail(cursor.getString(1));
            userRemember.setPassword(cursor.getString(2));
        }
        return userRemember;
    }

    public long register_user_remember(DtoUserRemember userRemember){
        ContentValues values = new ContentValues();
        values.put("email", userRemember.getEmail());
        values.put("password", userRemember.getPassword());

        return getWritableDatabase().insert(TABLE, null, values);
    }

    //  Method to Delete Remember User
    public  int delete_remember(DtoUserRemember userRemember){
        String email = "email=?";
        String[] args  = {userRemember. getEmail()};
        return  getWritableDatabase().delete(TABLE,email,args);
    }
}
