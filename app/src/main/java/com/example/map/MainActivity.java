package com.example.map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc=true;//记录是否是第一次定位
    private MyLocationConfiguration.LocationMode locationMode;//当前定位模式
//    @RequiresApi(api = Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();//获取百度地图对象
//        //开启交通图
        mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//权限检查
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,//指定GPS定位的提供者
                1000,//间隔时间
                1,//位置间隔一米
                new LocationListener() {//监听GPS定位信息是否改变
                    @Override
                    public void onLocationChanged(Location location) {//GPS信息发生改变时回调
                        locationUpdate(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {//GPS状态改变时回调

                    }

                    @Override
                    public void onProviderEnabled(String provider) {//定位提供者启动时回调

                    }

                    @Override
                    public void onProviderDisabled(String provider) {//定位提供者关闭时回调

                    }
                }

        );
        //从GPS获取最新的定位信息
        Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationUpdate(location);

        }
 public void   locationUpdate(Location location){
        if(location!=null){
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());//获取经纬度
            Log.i("Location", "纬度" + location.getLatitude() + "| 经度" + location.getLongitude());
            if(isFirstLoc){
                MapStatusUpdate u= MapStatusUpdateFactory.newLatLng(ll);//更新坐标位置
                mBaiduMap.animateMapStatus(u);//设置地图位置
                isFirstLoc=false;//取消第一次定位
            }
            //构造定位数据
            MyLocationData locData=new MyLocationData.Builder().accuracy(location.getAccuracy())
                    .direction(100)//设置方向信息
            .latitude(location.getLatitude())//设置纬度
            .longitude(location.getLongitude())//设置经度
            .build();
            mBaiduMap.setMyLocationData(locData);
            //设置自定义图标
//          //  BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
//            locationMode=MyLocationConfiguration.LocationMode.NORMAL;//设置定位模式
//            MyLocationConfiguration config = new MyLocationConfiguration(locationMode, true, bitmapDescriptor);//设置构造方式
//            mBaiduMap.setMyLocationConfiguration(config);

        }
        else {
            Log.i("Location", "没有获取到GPS信息");
        }
 }

    @Override
        protected void onResume() {
            super.onResume();
            //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
            mMapView.onResume();
        }
        @Override
        protected void onPause() {
            super.onPause();
            //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
            mMapView.onPause();
        }
        @Override
        protected void onDestroy() {
            super.onDestroy();
            //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
            mMapView.onDestroy();
            mMapView=null;
        }
    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);//开启定位图层
    }
        @Override
        protected void onStop() {
            super.onStop();
            mBaiduMap.setMyLocationEnabled(false);//停止定位图层
        }

        }
