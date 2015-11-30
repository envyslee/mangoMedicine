package com.nicholas.fastmedicine;


import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.nicholas.fastmedicine.controller.BadgeView;

import cn.sharesdk.framework.ShareSDK;


public class MainActivity extends FragmentActivity implements View.OnClickListener ,Fragment3.loadDataListener {

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;

    private View home;
    private View list;
    private View car;
    private View personal;

    private ImageView homeImg;
    private ImageView listImg;
    private ImageView carImg;
    private ImageView personalImg;

    private TextView homeText;
    private TextView listText;
    private TextView carText;
    private TextView personalText;

    private FragmentManager fragmentManager;

    private int lastIndex;

    private long exitTime = 0;

    private BadgeView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        view =new BadgeView(this);

        if (getIntent().getExtras()!=null) {
            String s = getIntent().getExtras().getString("comeFrom");
            if (s != null && s.equals("productDetail")) {
                setTabSelection(2);

            }
        }

        initViews();
        setTabSelection(0);
        //初始化分享
        ShareSDK.initSDK(this);

    }

    @Override
    public void onClick(View v) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection(lastIndex);
        switch (v.getId()) {
            case R.id.message_layout:
                lastIndex = 0;
                setTabSelection(0);
                break;
            case R.id.contacts_layout:
                lastIndex = 1;
                setTabSelection(1);
                break;
            case R.id.news_layout:
                lastIndex = 2;
                setTabSelection(2);
                break;
            case R.id.setting_layout:
                lastIndex = 3;
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                homeImg.setImageResource(R.drawable.home);
                homeText.setTextColor(Color.parseColor("#32B9AA"));
                if (fragment1 == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    fragment1 = new Fragment1();
                    transaction.add(R.id.content, fragment1);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(fragment1);
                }
                break;
            case 1:
                listImg.setImageResource(R.drawable.list);
                listText.setTextColor(Color.parseColor("#32B9AA"));
                if (fragment2 == null) {
                    fragment2 = new Fragment2();
                    transaction.add(R.id.content, fragment2);
                } else {
                    transaction.show(fragment2);
                }
                break;
            case 2:
                carImg.setImageResource(R.drawable.car);
                carText.setTextColor(Color.parseColor("#32B9AA"));
                /*fragment3 = new Fragment3();
                transaction.add(R.id.content, fragment3);
                transaction.show(fragment3);*/
                if (fragment3== null) {
                    fragment3 = new Fragment3();
                    transaction.add(R.id.content, fragment3);
                } else {
                    transaction.show(fragment3);
                }
                break;
            case 3:
            default:
                personalImg.setImageResource(R.drawable.personal);
                personalText.setTextColor(Color.parseColor("#32B9AA"));
                if (fragment4 == null) {
                    fragment4 = new Fragment4();
                    transaction.add(R.id.content, fragment4);
                } else {
                    transaction.show(fragment4);
                }
                break;
        }
        transaction.commit();
    }

    @Override
    protected void onResume() {
        //更新用户登录状态
        if (fragment4!=null){
            fragment4.updateUserInfo();
        }


        //取购物车数据量
        if (fragment3!=null){
            fragment3.GetCarData();
        }


        /*view.setTargetView(carImg);
        view.setBadgeGravity(Gravity.RIGHT);
        view.setBadgeCount(2);*/
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            //停止社交分享
            ShareSDK.stopSDK(this);
            finish();
            System.exit(0);
        }

    }

    private void initViews() {
        home = findViewById(R.id.message_layout);
        list = findViewById(R.id.contacts_layout);
        car = findViewById(R.id.news_layout);
        personal = findViewById(R.id.setting_layout);
        homeImg = (ImageView) findViewById(R.id.message_image);
        listImg = (ImageView) findViewById(R.id.contacts_image);
        carImg = (ImageView) findViewById(R.id.news_image);
        personalImg = (ImageView) findViewById(R.id.setting_image);
        homeText = (TextView) findViewById(R.id.message_text);
        listText = (TextView) findViewById(R.id.contacts_text);
        carText = (TextView) findViewById(R.id.news_text);
        personalText = (TextView) findViewById(R.id.setting_text);
        home.setOnClickListener(this);
        list.setOnClickListener(this);
        car.setOnClickListener(this);
        personal.setOnClickListener(this);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (fragment1 != null) {
            transaction.hide(fragment1);
        }
        if (fragment2 != null) {
            transaction.hide(fragment2);
        }
        if (fragment3 != null) {
            transaction.hide(fragment3);
        }
        if (fragment4 != null) {
            transaction.hide(fragment4);
        }
    }

    private void clearSelection(int index) {
        switch (index) {
            case 0:
                homeImg.setImageResource(R.drawable.home_back);
                homeText.setTextColor(Color.parseColor("#82858b"));
                break;
            case 1:
                listImg.setImageResource(R.drawable.list_back);
                listText.setTextColor(Color.parseColor("#82858b"));
                break;
            case 2:
                carImg.setImageResource(R.drawable.car_back);
                carText.setTextColor(Color.parseColor("#82858b"));
                break;
            case 3:
                personalImg.setImageResource(R.drawable.personal_back);
                personalText.setTextColor(Color.parseColor("#82858b"));
                break;
        }

    }


    @Override
    public void clearCarCount() {
        view.setBadgeCount(0);
    }
}
