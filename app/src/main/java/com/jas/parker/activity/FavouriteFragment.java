package com.jas.parker.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jas.parker.R;
import com.jas.parker.adapter.FavouriteAdapter;
import com.jas.parker.module.save.Favourite;
import com.jas.parker.type.Lot;

import java.util.ArrayList;


public class FavouriteFragment extends Fragment {

    private Favourite favourite;
    private FavouriteAdapter adapter;
    private ListView listFavourite;
    int testindex = 0; //will be delete

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_contact for this fragment


        try {
            view = inflater.inflate(R.layout.content_favourite, container, false);
        } catch (InflateException e) {

        }

        favourite = new Favourite();
        ArrayList<Lot> arrayOfLots = Lot.fromJson(favourite.getFavourites());
        adapter = new FavouriteAdapter(getActivity(), arrayOfLots);
        //改掉this

        listFavourite = (ListView) view.findViewById(R.id.listFavourite);

        listFavourite.setAdapter(adapter);
        registerForContextMenu(listFavourite);




        listFavourite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // String string = (String) parent.getAdapter().getItem(position);

            }
        });

        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.favourite_menu, menu);
    }

    //ListView Menu內選項的設定
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_info:
                new AlertDialog.Builder(getActivity())
                        .setTitle("停車場資訊")
                        .setMessage("名稱：" + adapter.getItem(info.position).getLotName() + "\n" +
                                "地址：" + adapter.getItem(info.position).getLotAddress()+ "\n" +
                                "收費資訊：" + adapter.getItem(info.position).getPayex() + "\n" +
                                "電話：" + adapter.getItem(info.position).getTel() + "\n")
                        .show();
                return true;
            case R.id.action_navigation:
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + adapter.getItem(info.position).getyAxis() + "," + adapter.getItem(info.position).getxAxis() + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                return true;
            case R.id.action_delete:
                adapter.delete(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
