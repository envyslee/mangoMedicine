package com.nicholas.fastmedicine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private int[] ids = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_5};
    private SharedPreferences preferences;
    private List<View> guides = new ArrayList<>();
    private ViewPager pager;
    private ImageView curDot;
    private int offset;
    private int curPos = 0;
    private SharedPreferences.Editor editor;
    private Button image_open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Display the current version number
        PackageManager pm = getPackageManager();
        PackageInfo pi=null;
        try {
             pi = pm.getPackageInfo("com.nicholas.fastmedicine", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        preferences=getSharedPreferences("versionCode",MODE_PRIVATE);
        int savedVerCode=preferences.getInt("versionCode",0);
        if (pi==null)
        {
            GotoMainActivity();
        }
        if (pi.versionCode>savedVerCode) {
            editor = preferences.edit();
            editor.putInt("versionCode", pi.versionCode);
            editor.commit();
            InitView();
        } else {
            GotoSplashActivity();
        }

        image_open = (Button) findViewById(R.id.image_open);
        image_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoMainActivity();
            }
        });
    }

    private void InitView() {
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            //imageView.setImageResource(ids[i]);
            Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), ids[i]));
            imageView.setImageBitmap(bitmap);
            /*ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);*/
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            guides.add(imageView);
        }
        curDot = (ImageView) findViewById(R.id.cur_dot);
        curDot.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        offset = curDot.getWidth();
                        return true;
                    }
                });

        WelcomePagerAdapter adapter = new WelcomePagerAdapter(guides);
        pager = (ViewPager) findViewById(R.id.welcome_viewpager);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                moveCursorTo(position);
                if (position == ids.length - 1) {// 到最后一张了
                    image_open.setVisibility(View.VISIBLE);
                } else {
                    image_open.setVisibility(View.GONE);
                }
                curPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void moveCursorTo(int position) {
        TranslateAnimation anim = new TranslateAnimation(offset * curPos, offset * position, 0, 0);
        anim.setDuration(300);
        anim.setFillAfter(true);
        curDot.startAnimation(anim);
    }

    private void GotoSplashActivity() {
        Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
        startActivity(intent);
        WelcomeActivity.this.finish();
    }

    private void GotoMainActivity()
    {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        WelcomeActivity.this.finish();
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory
     */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    class WelcomePagerAdapter extends PagerAdapter {
        private List<View> views;

        public WelcomePagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }
}
