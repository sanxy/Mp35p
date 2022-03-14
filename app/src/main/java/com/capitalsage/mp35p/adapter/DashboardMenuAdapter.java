package com.capitalsage.mp35p.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.capitalsage.mp35p.activities.MainActivity;
import com.capitalsage.mp35p.R;
import com.capitalsage.mp35p.model.DashboardMenu;

import java.util.List;

public class DashboardMenuAdapter extends RecyclerView.Adapter<DashboardMenuAdapter.MyViewHolder> {

    Context context;
    private final List<DashboardMenu> transactions;

    public DashboardMenuAdapter(Context context, List<DashboardMenu> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public DashboardMenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_menu, parent, false);
        return new MyViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DashboardMenuAdapter.MyViewHolder holder, int position) {
        // set the data in items

        try{
            DashboardMenu item = transactions.get(position);
            //NumberFormat.getCurrencyInstance();

            String name = item.getText();

            holder.cardViewType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) v.getContext()).onClickCalled(name);
                    Log.i("Name", "name:: "+name);
                }
            });

            holder.title.setText(name);

            if (name.contains("Withdrawal")){
//                holder.cardViewType.setCardBackgroundColor(context.getColor(R.color.bills_payment));
                holder.titleImage.setImageResource(R.drawable.ic_withdrawal);
            }
            else if (name.contains("Transfer")){
//                holder.cardViewType.setCardBackgroundColor(context.getColor(R.color.data));
                holder.titleImage.setImageResource(R.drawable.ic_credit_card);
            }
            else if (name.contains("Airtime")){
//                holder.cardViewType.setCardBackgroundColor(context.getColor(R.color.data));
                holder.titleImage.setImageResource(R.drawable.ic_telephone);
            }
            else if (name.contains("Data")){
//                holder.cardViewType.setCardBackgroundColor(context.getColor(R.color.data));
                holder.titleImage.setImageResource(R.drawable.ic_telephone);
            }
            else if (name.contains("Paybill")){
//                holder.cardViewType.setCardBackgroundColor(context.getColor(R.color.data));
                holder.titleImage.setImageResource(R.drawable.ic_payment_option);
            }
            else if (name.contains("History")){
//                holder.cardViewType.setCardBackgroundColor(context.getColor(R.color.data));
                holder.titleImage.setImageResource(R.drawable.ic_history);
            }
            else if (name.contains("Disputes")){
//                holder.cardViewType.setCardBackgroundColor(context.getColor(R.color.data));
                holder.titleImage.setImageResource(R.drawable.ic_dispute);
            }
            else if (name.contains("Balance")){
//                holder.cardViewType.setCardBackgroundColor(context.getColor(R.color.data));
                holder.titleImage.setImageResource(R.drawable.ic_balance);
            }

        }catch (Exception e){
            Log.i("LastTrans", "Error: " + e.getMessage());
        }

        // implement setOnClickListener event on item view.

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView title;
        CardView cardViewType;
        ImageView titleImage;
        LinearLayout rootView;
        ConstraintLayout rootView2;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            title = itemView.findViewById(R.id.grid_view_item_text);
            titleImage = itemView.findViewById(R.id.gridview_item_image);
            cardViewType = itemView.findViewById(R.id.card_view_grid_view);
            rootView = itemView.findViewById(R.id.linear_layout);
            rootView2 = itemView.findViewById(R.id.root_view);

        }

    }
}
