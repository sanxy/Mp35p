package com.capitalsage.mp35p.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.capitalsage.mp35p.R;
import com.capitalsage.mp35p.activities.CardTransferActivity;
import com.capitalsage.mp35p.model.BankList;
import java.util.ArrayList;


public class BankListAdapter extends ArrayAdapter<BankList> implements Filterable {
    LayoutInflater inflater;
    ArrayList<BankList> objects;
    ArrayList<BankList> itemsModelListFiltered;
    ViewHolder holder = null;


    public BankListAdapter(Context context, int textViewResourceId, ArrayList<BankList> objects) {
        super(context, textViewResourceId, objects);

        inflater = ((Activity) context).getLayoutInflater();

        this.objects = objects;
        this.itemsModelListFiltered = objects;
    }

    @Override
    public int getCount() {
        return itemsModelListFiltered == null ? 0 : itemsModelListFiltered.size();
    }

    @Override
    public BankList getItem(int position) {
        return itemsModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.spinner_bank_list_custom_layout, parent, false);
            holder.name = row.findViewById(R.id.textView);
            holder.img = row.findViewById(R.id.imageView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.name.setText(itemsModelListFiltered.get(position).getBankName());

        Glide.with(row.getContext())
                .load(itemsModelListFiltered.get(position).getBankLogoUrl()) // image url
                .placeholder(R.drawable.nobankimage) // any placeholder to load at start
                .error(R.drawable.nobankimage)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(holder.img);

        return row;
    }

    static class ViewHolder {
        TextView name;
        ImageView img;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = objects.size();
                    filterResults.values = objects;

                } else {
                    ArrayList<BankList> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for (BankList itemsModel : objects) {
                        if (itemsModel.getBankName().toLowerCase().contains(searchStr)) {
                            resultsModel.add(itemsModel);
                            filterResults.count = resultsModel.size();
                            filterResults.values = resultsModel;
                        }

                    }

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                itemsModelListFiltered = (ArrayList<BankList>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}
