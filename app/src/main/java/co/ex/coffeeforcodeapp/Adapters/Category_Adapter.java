package co.ex.coffeeforcodeapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import co.ex.coffeeforcodeapp.Api.Category.DtoCategorys;
import co.ex.coffeeforcodeapp.R;

import java.util.ArrayList;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.MyHolderCategory> {
    ArrayList<DtoCategorys> dtoCategorysArrayList;

    public Category_Adapter(ArrayList<DtoCategorys> dtoCategorysArrayList) {
        this.dtoCategorysArrayList = dtoCategorysArrayList;
    }


    @NonNull
    @Override
    public MyHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category, parent, false);
        return new MyHolderCategory(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderCategory holder, int position) {
        Picasso.get().load(dtoCategorysArrayList.get(position).getImg_cat_st()).into(holder.ImgCategory);
        //holder.ImgCategory.setImageBitmap(dtoCategorysArrayList.get(position).getImg_cat());
        holder.txtNm_Category.setText(dtoCategorysArrayList.get(position).getNm_cat());

    }

    @Override
    public int getItemCount() {
        return dtoCategorysArrayList.size();
    }

    class MyHolderCategory extends RecyclerView.ViewHolder{
        TextView txtNm_Category;
        ImageView ImgCategory;

        public MyHolderCategory(@NonNull View itemView) {
            super(itemView);
            txtNm_Category = itemView.findViewById(R.id.txtNm_Category);
            ImgCategory = itemView.findViewById(R.id.ImgCategory);
        }
    }
}
