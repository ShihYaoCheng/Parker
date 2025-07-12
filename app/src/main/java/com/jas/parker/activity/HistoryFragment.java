package com.jas.parker.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jas.parker.R;
import com.jas.parker.adapter.HistoryAdapter;
import com.jas.parker.module.save.Favourite;
import com.jas.parker.module.save.History;
import com.jas.parker.type.Lot;

import java.util.ArrayList;

/**
 * Created by alex2 on 2016/6/30.
 */
public class HistoryFragment extends Fragment {
    private History history;
    private Favourite favourite;
    private HistoryAdapter adapter;
    private ListView listHistory;

    int testindex = 0; //will be delete


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_contact for this fragment


        try {
            view = inflater.inflate(R.layout.content_history, container, false);
        } catch (InflateException e) {

        }


        history = new History();
        favourite = new Favourite();
        ArrayList<Lot> arrayOfLots = Lot.fromJson(history.getHistorys());
        adapter = new HistoryAdapter(getActivity(), arrayOfLots);
        listHistory = (ListView) view.findViewById(R.id.listHistory);

        listHistory.setAdapter(adapter);
        registerForContextMenu(listHistory);
/*
        final Button testAdd = (Button) view.findViewById(R.id.button2);
        testAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                String testJson = "{\"id\":653,\"originalId\":\"010036\",\"taiwanCity\":\"新北市\",\"taiwanArea\":\"板橋區\",\"name\":\"特專三臨時平面停車場\",\"type\":\"dynamic\",\"summary\":\"平面式臨時路外停車場\",\"address\":\"新北市板橋區漢生東路64號旁空地\",\"xAxis\":121.46490757038953,\"yAxis\":25.01605185421612,\"tel\":\"2255-5419\",\"payex\":\"小型車計時每小時20元;小型車計時每小時30元;小型車月租每月3000元;機車計次每次10元;機車月租每月150元;大型車計時每小時40元;大型車計時每小時60元;\",\"totalCar\":271,\"totalMotor\":2365,\"serviceTime\":\"0~24時\",\"feeInfo\":{\"carFeeInfo\":{\"weekday\":{\"previousHoursInfo\":{},\"intervalInfo\":[{\"interval\":\"0~24\",\"timeChargeParkingFee\":\"20\",\"feeCycle\":\"1\"},{\"interval\":\"0~24\",\"timeChargeParkingFee\":\"30\",\"feeCycle\":\"1\"}]},\"weekendAndHoliday\":{\"previousHoursInfo\":{},\"intervalInfo\":[{\"interval\":\"0~24\",\"timeChargeParkingFee\":\"20\",\"feeCycle\":\"1\"},{\"interval\":\"0~24\",\"timeChargeParkingFee\":\"30\",\"feeCycle\":\"1\"}]}},\"motorFeeInfo\":{\"weekday\":{\"timesFee\":\"10\",\"previousHoursInfo\":{},\"intervalInfo\":[]},\"weekendAndHoliday\":{\"timesFee\":\"10\",\"previousHoursInfo\":{},\"intervalInfo\":[]}}},\"simpleFeeType\":\"hours\",\"simpleFee\":30,\"Distance\":237.0}";
                JsonObject jsonObject = new JsonParser().parse(testJson).getAsJsonObject();
                history.addHistory(jsonObject);
                ArrayList<Lot> newLots = Lot.fromJson(history.getHistorys());
                adapter.addAll(newLots);
                ++testindex;
            }
        });
*/
        final Button testClear = (Button) view.findViewById(R.id.button3);
        testClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.deleteAll();
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
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_delete:
                adapter.delete(info.position);
                return true;
            case R.id.action_add_favourite:
                favourite.addFavourite(history.getHistorys().get(info.position)); //把歷史紀錄中的一筆資料加入到我的最愛
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
