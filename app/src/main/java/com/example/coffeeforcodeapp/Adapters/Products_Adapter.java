package com.example.coffeeforcodeapp.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeforcodeapp.Api.DtoMenu;
import com.example.coffeeforcodeapp.R;

import java.util.ArrayList;

public class Products_Adapter extends RecyclerView.Adapter<Products_Adapter.MyHolderProducts> {
    ArrayList<DtoMenu> dtoMenuArrayList;

    public Products_Adapter(ArrayList<DtoMenu> dtoMenuArrayList) {
        this.dtoMenuArrayList = dtoMenuArrayList;
    }

    @NonNull
    @Override
    public MyHolderProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_products, parent, false);
        return new MyHolderProducts(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderProducts holder, int position) {
        holder.ImgProd.setImageBitmap(dtoMenuArrayList.get(position).getImg_prod());
        holder.txtNm_prod.setText(dtoMenuArrayList.get(position).getNm_prod());
        //holder.txtSize_Prod.setText(dtoMenuArrayList.get(position).getSize());
        holder.txtPrice_Prod.setText("R$ " + (int) dtoMenuArrayList.get(position).getPrice_prod());

    }

    @Override
    public int getItemCount() {
        return dtoMenuArrayList.size();
    }

    class MyHolderProducts extends RecyclerView.ViewHolder{
        TextView txtNm_prod, txtSize_Prod, txtPrice_Prod;
        ImageView ImgProd;

        public MyHolderProducts(@NonNull View itemView) {
            super(itemView);
            txtNm_prod = itemView.findViewById(R.id.txtNm_prod);
            //txtSize_Prod = itemView.findViewById(R.id.txtSize_Prod);
            txtPrice_Prod = itemView.findViewById(R.id.txtPrice_Prod);
            ImgProd = itemView.findViewById(R.id.ImgProd);
        }
    }
}
