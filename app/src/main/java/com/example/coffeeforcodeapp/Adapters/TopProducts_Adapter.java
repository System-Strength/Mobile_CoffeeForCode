package com.example.coffeeforcodeapp.Adapters;


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

public class TopProducts_Adapter extends RecyclerView.Adapter<TopProducts_Adapter.MyHolder>{
    ArrayList<DtoMenu> dtoMenusArrayList;

    public TopProducts_Adapter(ArrayList<DtoMenu> dtoMenusArrayList){
        this.dtoMenusArrayList = dtoMenusArrayList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_popular_products, parent, false);
        return new MyHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.txtStart_PopularProduct.setText(dtoMenusArrayList.get(position).getBonusDesc());
        holder.txtName_popularProduct.setText(dtoMenusArrayList.get(position).getNm_prod());
        holder.txtSize_popularProduct.setText(dtoMenusArrayList.get(position).getSize());
        holder.ImgPopularProduct.setImageBitmap(dtoMenusArrayList.get(position).getImg_prod());
    }


    @Override
    public int getItemCount() {
        return dtoMenusArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView ImgPopularProduct;
        TextView txtStart_PopularProduct, txtName_popularProduct, txtSize_popularProduct;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ImgPopularProduct = itemView.findViewById(R.id.ImgPopularProduct);
            txtStart_PopularProduct = itemView.findViewById(R.id.txtStart_PopularProduct);
            txtName_popularProduct = itemView.findViewById(R.id.txtName_popularProduct);
            txtSize_popularProduct = itemView.findViewById(R.id.txtSize_popularProduct);

        }
    }
}
