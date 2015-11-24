package com.nicholas.fastmedicine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nicholas.fastmedicine.adapter.ImageAdapter;
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.common.MethodSingleton;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

/**
 * Created by eggri_000 on 2015/10/13.
 */
public class Fragment1 extends Fragment implements View.OnClickListener {

    private ViewFlow viewFlow;
    private TextView cityTv;
    private Intent intent;

    //百度定位
    public LocationClient mLocationClient = null;

    //定位成功标识
    private boolean locateSuccess=false;

    private Bundle bundle;

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.shake_lay:
                intent=new Intent(getContext(),ShakeActivity.class);
                startActivity(intent);
                break;
            case R.id.servicePhone:
                new AlertDialog.Builder(getContext())
                        .setMessage("客服电话：4008-365-365")
                        .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4008365365"));
                                startActivity(intentPhone);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            case R.id.searchBanner:
                intent=new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.locateCityName:
                if (locateSuccess)
                {
                    /*intent=new Intent(getContext(),MapActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);*/
                    Toast.makeText(getActivity(),"已定位成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mLocationClient.start();//重新定位
                    Toast.makeText(getActivity(),"正在定位...",Toast.LENGTH_SHORT).show();
                }
                break;
           /* case R.id.community_lay:
                intent=new Intent(getContext(),CommunityActivity.class);
                startActivity(intent);
                break;*/
            case R.id.price_lay:
                intent=new Intent(getContext(),WebActivity.class);
                startActivity(intent);
                break;
            case R.id.disease_lay:
                intent=new Intent(getContext(),SelfCheckListActivity.class);
                startActivity(intent);
                break;
            case R.id.article_lay:

                break;
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null, false);
        viewFlow = (ViewFlow)view.findViewById(R.id.viewflow);
        viewFlow.setAdapter(new ImageAdapter(getActivity()));
        viewFlow.setmSideBuffer(4); // 实际图片张数

        CircleFlowIndicator indic = (CircleFlowIndicator) view.findViewById(R.id.viewflowindic);
        viewFlow.setFlowIndicator(indic);
        viewFlow.setTimeSpan(6000);
        viewFlow.setSelection(4 * 1000);	//张数的倍数
        viewFlow.startAutoFlowTimer();  //自动播放

        //拨打客服电话
        ImageView  servicePhone=(ImageView)view.findViewById(R.id.servicePhone);
        servicePhone.setOnClickListener(this);

        //进入摇一摇
        LinearLayout shake_lay=(LinearLayout)view.findViewById(R.id.shake_lay);
        shake_lay.setOnClickListener(this);

        //进入搜索
        ImageView searchTv=(ImageView)view.findViewById(R.id.searchBanner);
        searchTv.setOnClickListener(this);

        //进入地图
        cityTv=(TextView)view.findViewById(R.id.locateCityName);
        cityTv.setOnClickListener(this);

        //进入社区
       /* LinearLayout community_lay=(LinearLayout)view.findViewById(R.id.community_lay);
        community_lay.setOnClickListener(this);*/

        LinearLayout article_lay=(LinearLayout)view.findViewById(R.id.article_lay);
        article_lay.setOnClickListener(this);

        //今日特价
        LinearLayout price_lay=(LinearLayout)view.findViewById(R.id.price_lay);
        price_lay.setOnClickListener(this);

        //常见病症
        LinearLayout disease_lay=(LinearLayout)view.findViewById(R.id.disease_lay);
        disease_lay.setOnClickListener(this);

        //toobar布局
       /* toolbar_ly=(RelativeLayout)view.findViewById(R.id.toolbar_ly);

        //scrollview监听
        MyScrollView home_scroll=(MyScrollView)view.findViewById(R.id.home_scroll);
        home_scroll.setOnMyScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY < 100) {
                    toolbar_ly.setAlpha((float) scrollY/100);
                    Log.e("TAG", "Alpha : " + (float) scrollY/100);
                }
            }
        });*/


        if (MethodSingleton.getInstance().isNetAvailable(getContext()))
        {
            mLocationClient=new LocationClient(getActivity().getApplication(),initLocationOption());
            /*MyBaiduLocationListener listener=new MyBaiduLocationListener();
            mLocationClient.registerLocationListener(listener);*/
            mLocationClient.registerLocationListener(new BDLocationListener(){
                @Override
                public void onReceiveLocation(BDLocation location) {
                    String city=location.getCity();
                    if(city!=null)
                    {
                        cityTv.setText(city);
                        if (city.equals("苏州市")) {
                            //Log.i("BaiduLocationApiDem", "success");
                            locateSuccess = true;//标记定位成功
                            Constant.cityName=city;
                            Constant.latitude=location.getLatitude();
                            Constant.lontitude=location.getLongitude();
                            Constant.radius=location.getRadius();
                            bundle = new Bundle();
                            bundle.putDouble("latitude", location.getLatitude());//经度
                            bundle.putDouble("lontitude", location.getLongitude());//维度
                            bundle.putFloat("radius", location.getRadius());//方向，0-360度
                            bundle.putString("city", city);
                            //bundle.putString("address",location.getAddrStr());
                        }
                        else
                        {
                            Toast.makeText(getContext(),"暂不支持"+city+"的服务",Toast.LENGTH_SHORT).show();
                        }
                        mLocationClient.stop();
                    }
                    else {
                        Log.i("BaiduLocationApiDem", "no data");
                    }
                }
            });
            mLocationClient.start();
        }
        else{
            Toast.makeText(getContext(),Constant.netError,Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    /**
     * LocationClientOption�࣬�����������ö�λSDK�Ķ�λ��ʽ
     * @return
     */
    private LocationClientOption initLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span=5000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        //mLocationClient.setLocOption(option);
        return option;
    }
}
