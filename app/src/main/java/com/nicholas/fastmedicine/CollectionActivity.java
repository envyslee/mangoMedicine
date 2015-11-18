package com.nicholas.fastmedicine;

import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView medicine;
    private TextView article;
    private View tabline;
    private int screen1_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("收藏");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initTabLine();
        initViewPager();


    }

    /**
     * 初始化ViewPager下的线
     */
    private void initTabLine() {
        tabline = findViewById(R.id.tabline);
        //取屏幕1/2
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screen1_2 = metrics.widthPixels / 2;
        ViewGroup.LayoutParams lp = tabline.getLayoutParams();
        lp.width = screen1_2;
        tabline.setLayoutParams(lp);
    }


    /**
     *初始化ViewPager
     */
    private void initViewPager() {
  /*      PagerTabStrip tab=(PagerTabStrip)findViewById(R.id.tab);
        tab.setTextColor(Color.RED);
        tab.setTabIndicatorColor(Color.BLUE);
        tab.setDrawFullUnderline(false);*/

        medicine = (TextView) findViewById(R.id.medicine);
        article = (TextView) findViewById(R.id.artical);
        medicine.setTextColor(Color.BLACK);
        viewPager = (ViewPager) findViewById(R.id.collectionvp);
        final List<Fragment> fragments = new ArrayList<>();
        FragmentArticle fa = new FragmentArticle();
        FragmentMedicine fm = new FragmentMedicine();
        fragments.add(fa);
        fragments.add(fm);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }


        };
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabline.getLayoutParams();
                int l = (int) (position * screen1_2 + positionOffset * screen1_2);
                layoutParams.leftMargin = l;
                //Log.i("TAG", positionOffset + "," + position + "," + l);
                tabline.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                ResetTextColor();
                switch (position) {
                    case 0:
                        medicine.setTextColor(Color.BLACK);
                        break;
                    case 1:
                        article.setTextColor(Color.BLACK);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void ResetTextColor() {
        medicine.setTextColor(Color.parseColor("#999999"));
        article.setTextColor(Color.parseColor("#999999"));
    }
}
