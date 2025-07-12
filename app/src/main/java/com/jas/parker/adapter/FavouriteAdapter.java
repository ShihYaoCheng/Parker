package com.jas.parker.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jas.parker.R;
import com.jas.parker.type.Lot;
import com.jas.parker.module.save.Favourite;
import java.util.ArrayList;

/**
 * Created by alex2 on 2016/7/13.
 */
public class FavouriteAdapter extends ArrayAdapter<Lot> {
    private SparseBooleanArray mSelectedItemsIds;
    //紀錄被選擇的Item
    private ArrayList<Lot> lots;
    private Favourite favourite;

    public FavouriteAdapter(Context context, ArrayList<Lot> lots) {
        super(context, R.layout.custom_list_favourite, lots);
        mSelectedItemsIds = new SparseBooleanArray();
        this.lots = lots;
        favourite = new Favourite();
    }

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView address;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Lot lot = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_list_favourite, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.lotName);
            viewHolder.address = (TextView) convertView.findViewById(R.id.lotAddress);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(lot.getLotName());
        viewHolder.address.setText(lot.getLotAddress());
        // Return the completed view to render on screen

        return convertView;
    }

    public void remove(int position, int index) {
        lots.remove(position - index);
        // mSelectedItemsIds.delete(position);
        favourite.deleteFavourite(position-index);

        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public void clearSelectedIds(){  //當做資料刪除結束後必須把紀錄的SelectedIds刪除空
        mSelectedItemsIds.clear();
    }

    public void delete(int index){
        favourite.deleteFavourite(index); //刪除並寫入檔案紀錄
        lots.remove(index); //刪除顯示的lot listview
        notifyDataSetChanged();
    }
}

