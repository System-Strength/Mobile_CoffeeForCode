package com.example.coffeeforcodeapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.coffeeforcodeapp.R;

public class LoadingDialog {

    private final Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity myactivity){
        activity = myactivity;
    }

    @SuppressLint("InflateParams")
    public void  startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading,null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

   public void dimissDialog(){
        dialog.dismiss();
    }
}
