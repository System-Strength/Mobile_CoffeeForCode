package co.ex.coffeeforcodeapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.ex.coffeeforcodeapp.Api.Card.DtoCard;
import co.ex.coffeeforcodeapp.R;

public class CardsPurchase_Adapter extends RecyclerView.Adapter<CardsPurchase_Adapter.MyHolderCards> {
    ArrayList<DtoCard> dtoCardArrayList;
    Context context;
    static int selectedItem = 0;
    //  Flags Images
    String flag_visa = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fvisa.png?alt=media&token=e9dd2e2b-dd30-444e-b745-2a1dd2273db9";
    String flag_mastercard = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmastercard.png?alt=media&token=79df43fd-494c-4160-93f1-7194266f76b9";
    String flag_maestro = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmaestro.png?alt=media&token=28fd5789-f277-4027-8b0f-9ff1f38b2d5d";



    public CardsPurchase_Adapter(ArrayList<DtoCard> dtoCardArrayList, Context context) {
        this.dtoCardArrayList = dtoCardArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyHolderCards onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cards_purchase, parent, false);
        return new MyHolderCards(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderCards holder, int position) {
        switch (dtoCardArrayList.get(position).getFlag_card()) {
            case "MasterCard":
                Picasso.get().load(flag_mastercard).into(holder.flag_card_purchase);
                holder.cardCardList_purchase.setCardBackgroundColor(context.getColor(R.color.mastercard));
                break;
            case "Visa":
                Picasso.get().load(flag_visa).into(holder.flag_card_purchase);
                holder.cardCardList_purchase.setCardBackgroundColor(context.getColor(R.color.visacard));
                break;
            case "Maestro":
                Picasso.get().load(flag_maestro).into(holder.flag_card_purchase);
                holder.cardCardList_purchase.setCardBackgroundColor(context.getColor(R.color.maestrocard));
                break;
        }
        if (position == selectedItem){
            //  Make card selected
            holder.baseNotCheck_purchaseCard.setVisibility(View.GONE);
            holder.baseCheck_purchaseCard.setVisibility(View.VISIBLE);
        }else{
            holder.baseNotCheck_purchaseCard.setVisibility(View.VISIBLE);
            holder.baseCheck_purchaseCard.setVisibility(View.GONE);
        }

        holder.cardCardList_purchase.setElevation(20);
        String fullNumber = dtoCardArrayList.get(position).getNumber_card();
        String[]  SelectNumber = fullNumber.split(" ");
        String LastNumbers = SelectNumber[3];
        holder.txt_numberCard_purchase.setText("•••• •••• •••• " + LastNumbers);
    }

    @Override
    public int getItemCount() {
        return dtoCardArrayList.size();
    }

    class MyHolderCards extends RecyclerView.ViewHolder{
        TextView txt_numberCard_purchase;
        ImageView flag_card_purchase;
        CardView cardCardList_purchase;
        ConstraintLayout baseCheck_purchaseCard, baseNotCheck_purchaseCard;


        public MyHolderCards(@NonNull View itemView) {
            super(itemView);
            txt_numberCard_purchase = itemView.findViewById(R.id.txt_numberCard_purchase);
            flag_card_purchase = itemView.findViewById(R.id.flag_card_purchase);
            cardCardList_purchase = itemView.findViewById(R.id.cardCardList_purchase);
            baseCheck_purchaseCard = itemView.findViewById(R.id.baseCheck_purchaseCard);
            baseNotCheck_purchaseCard = itemView.findViewById(R.id.baseNotCheck_purchaseCard);

            cardCardList_purchase.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }
}
