package co.ex.coffeeforcodeapp.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import co.ex.coffeeforcodeapp.Api.Products.DtoMenu;
import co.ex.coffeeforcodeapp.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Products_Adapter extends RecyclerView.Adapter<Products_Adapter.MyHolderProducts> {
    ArrayList<DtoMenu> dtoMenuArrayList;

    public Products_Adapter(ArrayList<DtoMenu> dtoMenuArrayList) {
        this.dtoMenuArrayList = dtoMenuArrayList;
    }

    @NonNull
    @Override
    public MyHolderProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_products_products, parent, false);
        return new MyHolderProducts(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderProducts holder, int position) {
        Picasso.get().load(dtoMenuArrayList.get(position).getImg_prod_st()).into(holder.ImgProd);
        //holder.ImgProd.setImageBitmap(dtoMenuArrayList.get(position).getImg_prod());
        holder.txtNm_prod.setText(dtoMenuArrayList.get(position).getNm_prod());
        holder.txtCategory_Prod.setText(dtoMenuArrayList.get(position).getNm_cat());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        holder.txtPrice_Prod.setText("R$ " + numberFormat.format(dtoMenuArrayList.get(position).getPrice_prod()));

    }

    @Override
    public int getItemCount() {
        return dtoMenuArrayList.size();
    }

    class MyHolderProducts extends RecyclerView.ViewHolder{
        TextView txtNm_prod, txtCategory_Prod, txtPrice_Prod;
        ImageView ImgProd;

        public MyHolderProducts(@NonNull View itemView) {
            super(itemView);
            txtNm_prod = itemView.findViewById(R.id.txtNm_prod);
            txtCategory_Prod = itemView.findViewById(R.id.txtCategory_Prod);
            txtPrice_Prod = itemView.findViewById(R.id.txtPrice_Prod);
            ImgProd = itemView.findViewById(R.id.ImgProd);
        }
    }
}
