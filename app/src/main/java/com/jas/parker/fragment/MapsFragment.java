package com.jas.parker.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jas.parker.module.map.GPSLocation;
import com.jas.parker.module.map.GetDataFromDB;
import com.jas.parker.type.ParkerDataObject;

import java.util.ArrayList;
import com.jas.parker.R;

public class MapsFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSLocation location;//呼叫一次會重新定位
    LatLng nowPlace;//現在位置
    LatLng markerPoint;//綠點位置
    private ImageButton OkButton;

    public static ParkerDataObject[] parkerDataObjects;//附近停車場資料
    public static LatLng recordPlace;//紀錄的空地
    public static ParkerDataObject recordPlaceParkerDataObject;//紀錄的停車場
    public static int SpaceOrParkingLot = -1;//0為空地,1為停車場
    private static View view;

    private ArrayList<String> lot = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the item_contact for this fragment
        Log.v("TAG1", "onCreatView");
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.activity_maps, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        location = new GPSLocation(getActivity());

        // OkButton = (ImageButton) view.findViewById(R.id.btn);
        //View mapView = super.onCreateView(inflater, container, savedInstanceState);
/*
        View locationButton = ((View) v.findViewById(R.id.map).getParent()).findViewById(Integer.parseInt(("2")));

        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
*/
        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v("TAG1", "onCreat");
        super.onCreate(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v("TAG1", "onMapReady");
        mMap = googleMap;

        double latitude = location.getLatitude();//現在緯度
        double longitude = location.getLongitude();//現在經度
        nowPlace = new LatLng(latitude, longitude);

        markerPoint = null;

        //定位按鈕
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location = new GPSLocation(getActivity());
                nowPlace = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(nowPlace));
            }
        });

        //從parkerDB找附近停車場
        GetDataFromDB getDataFromDB = new GetDataFromDB(latitude, longitude);
        Thread t = new Thread(getDataFromDB);
        t.start();
        try {
            t.join();
            Log.v("TAG1", "join");
        } catch (InterruptedException e) {
            Log.v("TAG1", "join fail");
            e.printStackTrace();
        }
        parkerDataObjects = getDataFromDB.getParkerDataObjects();
        //把附近停車場顯示在地圖上
        int num = parkerDataObjects.length;
        for (int i = 0; i < num; i++) {
            LatLng park = new LatLng(parkerDataObjects[i].getYAxis(), parkerDataObjects[i].getXAxis());
            addMarker(park, parkerDataObjects[i].getName(), parkerDataObjects[i].getAddress());
        }

        //畫面
        if (markerPoint != null) {
            MarkerOptions options = new MarkerOptions();
            options.position(markerPoint);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(options);
        }
        //替代資料
        /*
        LatLng p2 = new LatLng(25.14138, 121.792025);
        parkingLots.add(p2);
        for(int i = 0;i<parkingLots.size();i++){
            addMarker(parkingLots.get(i), "第"+(i+1)+"停車場", "內容");
        }
        */

        mMap.moveCamera(CameraUpdateFactory.newLatLng(nowPlace));//移動畫面
        mMap.getUiSettings().setZoomControlsEnabled(false);//放大縮小按鈕
        mMap.setOnMapClickListener(mapClick);
        mMap.setOnMarkerClickListener(markerClick);
        mMap.setOnInfoWindowClickListener(infoWindowClick);
        // OkButton.setOnClickListener(OkButtonClick);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        //定位
        if (mMap != null) {
            if (android.support.v4.app.ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && android.support.v4.app.ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        }

    }

    private GoogleMap.OnMapClickListener mapClick = new GoogleMap.OnMapClickListener() {

        @Override
        public void onMapClick(LatLng point) {

            mMap.clear();
            int num = parkerDataObjects.length;
            for (int i = 0; i < num; i++) {
                LatLng park = new LatLng(parkerDataObjects[i].getYAxis(), parkerDataObjects[i].getXAxis());
                addMarker(park, parkerDataObjects[i].getName(), parkerDataObjects[i].getAddress());
            }

            //adding new item to ArrayList
            markerPoint = point;
            //System.out.print("111");
            MarkerOptions options = new MarkerOptions();
            options.position(point);

            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            mMap.addMarker(options);
        }
    };
    private GoogleMap.OnMarkerClickListener markerClick = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.showInfoWindow();
            Toast.makeText(getActivity().getApplication(), "提醒：請再點擊上方資訊欄", Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    private GoogleMap.OnInfoWindowClickListener infoWindowClick = new GoogleMap.OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick(Marker marker) {
            final Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner_lot); //spinner
            lot.clear();
            mMap.clear();

            int num = parkerDataObjects.length;
            for (int i = 0; i < num; i++) {
                LatLng park = new LatLng(parkerDataObjects[i].getYAxis(), parkerDataObjects[i].getXAxis());
                addMarker(park, parkerDataObjects[i].getName(), parkerDataObjects[i].getAddress());
            }

            markerPoint = marker.getPosition();
            MarkerOptions options = new MarkerOptions();
            options.position(marker.getPosition());
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(options);

            recordPlace = markerPoint;
            int i;
            for (i = 0; i < num; i++) {
                if (recordPlace.longitude == parkerDataObjects[i].getXAxis() && recordPlace.latitude == parkerDataObjects[i].getYAxis()) {

                    recordPlaceParkerDataObject = parkerDataObjects[i];
                    Log.v("TAG1", recordPlaceParkerDataObject.getName());
                    SpaceOrParkingLot = 1;

                    Toast.makeText(getActivity().getApplication(), "您記錄停車位置是：" + recordPlaceParkerDataObject.getName()+"\n可以點選右側頁面記錄停車位", Toast.LENGTH_LONG).show();

                    //add lot into FeeFragment spinner
                    lot.add(recordPlaceParkerDataObject.getName());
                    // Create an ArrayAdapter using the string array and a default spinner item_contact
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, lot);
                    // Specify the item_contact to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner.setAdapter(adapter);

                    ParkerDataObject temp = parkerDataObjects[i];
                    parkerDataObjects[i] = parkerDataObjects[0];
                    parkerDataObjects[0] = temp;

                    FeeFragment.parkers = parkerDataObjects;
                    break;
                }
            }
        }
    };



    private Button.OnClickListener OkButtonClick = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (markerPoint == null) {
                Toast.makeText(getActivity().getApplication(), "請選擇要記錄的地點", Toast.LENGTH_LONG).show();
            } else {
                recordPlace = markerPoint;
                int num = parkerDataObjects.length;
                int i;
                for (i = 0; i < num; i++) {
                    if (recordPlace.longitude == parkerDataObjects[i].getXAxis() && recordPlace.latitude == parkerDataObjects[i].getYAxis()) {
                        recordPlaceParkerDataObject = parkerDataObjects[i];
                        Log.v("TAG1", recordPlaceParkerDataObject.getName());
                        SpaceOrParkingLot = 1;
                        Toast.makeText(getActivity().getApplication(), "已幫您記錄停車位置\n您停的停車場是：" + recordPlaceParkerDataObject.getName(), Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                if (i == num) {
                    Log.v("TAG1", "紀錄位置不是資料庫停車場");
                    Toast.makeText(getActivity().getApplication(), "已幫您記錄停車位置\n您停的地點是空地", Toast.LENGTH_LONG).show();
                    SpaceOrParkingLot = 0;
                }

                //頁面跳轉
                //Intent intent = new Intent();
                //intent.setClass(getActivity(), NavigationActivity.class);
                //startActivity(intent);

            }
        }

    };

    // 在地圖加入指定位置與標題的標記
    private void addMarker(LatLng place, String title, String snippet) {
        //BitmapDescriptor icon =  BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place).title(title).snippet(snippet);

        mMap.addMarker(markerOptions);
    }

}