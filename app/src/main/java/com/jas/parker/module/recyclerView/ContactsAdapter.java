package com.jas.parker.module.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jas.parker.R;

import java.util.List;

/**
 * Created by bluej on 2016/10/22.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView;
        public ViewHolder(View itemView){
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
    private List<Contact> mContacts;

    public ContactsAdapter(List<Contact> contacts){
        mContacts = contacts;
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View contactView = LayoutInflater.from(context).inflate(R.layout.item_contact, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Contact contact = mContacts.get(position);
        TextView nameTextView = viewHolder.nameTextView;
        nameTextView.setText(contact.getName());
    }

}