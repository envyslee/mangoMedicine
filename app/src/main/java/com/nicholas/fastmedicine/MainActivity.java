package com.nicholas.fastmedicine;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.githang.viewpagerindicator.IconPagerAdapter;
import com.githang.viewpagerindicator.IconTabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


public class MainActivity extends FragmentActivity {

    //viewpager
    private ViewPager mViewPager;
    private IconTabPageIndicator mIndicator;


    private long exitTime = 0;

    private FragmentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        //初始化分享
        ShareSDK.initSDK(this);
    }


    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis()-exitTime>2000)
        {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else
        {
            //停止社交分享
            ShareSDK.stopSDK(this);
            finish();
            System.exit(0);
        }

    }

    /**
     * 初始化viewpager
     */
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(3);//缓存fargment的数量
        mIndicator = (IconTabPageIndicator) findViewById(R.id.indicator);
        List<BaseFragment>  fragments = initFragments();

        adapter = new FragmentAdapter(fragments, getSupportFragmentManager());


        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);


      /*  mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }



    private List<BaseFragment> initFragments() {
        List<BaseFragment> fragments = new ArrayList<>();

        BaseFragment userFragment = new Fragment1();
        userFragment.setTitle("首页");
        userFragment.setIconId(R.drawable.tab_user_selector);
        fragments.add(userFragment);

        BaseFragment noteFragment = new Fragment2();
        noteFragment.setTitle("分类");
        noteFragment.setIconId(R.drawable.tab_record_selector);
        fragments.add(noteFragment);

        BaseFragment contactFragment = new Fragment3();
        contactFragment.setTitle("购物车");
        contactFragment.setIconId(R.drawable.tab_user_selector);
        fragments.add(contactFragment);

        BaseFragment recordFragment = new Fragment4();
        recordFragment.setTitle("我的");
        recordFragment.setIconId(R.drawable.tab_record_selector);
        fragments.add(recordFragment);

        return fragments;
    }

    class FragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        private List<BaseFragment> mFragments;

        private String tagString="";
        public FragmentAdapter(List<BaseFragment> fragments, FragmentManager fm) {
            super(fm);
            mFragments = fragments;
        }



    /*    public  String makeFragmentName(int viewId, long index) {
            return "android:switcher:" + viewId + ":" + index;
        }*/

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            Fragment fragment=(Fragment) super.instantiateItem(container, position);
            if (position==2)
            {
                tagString=fragment.getTag();
             /*   android.support.v4.app.FragmentTransaction ft=fragmentManager.beginTransaction();
                ft.remove(fragment);
                fragment=initFragments().get(2);
                ft.add(container.getId(), fragment, fragmentTag);
                ft.attach(fragment);
                ft.commit();*/

            }
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            Fragment view = (Fragment)object;
            String tag=view.getTag().toString();
            if (tag.equals(tagString))
                return POSITION_NONE;
            else
                return POSITION_UNCHANGED;
            //return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getIconResId(int index) {
            return mFragments.get(index).getIconId();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }
    }

}
