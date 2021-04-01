package co.ex.coffeeforcodeapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.ex.coffeeforcodeapp.Api.Orders.DtoOrders;
import co.ex.coffeeforcodeapp.R;

public class Orders_Adapter extends RecyclerView.Adapter<Orders_Adapter.MyHolderOrders> {
    ArrayList<DtoOrders> dtoOrdersArrayList;
    Context context;


    public Orders_Adapter(ArrayList<DtoOrders> dtoOrdersArrayList, Context context) {
        this.dtoOrdersArrayList = dtoOrdersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderOrders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_myorders, parent, false);
        return new MyHolderOrders(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderOrders holder, int position) {
        holder.cardFollowOrder.setElevation(10);
        holder.txt_held_in_date.setText(dtoOrdersArrayList.get(position).getHeld_in() + "");
        holder.txt_ordernumber.setText(dtoOrdersArrayList.get(position).getCd_order() + "");
        holder.txt_payment_type.setText(dtoOrdersArrayList.get(position).getPayFormat_user() + "");
        holder.txt_Status.setText(dtoOrdersArrayList.get(position).getStatus() + "");
    }

    @Override
    public int getItemCount() {
        return dtoOrdersArrayList.size();
    }

    static class MyHolderOrders extends RecyclerView.ViewHolder{
        TextView txt_ordernumber, txt_held_in_date, txt_payment_type, txt_Status;
        CardView cardFollowOrder;


        public MyHolderOrders(@NonNull View itemView) {
            super(itemView);
            txt_ordernumber = itemView.findViewById(R.id.txt_ordernumber);
            txt_held_in_date = itemView.findViewById(R.id.txt_held_in_date);
            txt_payment_type = itemView.findViewById(R.id.txt_payment_type);
            txt_Status = itemView.findViewById(R.id.txt_Status);
            cardFollowOrder = itemView.findViewById(R.id.cardFollowOrder);
        }
    }
}
