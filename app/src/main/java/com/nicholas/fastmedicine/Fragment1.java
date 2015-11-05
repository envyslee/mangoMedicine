package com.nicholas.fastmedicine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.common.Method;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

/**
 * Created by eggri_000 on 2015/10/13.
 */
public class Fragment1 extends BaseFragment implements View.OnClickListener {

    /*private ViewFlipper myflipper;*/
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
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4008365365"));
                                startActivity(intent);
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
                    intent=new Intent(getContext(),MapActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.community_lay:
                intent=new Intent(getContext(),CommunityActivity.class);
                startActivity(intent);
                break;
            case R.id.price_lay:
                /*intent=new Intent(getContext(),FloatActivity.class);
                startActivity(intent);*/
                break;
            case R.id.disease_lay:
                intent=new Intent(getContext(),SelfCheckListActivity.class);
                startActivity(intent);
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
        LinearLayout community_lay=(LinearLayout)view.findViewById(R.id.community_lay);
        community_lay.setOnClickListener(this);

        //今日特价
        LinearLayout price_lay=(LinearLayout)view.findViewById(R.id.price_lay);
        price_lay.setOnClickListener(this);

        //常见病症
        LinearLayout disease_lay=(LinearLayout)view.findViewById(R.id.disease_lay);
        disease_lay.setOnClickListener(this);

        if (Method.isNetAvailable(getContext()))
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
            Toast.makeText(getContext(),"亲，网络不给力哦",Toast.LENGTH_SHORT).show();
        }



        return view;

        /*myflipper=(ViewFlipper)view.findViewById(R.id.myflipper);
        myflipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        startX=event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                    {
                        if (event.getX()-startX>100)
                        {
                            myflipper.showPrevious();
                        }
                        if (startX-event.getX()>100)
                        {
                            myflipper.showNext();
                        }
                        break;
                    }
                }
                return false;
            }
        });
        for (int i=0;i<resId.length;i++)
        {
            myflipper.addView(getImageView(resId[i]));
        }
        //���ö���
        //myflipper.setInAnimation();
        myflipper.setFlipInterval(3000);
        myflipper.startFlipping();*/


    }

    /**
     * LocationClientOption�࣬�����������ö�λSDK�Ķ�λ��ʽ
     * @return
     */
    private LocationClientOption initLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
        option.setCoorType("bd09ll");//��ѡ��Ĭ��gcj02�����÷��صĶ�λ������ϵ
        int span=5000;
        //option.setScanSpan(span);//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
        option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
        option.setOpenGps(true);//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
        option.setLocationNotify(true);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
        option.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯��������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ���
        option.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI��������BDLocation.getPoiList��õ�
        option.setIgnoreKillProcess(false);//��ѡ��Ĭ��false����λSDK�ڲ���һ��SERVICE�����ŵ��˶�����̣������Ƿ���stop��ʱ��ɱ�������̣�Ĭ��ɱ��
        option.SetIgnoreCacheException(false);//��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
        option.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps������Ĭ����Ҫ
        //mLocationClient.setLocOption(option);
        return option;
    }
   /* private ImageView getImageView(int resId)
    {
        ImageView image=new ImageView(getActivity());
        //image.setImageResource(resId);//����ͼƬ��С
        image.setBackgroundResource(resId);//����
        return  image;
    }*/
}
