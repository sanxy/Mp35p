package com.capitalsage.mp35p.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capitalsage.mp35p.R;
import com.capitalsage.mp35p.model.Frequent_BankList;

import java.math.BigDecimal;
import java.util.List;

public class FrequentBankListAdapter extends RecyclerView.Adapter<FrequentBankListAdapter.Frequent_bank_listViewHolder> {

    Context context;
    List<Frequent_BankList> banklisting;

    OnBankListener mOnBankListener;

    public FrequentBankListAdapter(Context context, List<Frequent_BankList> banklisting, OnBankListener onBankListener) {
        this.context = context;
        this.banklisting = banklisting;

        this.mOnBankListener = onBankListener;
    }

    @NonNull
    @Override
    public Frequent_bank_listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frequent_used_banks_menu, parent, false);
        return new Frequent_bank_listViewHolder(view,mOnBankListener);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull Frequent_bank_listViewHolder holder, int position) {
        // set the data in items

        Frequent_BankList item = banklisting.get(position);


        String bankName  = item.getBankName();
        String bankKey = item.getKeys();
        String bankCode = item.getBankCode();
        String bankImage = item.getBankLogoUrl();
        String bankID = item.getBankId();
        System.out.println(bankName);
        holder.bank_label.setText(bankName);
        Glide.with(context)
                .load(bankImage)
                .placeholder(R.drawable.nobankimage)
                .error(R.drawable.nobankimage)
                .into(holder.bank_image);
//        implement setOnClickListener event on item view.

    }

    private long customRound(Double value) {
        BigDecimal bigDecimalNumber = BigDecimal.valueOf(value);
        Integer intValue = bigDecimalNumber.intValue();
        BigDecimal decimalValue = bigDecimalNumber.subtract(BigDecimal.valueOf(intValue));

        if (decimalValue.compareTo(BigDecimal.valueOf(0.3)) > 0.3) {
            return intValue + 1;
        }
        return intValue;
    }

    @Override
    public int getItemCount() {
        return banklisting.size();
    }

    public static class Frequent_bank_listViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // init the item view's

        TextView bank_label;
        ImageView bank_image;
        OnBankListener onBankListener;
        public Frequent_bank_listViewHolder(View itemView,OnBankListener onBankListener) {
            super(itemView);


            // get the reference of item view's
            bank_label = itemView.findViewById(R.id.frequent_used_banks_label);
            bank_image = itemView.findViewById(R.id.frequent_used_banks_image);
            this.onBankListener = onBankListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onBankListener.OnBankClick(getAdapterPosition());
        }
    }

    public interface OnBankListener{
        void OnBankClick(int position);
    }
}
