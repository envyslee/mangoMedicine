package com.nicholas.fastmedicine;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.nicholas.fastmedicine.common.BitmapCache;

import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.controller.BadgeView;
import com.nicholas.fastmedicine.item.ReviewItem;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;
import litepalDB.CollectionItem;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    String[] ids = new String[]{"http://img.my.csdn.net/uploads/201508/05/1438760421_2824.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760420_2660.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760420_7188.jpg",};

    private String ph_phone = "";

    private ImageLoader imageLoader;
    private float oldTouchValue;
    private AdapterViewFlipper flipper;
    private ImageView car_img, favor_img, call_ph;
    private TextView name_tv;
    private TextView desc_tv;
    private TextView price_tv;
    private TextView spec_tv;
    private TextView Intro_name;
    private TextView Intro_ingredients;
    private TextView Intro_character;
    private TextView Intro_usage;
    private TextView Intro_spec;
    private TextView Intro_amount;
    private TextView Intro_untowardEffect;
    private TextView Intro_attention;
    private TextView Intro_storge;
    private TextView Intro_enterprise;
    private TextView ph_name;
    private TextView ph_address;
    private TextView review_count;

    private Button addtocar_btn;

    private RelativeLayout loading;

    private String priceId;

    private BadgeView badgeView;

    private List<ReviewItem> reviewItems;

    private void initView() {
        //药品名称
        name_tv = (TextView) findViewById(R.id.name_tv);
        Intro_name = (TextView) findViewById(R.id.Intro_name);

        //规格
        spec_tv = (TextView) findViewById(R.id.spec_tv);
        Intro_spec = (TextView) findViewById(R.id.Intro_spec);
        //描述
        desc_tv = (TextView) findViewById(R.id.desc_tv);
        //价格
        price_tv = (TextView) findViewById(R.id.price_tv);
        //成分
        Intro_ingredients = (TextView) findViewById(R.id.Intro_ingredients);
        //性状
        Intro_character = (TextView) findViewById(R.id.Intro_character);
        //功能主治
        Intro_usage = (TextView) findViewById(R.id.Intro_usage);
        //用法用量
        Intro_amount = (TextView) findViewById(R.id.Intro_amount);
        //不良反应
        Intro_untowardEffect = (TextView) findViewById(R.id.Intro_untowardEffect);
        //注意事项
        Intro_attention = (TextView) findViewById(R.id.Intro_attention);
        //贮藏
        Intro_storge = (TextView) findViewById(R.id.Intro_storge);
        //生产企业
        Intro_enterprise = (TextView) findViewById(R.id.Intro_enterprise);
        //药店名称
        ph_name = (TextView) findViewById(R.id.ph_name);
        //药店地址
        ph_address = (TextView) findViewById(R.id.ph_address);

        //药店电话
        call_ph = (ImageView) findViewById(R.id.call_ph);
        call_ph.setOnClickListener(this);
        //收藏
        favor_img = (ImageView) findViewById(R.id.favor_img);
        favor_img.setOnClickListener(this);
        //购物车
        car_img = (ImageView) findViewById(R.id.car_img);
        car_img.setOnClickListener(this);

        //加入购物车
        addtocar_btn = (Button) findViewById(R.id.addtocar_btn);
        addtocar_btn.setOnClickListener(this);

        //评论条数
        review_count=(TextView)findViewById(R.id.review_count);

        //进入评论页
        RelativeLayout go_review=(RelativeLayout)findViewById(R.id.go_review);
        go_review.setOnClickListener(this);



        //加载动画
        loading=(RelativeLayout)findViewById(R.id.loading_lay);

        badgeView=new BadgeView(ProductDetailActivity.this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Bundle bundle = getIntent().getExtras();
        String productName = bundle.getString("productName");
        Double productId = bundle.getDouble("productId");
        Double pharmacyId = bundle.getDouble("pharmacyId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        initView();
        name_tv.setText(productName);
        Intro_name.setText(productName);

        flipper = (AdapterViewFlipper) findViewById(R.id.avf);
        AvfAdapter adapter = new AvfAdapter();
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
        GetCarCount();
        GetProductDetail(Constant.baseUrl + "postProductDetail", productId, pharmacyId);

    }

    private void GetProductDetail(String url, Double productId, Double pharmacyId) {
        Map<String, String> map = new HashMap<>();
        map.put("productId", String.valueOf(productId.intValue()));
        map.put("pharmacyId", String.valueOf(pharmacyId.intValue()));
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ProductDetailActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode() == null) {
                    Toast.makeText(ProductDetailActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                if (ws.getResCode().equals("0")) {
                    Map<String, Object> map = (Map) ws.getContent();
                    //String pics = map.get("productPics").toString();
                    Intro_ingredients.setText(map.get("mainIngredients").toString());
                    Intro_character.setText(map.get("productCharacter").toString());
                    Intro_usage.setText(map.get("productUsage").toString());
                    Intro_amount.setText(map.get("usageAmount").toString());
                    Intro_attention.setText(map.get("attentionTip").toString());
                    Intro_storge.setText(map.get("productStorage").toString());
                    Intro_enterprise.setText(map.get("productEnterprise").toString());
                    Intro_untowardEffect.setText(map.get("untowardEffect").toString());
                    Intro_spec.setText(map.get("productSpec").toString());

                    ph_address.setText(map.get("pharmacyAddress").toString());
                    ph_name.setText(map.get("pharmacyName").toString());
                    ph_phone = map.get("pharmacyPhone").toString();

                    desc_tv.setText(map.get("productUsage").toString().substring(0, 28) + "...");
                    spec_tv.setText(map.get("productSpec").toString());
                    price_tv.setText("￥" + String.valueOf(map.get("productPrice")));
                    double p = (double) map.get("priceId");
                    priceId = String.valueOf((int) p);

                    loading.setVisibility(View.GONE);
                    GetReview();
                } else {
                    Toast.makeText(ProductDetailActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favor_img:
                //检查数据库中是否已存在

                CollectionItem item = new CollectionItem("001", "秋季养生", "吃胡萝卜吃胡萝卜吃胡萝卜", 0);
                item.save();
                Toast.makeText(ProductDetailActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                favor_img.setEnabled(false);
                break;
            case R.id.car_img:
                Intent intent=new Intent(ProductDetailActivity.this,MainActivity.class);
                intent.putExtra("comeFrom","productDetail");
                startActivity(intent);
                finish();
                break;
            case R.id.addtocar_btn:

                AddToCar();
                break;
            case R.id.call_ph:
                new AlertDialog.Builder(ProductDetailActivity.this)
                        .setMessage("药房电话：" + ph_phone)
                        .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ph_phone));
                                startActivity(intentPhone);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            case R.id.go_review:
                Intent intent_re=new Intent(ProductDetailActivity.this,ReviewActivity.class);
                intent_re.putExtra("reviews",(Serializable)reviewItems);
                startActivity(intent_re);
                break;
            default:
                break;
        }
    }

    private void GetCarCount(){
        String url=Constant.baseUrl+"getCarCount";
        if (!Constant.userId.isEmpty()){
            Map<String ,String> map=new HashMap<>();
            map.put("userId",Constant.userId);
            new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
                @Override
                public void onError(Request request, Exception e) {
                    Toast.makeText(ProductDetailActivity.this,Constant.dataError,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(WsResponse ws) {
                    if (ws.getResCode() != null) {
                        if (ws.getResCode().equals("0")){
                            badgeView.setBadgeCount(((Double) ws.getContent()).intValue());
                            badgeView.setTargetView(car_img);
                        }
                    }
                }
            });
        }
    }

    private void GetReview(){
        String url= Constant.baseUrl+"getReview";
        Map<String ,String> map=new HashMap<>();
        map.put("priceId", priceId);
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                //Toast.makeText(ProductDetailActivity.this,Constant.dataError,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                    if (ws.getResCode()!=null){
                        if (ws.getResCode().equals("0")){
                           reviewItems=new ArrayList<>();
                            List<Map<String ,Object>> s=(List)ws.getContent();
                            int size=s.size();
                            review_count.setText("("+size+"人评价)");
                            if (size>0){
                                for (int i=0;i<size;i++) {
                                    Map<String ,Object> m=s.get(i);
                                    double time=(double)m.get("createdTime");
                                    SimpleDateFormat format=new SimpleDateFormat("yyyy.MM.dd");
                                    ReviewItem item = new ReviewItem(((Double)m.get("id")).intValue(),m.get("content").toString(),format.format(time),m.get("userName").toString());
                                    reviewItems.add(item);
                                }
                            }
                        }
                    }
            }
        });
    }

    private void AddToCar(){
        loading.setVisibility(View.VISIBLE);
        String url=Constant.baseUrl+"postIntoCar";
        Map<String ,String> map=new HashMap<>();
        if (!Constant.userId.isEmpty()){
            map.put("userId",Constant.userId);
            map.put("priceId",priceId);
            map.put("count", "1");
            new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
                @Override
                public void onError(Request request, Exception e) {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(ProductDetailActivity.this,Constant.dataError,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(WsResponse ws) {
                    loading.setVisibility(View.GONE);
                    if (ws.getResCode() != null) {
                        if (ws.getResCode().equals("0")) {
                            Constant.carDataChanged=true;
                            CarAnimator();
                            badgeView.setBadgeCount(((Double) ws.getContent()).intValue());
                            badgeView.setTargetView(car_img);
                        }else if (ws.getResCode().equals("010")){
                            Toast.makeText(ProductDetailActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ProductDetailActivity.this, ws.getResMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ProductDetailActivity.this,Constant.dataError,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(ProductDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
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
    private void CarAnimator() {
        /*float flipperX=flipper.getTranslationX();
        float flipperY=flipper.getTranslationY();
        ObjectAnimator distanceY=ObjectAnimator.ofFloat(flipper, "translationY", flipperY, getWindowManager().getDefaultDisplay().getHeight());
        ObjectAnimator distanceX=ObjectAnimator.ofFloat(flipper,"translationX",flipperX,-20);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(flipper, "alpha", 1f, 0f);*/
        ObjectAnimator sizeX = ObjectAnimator.ofFloat(car_img, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator sizeY = ObjectAnimator.ofFloat(car_img, "scaleY", 1f, 1.5f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.play(sizeX).with(sizeY);
        set.setDuration(1500);
        set.start();
    }

    class AvfAdapter extends BaseAdapter {

        private AvfAdapter() {
            RequestQueue queue = Volley.newRequestQueue(ProductDetailActivity.this);
            imageLoader = new ImageLoader(queue, new BitmapCache());
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
            NetworkImageView image = new NetworkImageView(ProductDetailActivity.this);
            image.setDefaultImageResId(R.drawable.head);
            image.setErrorImageResId(R.drawable.head);
            image.setImageUrl(ids[position], imageLoader);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            return image;
        }
    }
}
