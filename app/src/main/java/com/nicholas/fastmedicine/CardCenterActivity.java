package com.nicholas.fastmedicine;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CardCenterActivity extends AppCompatActivity {

    private ViewPager vp;
    //private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private ImageView imageView;//tab线条
    private TextView useful,used,over;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_center);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("优惠券");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vp=(ViewPager)findViewById(R.id.card_vp);

        initTextView();
        initImage();
        initViewPager();
    }

    private void initTextView(){
        useful=(TextView)findViewById(R.id.useful);
        used=(TextView)findViewById(R.id.used);
        over=(TextView)findViewById(R.id.overed);
    }

    private void initImage(){
        imageView=(ImageView)findViewById(R.id.cursor);
        //bmpW = BitmapFactory.decodeResource(getResources(),R.drawable.tab).getWidth();

        DisplayMetrics metrics =new DisplayMetrics();
        Display display=getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        int screenWidth=metrics.widthPixels;
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width=screenWidth/3;
        imageView.setLayoutParams(layoutParams);
        bmpW=screenWidth/3;
        //offset = (screenWidth / 3 - bmpW) / 2;
        //Matrix matrix=new Matrix();
        //matrix.postTranslate(0,0);//起始位置
       // imageView.setImageMatrix(matrix);
    }

    private void initViewPager() {
        final List<Fragment> fragments = new ArrayList<>();
        CardUsefulFragment  cardUseful=new CardUsefulFragment();
        fragments.add(cardUseful);
        CardUsedFragment cardUsed=new CardUsedFragment();
        fragments.add(cardUsed);
        CardOverFragment cardOver=new CardOverFragment();
        fragments.add(cardOver);

        FragmentPagerAdapter adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //int one = offset * 2+  bmpW;// 页卡1 -> 页卡2 偏移量
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Animation animation=new TranslateAnimation(bmpW*currIndex,bmpW*position,0,0);
                currIndex=position;
                animation.setFillAfter(true);
                animation.setDuration(300);
                imageView.startAnimation(animation);
                switch (position){
                    case 0:
                        used.setTextColor(Color.parseColor("#999999"));
                        useful.setTextColor(Color.BLACK);
                     break;
                    case 1:
                        useful.setTextColor(Color.parseColor("#999999"));
                        over.setTextColor(Color.parseColor("#999999"));
                        used.setTextColor(Color.BLACK);
                        break;
                    case 2:
                        used.setTextColor(Color.parseColor("#999999"));
                        over.setTextColor(Color.BLACK);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
