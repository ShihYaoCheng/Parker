package com.jas.parker.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Geocoder;
import android.location.Address;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.NumberPicker.Formatter;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.jas.parker.R;
import com.jas.parker.module.map.GetDynamicDataFromDB;
import com.jas.parker.module.save.Favourite;
import com.jas.parker.module.map.GPSLocation;
import com.jas.parker.module.map.GetDataFromDB;
import com.jas.parker.module.save.History;
import com.jas.parker.type.BusinessDataObject;
import com.jas.parker.type.ParkerDataObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindParkingLotFragment extends Fragment implements OnMapReadyCallback,Formatter {
    /*
    @Override
    public void onResume() {
        super.onResume();

    }
*/
    private GoogleMap mMap;
    private GPSLocation location;//呼叫一次會重新定位
    LatLng nowPlace;//現在位置
    LatLng camLat;//相機位置
    private FloatingActionButton fab;
    private Button searchButton;
    private EditText etAddress;
    private  Button filterButton;
    ArrayList<LatLng> parkingLots = new ArrayList<LatLng>();
    private NumberPicker npLow,npUp;
    private double lowerLimit, upperLimit;
    private LocationManager mLocationManager;

    public static ParkerDataObject[] parkerDataObjects;//附近停車場資料
    public static BusinessDataObject[] businessDataObjects;//附近商業資料
    public static HashMap <Marker,ParkerDataObject> markerMap;
    public int dynamicParkingLot;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_contact for this fragment


        try {
            view = inflater.inflate(R.layout.content_find_parking_lot, container, false);
        } catch (InflateException e) {

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used..
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.findParkingLotMap);
        mapFragment.getMapAsync(this);

        location = new GPSLocation(getActivity());
        LatLng l = new LatLng(location.getLatitude(),location.getLongitude());
        if(mMap !=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
        }


        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("定位管理").setMessage("GPS目前是尚未啟用\n"+"請問是否現在設定啟用GPS")
                    .setPositiveButton("啟用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent a = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            //ACTION_LOCATION_SOURCE_SETTINGS
                            startActivity(a);
                        }
                    })
                    .setNegativeButton("不啟用",null)
                    .create().show();
        }

        //定位按鈕
        fab = (FloatingActionButton) view.findViewById(R.id.findParkingLotFab);
        fab.setOnClickListener(LocateButtonOnClick);

        //搜尋按鈕
        searchButton = (Button) view.findViewById(R.id.btn_search);
        searchButton.setOnClickListener(searchButtonOnclick);

        //搜尋文字框
        etAddress = (EditText) view.findViewById(R.id.et_address);

        //篩選器按鈕
        filterButton = (Button) view.findViewById(R.id.btn_filter);
        filterButton.setOnClickListener(filterButtonOnclick);

        upperLimit = 10000;
        lowerLimit = 0;


        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        double latitude = location.getLatitude();//現在緯度
        double longitude = location.getLongitude();//現在經度
        nowPlace = new LatLng(latitude, longitude);

        //從parkerDB找附近停車場
        addMarkerToMap(nowPlace);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(nowPlace));
        mMap.setOnCameraChangeListener(mapCameraChange);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);//定位按鈕開關
        mMap.getUiSettings().setTiltGesturesEnabled(false);//傾斜手勢
        mMap.getUiSettings().setRotateGesturesEnabled(false);//旋轉手勢
        mMap.setInfoWindowAdapter(infoWindowAdapter);
        mMap.setOnMarkerClickListener(markerClick);//點擊marker
        mMap.setOnInfoWindowClickListener(infoWindowClick);//點擊資訊窗

        //開啟定位功能
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
            /*
            //假資料
            ParkerDataObject test = new ParkerDataObject(123,"123","城市", "地區", "測試資料" ,
                    "型態","簡介","地址", 121.777464,25.147376,"電話",
                    "付費資訊",0,0,"營業時間",null
                    ,"hours",50);
*/
        }

    }

    //畫面移動
    private GoogleMap.OnCameraChangeListener mapCameraChange = new GoogleMap.OnCameraChangeListener(){
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            camLat= new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude);
            addMarkerToMap(camLat);
        }
    };

    //從parkerDB找附近停車場
    public void addMarkerToMap(LatLng place){
        GetDataFromDB getDataFromDB = new GetDataFromDB(place.latitude, place.longitude);
        Thread t = new Thread(getDataFromDB);
        t.start();
        try {
            t.join();
            Log.v("TAG1", "GetDataFromDB join");
        } catch (InterruptedException e) {
            Log.v("TAG1", "GetDataFromDB join fail");
            e.printStackTrace();
        }
        parkerDataObjects = getDataFromDB.getParkerDataObjects();
        businessDataObjects = getDataFromDB.getBusinessDataObjects();

        //把附近停車場顯示在地圖上
        for (ParkerDataObject parkerDataObject : parkerDataObjects) {
            //篩選器
            //選擇要放哪個marker
            if(parkerDataObject.getSimpleFee() >= lowerLimit && parkerDataObject.getSimpleFee() <= upperLimit
                    && parkerDataObject.getSimpleFee() >=0 &&parkerDataObject.getSimpleFee() < 50){
                View marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_light_blue, null);
                TextView tvFee = (TextView) marker.findViewById(R.id.textView_fee_light_blue);
                //Log.v("TAG1", String.valueOf(parkerDataObject.getSimpleFee()));
                tvFee.setText("$"+parkerDataObject.getSimpleFee());
                addCustomMarker(createDrawableFromView(getActivity(), marker),parkerDataObject);
            }else if(parkerDataObject.getSimpleFee() >= lowerLimit && parkerDataObject.getSimpleFee() <= upperLimit
                    && parkerDataObject.getSimpleFee() >=50 &&parkerDataObject.getSimpleFee() <100){

                View marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_blue, null);
                TextView tvFee = (TextView) marker.findViewById(R.id.textView_fee_blue);
                tvFee.setText("$"+parkerDataObject.getSimpleFee());
                addCustomMarker(createDrawableFromView(getActivity(), marker),parkerDataObject);
            }else if(parkerDataObject.getSimpleFee() >= lowerLimit && parkerDataObject.getSimpleFee() <= upperLimit
                    && parkerDataObject.getSimpleFee() >= 100){

                View marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_dark_blue, null);
                TextView tvFee = (TextView) marker.findViewById(R.id.textView_fee_dark_blue);
                tvFee.setText("$"+parkerDataObject.getSimpleFee());
                addCustomMarker(createDrawableFromView(getActivity(), marker),parkerDataObject);
            }else if(parkerDataObject.getSimpleFee() == -1){
                View marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_gray, null);
                TextView tvFee = (TextView) marker.findViewById(R.id.textView_fee_gray);
                tvFee.setText("");
                addCustomMarker(createDrawableFromView(getActivity(), marker),parkerDataObject);

            }
        }

        //把附近商業顯示在地圖上
        for (BusinessDataObject businessDataObject : businessDataObjects) {
            View businessMarker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_business, null);
            addBusinessMarker(createDrawableFromView(getActivity(), businessMarker),businessDataObject);

        }

    }

    //按下搜尋按鈕
    private Button.OnClickListener searchButtonOnclick = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            String placeName = etAddress.getText().toString().trim();
            if(placeName.length()>0){
                Geocoder gc = new Geocoder(getActivity());
                List<Address> addresses = null;
                try {
                    addresses = gc.getFromLocationName(placeName,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(addresses == null || addresses.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "找不到該位置", Toast.LENGTH_SHORT).show();
                }else{
                    Address address = addresses.get(0);
                    LatLng position = new LatLng(address.getLatitude(),address.getLongitude());
                    String snippet=address.getAddressLine(0);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));

                    MarkerOptions options = new MarkerOptions();
                    options.position(position).title(placeName).snippet(snippet);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    Marker m =mMap.addMarker(options);
                    //m.showInfoWindow();

                }
            }
        }
    };

    //篩選器按鈕
    private Button.OnClickListener filterButtonOnclick = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            final View item = LayoutInflater.from(getActivity()).inflate(R.layout.filter_layout,null);

            //nnumberpicker
            npLow = (NumberPicker) item.findViewById(R.id.numberPicker_lowLimit);
            if(npLow!=null) {
                npLow.setFormatter(FindParkingLotFragment.this);
                npLow.setMaxValue(30);
                npLow.setMinValue(0);
                npLow.setValue(0);
                npLow.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }
            npUp = (NumberPicker) item.findViewById(R.id.numberPicker_upLimit);
            if(npUp!=null) {
                npUp.setFormatter(FindParkingLotFragment.this);
                npUp.setMaxValue(30);
                npUp.setMinValue(0);
                npUp.setValue(30);
                npUp.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }

            new AlertDialog.Builder(getActivity())
                    .setTitle("篩選")
                    .setView(item)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            NumberPicker numberPicker = (NumberPicker) item.findViewById(R.id.numberPicker_lowLimit);
                            lowerLimit = numberPicker.getValue()*10;
                            Log.v("TAG1", "lowerLimit:"+numberPicker.getValue());
                            NumberPicker numberPicker2 = (NumberPicker) item.findViewById(R.id.numberPicker_upLimit);
                            upperLimit = numberPicker2.getValue()*10;
                            Log.v("TAG1", "upperLimit:"+numberPicker2.getValue());
                            Toast.makeText(getActivity().getApplicationContext(), "顯示價錢在"+(int)lowerLimit+"~"+(int)upperLimit+"的停車場", Toast.LENGTH_LONG).show();
                            mMap.clear();
                            addMarkerToMap(camLat);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity().getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    };
    //重新定位按鈕
    private FloatingActionButton.OnClickListener LocateButtonOnClick = new FloatingActionButton.OnClickListener(){
        @Override
        public void onClick(View view) {
            location = new GPSLocation(getActivity());
            nowPlace = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(nowPlace));
        }
    };

    // 在地圖加入指定位置與標題的標記
    private void addMarker(LatLng place, String title, String snippet) {
        //BitmapDescriptor icon =  BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place).title(title).snippet(snippet);

        mMap.addMarker(markerOptions);
    }

    //creat pk marker
    private Marker addCustomMarker(Bitmap markerIcon,ParkerDataObject pObj){
        if(mMap !=null){
            LatLng point = new LatLng(pObj.getYAxis(),pObj.getXAxis());
            Marker marker  = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(pObj.getName())
                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon))
                    .snippet(pObj.getServiceTime())

            );
            //markerMap.put(marker,pObj);
            return marker;
        }
        return null;
    }

    //creat business marker
    private Marker addBusinessMarker(Bitmap markerIcon,BusinessDataObject bObj){
        if(mMap !=null){
            LatLng point = new LatLng(bObj.getYAxis(),bObj.getXAxis());
            Marker marker  = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(bObj.getBusinessName())
                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon))


            );
            //markerMap.put(marker,pObj);
            return marker;
        }
        return null;
    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels,displayMetrics.heightPixels);
        view.layout(0,0,displayMetrics.widthPixels,displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),view.getMeasuredHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return  bitmap;

    }

    //篩選器數字處理
    @Override
    public String format(int i) {
        String tmpStr = String.valueOf(i);
        if(i !=0){
            tmpStr = tmpStr+"0";
        }
        return tmpStr;
    }

    //顯示資訊視窗
    private GoogleMap.InfoWindowAdapter infoWindowAdapter = new GoogleMap.InfoWindowAdapter() {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View infoWindow= LayoutInflater.from(getActivity()).inflate(R.layout.custom_infowindow,null);
            TextView title = (TextView)infoWindow.findViewById(R.id.textView_parkingLotName);
            title.setText(marker.getTitle());
            //TextView serviceTime = (TextView)infoWindow.findViewById(R.id.Text_serviceTime);
            //serviceTime.setText(marker.getSnippet());
            TextView remainParkingSpace = (TextView)infoWindow.findViewById(R.id.TextView_DynamicParkingLot);
            if( dynamicParkingLot ==-9){
                remainParkingSpace.setText("政府無動態資料!");
            }else if(dynamicParkingLot == -1 ){
                remainParkingSpace.setText("");
            }else{
                remainParkingSpace.setText("剩餘車位:"+dynamicParkingLot);
            }



            return infoWindow;
        }
    };

    //點擊標記
    private GoogleMap.OnMarkerClickListener markerClick = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            Toast.makeText(getActivity().getApplication(), "點擊上方資訊欄以獲得詳細訊息", Toast.LENGTH_SHORT).show();

            GetDynamicDataFromDB getDynamicDataFromDB = new GetDynamicDataFromDB(parkerDataObjects[0].getOriginalId());
            Thread t2 = new Thread(getDynamicDataFromDB);
            t2.start();
            try {
                t2.join();
                Log.v("TAG1", "GetDynamicDataFromDB join");
            } catch (InterruptedException e) {
                Log.v("TAG1", "GetDynamicDataFromDB join fail");
                e.printStackTrace();
            }
            dynamicParkingLot = getDynamicDataFromDB.getDynamicParkingLot();

            marker.showInfoWindow();
            return true;
        }
    };

    //點擊資訊窗
    private GoogleMap.OnInfoWindowClickListener infoWindowClick = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            History history = new History();
            history.addHistoryParkerDataObject(parkerDataObjects[0]);
            Toast.makeText(getActivity().getApplication(), "已經加到歷史紀錄", Toast.LENGTH_SHORT).show();

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_parking_data,null);
            ListView listView = (ListView) view.findViewById(R.id.optionListView);
            String[] values = new String[]{"加到常用停車場", "導航"};

            if(marker.getPosition().latitude == parkerDataObjects[0].getYAxis() && marker.getPosition().longitude == parkerDataObjects[0].getXAxis()){
                //丟入你要顯示的文字
                TextView name = (TextView) view.findViewById(R.id.textView_parkingData_name);
                name.setText(parkerDataObjects[0].getName());
                TextView tel =  (TextView) view.findViewById(R.id.textView_parkingData_tel);
                tel.setText(" 電話:"+parkerDataObjects[0].getTel());
                TextView address =  (TextView) view.findViewById(R.id.textView_parkingData_address);
                address.setText(" 地址:"+parkerDataObjects[0].getAddress());
                TextView payex =  (TextView) view.findViewById(R.id.textView_parkingData_payex);
                payex.setText(" 收費資訊:"+parkerDataObjects[0].getPayex());
                TextView summary =  (TextView) view.findViewById(R.id.textView_parkingData_summary);
                summary.setText(" 類型:"+parkerDataObjects[0].getSummary());
            }else if(marker.getPosition().latitude == businessDataObjects[0].getYAxis() && marker.getPosition().longitude == businessDataObjects[0].getXAxis()){
                TextView name = (TextView) view.findViewById(R.id.textView_parkingData_name);
                name.setText(businessDataObjects[0].getBusinessName());
                TextView address =  (TextView) view.findViewById(R.id.textView_parkingData_address);
                address.setText(" 地址:"+parkerDataObjects[0].getAddress());
            }


            //使用ListAdapter來顯示你輸入的文字
            ListAdapter adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 ,values);
            //將ListAdapter設定至ListView裡面
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    switch (position){
                        case 0:
                            Gson gson = new Gson();
                            Favourite favourite = new Favourite();
                            favourite.addFavouriteParkerDataObject(parkerDataObjects[0]);
                            Toast.makeText(getActivity().getApplication(), "已經加到我的最愛", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            /*
                            Navigation navigation = new Navigation();
                            navigation.startNavigation(parkerDataObjects[0].getYAxis(),parkerDataObjects[0].getXAxis());
                            */
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + parkerDataObjects[0].getYAxis() + "," + parkerDataObjects[0].getXAxis() + "&mode=d");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                            /*
                            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }else{
                                Toast.makeText(view.getContext(), "無法判定可使用導航的應用程式", Toast.LENGTH_SHORT).show();
                            }*/
                            break;
                    }
                }
            });


            new AlertDialog.Builder(getActivity())
                    .setView(view)
                    /*
                    .setItems(values, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity().getApplication(), ""+i, Toast.LENGTH_SHORT).show();
                        }
                    })
                    */
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity().getApplication(), "取消", Toast.LENGTH_SHORT).show();
                        }
                    }).show();


        }
    };

}
