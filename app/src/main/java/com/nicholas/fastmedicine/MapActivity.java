package com.nicholas.fastmedicine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.nicholas.fastmedicine.adapter.AddressPoiAdapter;
import com.nicholas.fastmedicine.item.AddressPoiItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by eggri_000 on 2015/10/15.
 */
public class MapActivity extends Activity {

    private int SETADAPTER = 0;
    private AddressPoiAdapter adapter;
    private List<AddressPoiItem> list = new ArrayList<>();
    private List<PoiInfo> poiInfos;
    private ListView address_list;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private GeoCoder mSearch = null;
    private String geoCurrCity;
    BitmapDescriptor mCurrentMarker;//maker 图标


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        Bundle bundle = getIntent().getExtras();
        Double la = bundle.getDouble("latitude");
        Double lo = bundle.getDouble("lontitude");
        Float ra = bundle.getFloat("radius");
        final String city = bundle.getString("city");

        //地址列表
        address_list = (ListView) findViewById(R.id.address_list);
        address_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (geoCurrCity.equals(city)) {
                    TextView poiNameView = (TextView) view.findViewById(R.id.item_name);
                    String poiName = poiNameView.getText().toString();
                    TextView poiAddressView = (TextView) view.findViewById(R.id.item_address);
                    String poiAddress = poiAddressView.getText().toString();
                    Intent intent = getIntent();
                    intent.putExtra("poiName", poiName);
                    MapActivity.this.setResult(0, intent);
                    finish();
                } else {
                    Toast.makeText(MapActivity.this, "所选地址跟城市不匹配", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //卫星地图
        //mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化
        //mLocClient=new LocationClient(this);

        //构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(ra)
                .direction(100).latitude(la)
                .longitude(lo).build();
        //设置定位数据
        mBaiduMap.setMyLocationData(locData);
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
        //缩放级别
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomBy(4));
        //更新坐标
        LatLng ll = new LatLng(la, lo);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(ll));
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                //获取地图中心点坐标
                LatLng ptCenter = mBaiduMap.getMapStatus().target;
                //反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
            }
        });
        mSearch = GeoCoder.newInstance();
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));

        //mSearch.geocode(new GeoCodeOption().city(city).address(address));
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(geoCodeResult.getLocation());
                mBaiduMap.animateMapStatus(status);
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (poiInfos != null) {
                    poiInfos.clear();
                }
                if (list != null) {
                    list.clear();
                }
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(MapActivity.this, "该位置无信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                mBaiduMap.clear();

                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));

                //获取地址详情
                geoCurrCity = result.getAddressDetail().city;
                if (result.getPoiList() != null && result.getPoiList().size() > 0) {
                    poiInfos = result.getPoiList();
                } else {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(MapActivity.this, "该位置无信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (PoiInfo p : poiInfos) {
                    list.add(new AddressPoiItem(p.name, p.address));
                }
                if (adapter == null) {
                    adapter = new AddressPoiAdapter(MapActivity.this, R.layout.address_list_item, list);
                    handler.sendEmptyMessage(SETADAPTER);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SETADAPTER) {
                address_list.setAdapter(adapter);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
}
