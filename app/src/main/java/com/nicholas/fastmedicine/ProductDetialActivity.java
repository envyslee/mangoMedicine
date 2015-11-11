package com.nicholas.fastmedicine;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.nicholas.fastmedicine.common.BitmapCache;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import cn.sharesdk.onekeyshare.OnekeyShare;
import litepalDB.CollectionItem;

public class ProductDetialActivity extends AppCompatActivity implements View.OnClickListener {

    String[] ids=new String[]{ "http://img.my.csdn.net/uploads/201508/05/1438760421_2824.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760420_2660.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760420_7188.jpg",};

    private ImageLoader imageLoader;
    private float oldTouchValue;
    private AdapterViewFlipper flipper;
    private ImageView car_img;
    private ImageView favor_img;
    private TextView desc_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detial);
        String title=getIntent().getExtras().getString("title");
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("商品详情");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ShowShare();
                return true;
            }
        });

        flipper=(AdapterViewFlipper)findViewById(R.id.avf);
        AvfAdapter adapter=new AvfAdapter();
        flipper.setAdapter(adapter);
        //自动播放
        //flipper.startFlipping();
        flipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldTouchValue = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float currentX = event.getX();//手指向左滑动
                        if (oldTouchValue < currentX) {
                            flipper.showNext();
                        }
                        if (oldTouchValue > currentX)//手指向右滑动
                        {
                            flipper.showPrevious();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        //药品名称
        TextView name_tv=(TextView)findViewById(R.id.name_tv);
        name_tv.setText(title);
        //规格
        TextView spec_tv=(TextView)findViewById(R.id.spec_tv);
        spec_tv.setText("10ml/支x10支/盒");
        //描述
        desc_tv=(TextView)findViewById(R.id.desc_tv);
        desc_tv.setText("清热解毒，用于外感风所导致的感冒，症见发热、咳嗽、咽痛");

        //收藏
        favor_img=(ImageView)findViewById(R.id.favor_img);
        favor_img.setOnClickListener(this);
        //购物车
        car_img=(ImageView)findViewById(R.id.car_img);
        car_img.setOnClickListener(this);

        //加入购物车
        Button addtocar_btn=(Button)findViewById(R.id.addtocar_btn);
        addtocar_btn.setOnClickListener(this);
    }


    private void GetData()
    {
          String url = "https://raw.githubusercontent.com/hongyangAndroid/okhttp-utils/master/user.gson";
          new OkHttpRequest.Builder().url(url)
                .get(new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAG", "onError , e = " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        desc_tv.setText(response);
                    }


                });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share,menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.favor_img:
                //检查数据库中是否已存在

                CollectionItem item=new CollectionItem("001","秋季养生","吃胡萝卜吃胡萝卜吃胡萝卜",0);
                item.save();
                Toast.makeText(ProductDetialActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                favor_img.setEnabled(false);
                break;
            case R.id.car_img:
                Toast.makeText(this,"购物车图标",Toast.LENGTH_SHORT).show();
                break;
            case R.id.addtocar_btn:
                GetData();
                CarAnimator();
                break;
            default:
                break;
        }
    }



    /**
     * 社会化分享
     */
    private void ShowShare() {
        //初始化
        //ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本，啦啦啦~");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 加入购物车时动画
     */
    private  void CarAnimator()
    {
        /*float flipperX=flipper.getTranslationX();
        float flipperY=flipper.getTranslationY();
        ObjectAnimator distanceY=ObjectAnimator.ofFloat(flipper, "translationY", flipperY, getWindowManager().getDefaultDisplay().getHeight());
        ObjectAnimator distanceX=ObjectAnimator.ofFloat(flipper,"translationX",flipperX,-20);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(flipper, "alpha", 1f, 0f);*/
        ObjectAnimator sizeX=ObjectAnimator.ofFloat(car_img,"scaleX",1f,1.5f,1f);
        ObjectAnimator sizeY=ObjectAnimator.ofFloat(car_img,"scaleY",1f,1.5f,1f);
        AnimatorSet set=new AnimatorSet();
        set.play(sizeX).with(sizeY);
        set.setDuration(1500);
        set.start();
    }

    class AvfAdapter extends BaseAdapter{

        private AvfAdapter()
        {
            RequestQueue queue= Volley.newRequestQueue(ProductDetialActivity.this);
            imageLoader=new ImageLoader(queue,new BitmapCache());
        }
        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NetworkImageView image=new NetworkImageView(ProductDetialActivity.this);
            image.setDefaultImageResId(R.drawable.head);
            image.setErrorImageResId(R.drawable.head);
            image.setImageUrl(ids[position], imageLoader);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            return image;
        }
    }
}
