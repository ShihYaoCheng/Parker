package com.jas.parker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jas.parker.R;
import com.jas.parker.module.save.History;
import com.jas.parker.type.Lot;

import java.util.ArrayList;

/**
 * Created by alex2 on 2016/7/20.
 */
public class HistoryAdapter extends ArrayAdapter<Lot> {
    private ArrayList<Lot> lots;
    private History history;

    public HistoryAdapter(Context context, ArrayList<Lot> lots) {
        super(context, R.layout.custom_list_history, lots);
        this.lots = lots;
        history = new History();
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
            convertView = inflater.inflate(R.layout.custom_list_history, parent, false);
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

    public void delete(int index){
        history.deleteHistory(index); //刪除並寫入檔案紀錄
        lots.remove(index); //刪除顯示的lot listview
        notifyDataSetChanged();
    }

    public void deleteAll(){
        history.clearHistorys();
        lots.clear();
        notifyDataSetChanged();
    }

}
