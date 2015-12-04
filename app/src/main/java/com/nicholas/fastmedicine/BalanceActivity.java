package com.nicholas.fastmedicine;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.OrderProductAdapter;
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.controller.ListViewFitScrollView;
import com.nicholas.fastmedicine.item.ProductListItem;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout loading,point_lay;
    private TextView  receiver,phone,address,payMethod,point,card,productTotalPrice,orderTotalPrice,favourablePrice,shouldPay;
    private Button goPay;
    private CheckBox userPointCb;
    private ListViewFitScrollView order_product_list;
    private ScrollView sv;
    private boolean addressAlready=false;
    private int payMethodTag=0;//0到付1支付宝2微信
    private PopupWindow pop;
    private String totalPrice;
    private   int cardAmount=0;
    private BigDecimal b;//可用积分
    private List<ProductListItem> children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("结算清单");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle=getIntent().getExtras();
        //获取商品总价
        totalPrice=bundle.getString("totalPay");
        children =(List<ProductListItem>) bundle.getSerializable("children");

        initView();

        productTotalPrice.setText("￥" + totalPrice);
        orderTotalPrice.setText("￥" + totalPrice);
        shouldPay.setText("应付金额:￥"+totalPrice);

        if (!Constant.userId.isEmpty()) {
            getPoint(Constant.userId);
        }
        order_product_list.setAdapter(new OrderProductAdapter(this,R.layout.order_product_list_item,children));

        //默认显示在listview 所以手动滚动到顶部
        sv.smoothScrollTo(0,0);
    }

    private void initView() {
        loading = (RelativeLayout) findViewById(R.id.loading_lay);
        loading.setVisibility(View.GONE);

        //地址选择
        LinearLayout address_lay = (LinearLayout) findViewById(R.id.address_lay);
        address_lay.setOnClickListener(this);

        //收货人
        receiver=(TextView)findViewById(R.id.receiver);
        receiver.setOnClickListener(this);

        //电话
         phone=(TextView)findViewById(R.id.phone);
        phone.setOnClickListener(this);

        //地址
        address=(TextView)findViewById(R.id.address);
        address.setOnClickListener(this);

        //提交订单
        goPay=(Button)findViewById(R.id.goPay);
        goPay.setOnClickListener(this);

        //支付方式
        payMethod=(TextView)findViewById(R.id.payMethod);
        payMethod.setOnClickListener(this);

        //积分
        point=(TextView)findViewById(R.id.point);
        point_lay=(RelativeLayout)findViewById(R.id.point_lay);

        //优惠券
        card=(TextView)findViewById(R.id.card);
        card.setOnClickListener(this);

        //商品总价
        productTotalPrice=(TextView)findViewById(R.id.productTotalPrice);

        //订单总价
        orderTotalPrice=(TextView)findViewById(R.id.orderTotalPrice);

        //优惠价格
        favourablePrice=(TextView)findViewById(R.id.favourablePrice);

        //商品列表
        order_product_list=(ListViewFitScrollView)findViewById(R.id.order_product_list);

        //实际应付
        shouldPay=(TextView)findViewById(R.id.shouldPay);

        //外层scrollview
        sv=(ScrollView)findViewById(R.id.sv);

        //是否使用积分
        userPointCb=(CheckBox)findViewById(R.id.userPointCb);
        userPointCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BigDecimal pointAmount=b.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                if (isChecked){
                    favourablePrice.setText("-￥" +pointAmount.add(new BigDecimal(cardAmount)));
                    orderTotalPrice.setText("￥"+calcOrderPrice().subtract(pointAmount));
                    shouldPay.setText("应付金额:￥"+calcOrderPrice().subtract(pointAmount));
                }else{
                    favourablePrice.setText("-￥" +cardAmount+".00");
                    orderTotalPrice.setText("￥"+calcOrderPrice());
                    shouldPay.setText("应付金额:￥"+calcOrderPrice());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_lay:

                break;
            case R.id.goPay:
                break;

            case R.id.payMethod:
                showPop();
                break;
            case R.id.address:
                if (addressAlready) {
                    Intent intent = new Intent(BalanceActivity.this, AddressActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(BalanceActivity.this,ProvinceActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ali:
                Drawable drawable = getResources().getDrawable(R.drawable.weibo);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                payMethod.setCompoundDrawables(null, null, drawable, null);
                payMethodTag=2;
                pop.dismiss();
                break;
            case R.id.wechat:
                Drawable dw = getResources().getDrawable(R.drawable.wechat);
                dw.setBounds(0, 0, dw.getMinimumWidth(), dw.getMinimumHeight());
                payMethod.setCompoundDrawables(null, null, dw, null);
                payMethodTag=1;
                pop.dismiss();
                break;
            case R.id.cod:
                Drawable dc=getResources().getDrawable(R.drawable.come_pay);
                dc.setBounds(0,0,dc.getMinimumWidth(),dc.getMinimumHeight());
                payMethod.setCompoundDrawables(null, null, dc, null);
                payMethodTag=0;
                pop.dismiss();
                break;
            case R.id.card:
                Intent intent=new Intent(BalanceActivity.this,CardActivity.class);
                intent.putExtra("totalPrice", totalPrice);
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!Constant.userId.isEmpty()) {
            getAddressList(Constant.userId);
            //getAddressList("2");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0&&resultCode==0){
            if (data!=null){
                Bundle bundle=data.getExtras();
                int cardId= bundle.getInt("cardId");
                cardAmount=bundle.getInt("cardAmount");
                Drawable drawable = getResources().getDrawable(R.drawable.card_on);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                card.setCompoundDrawables(null, null, drawable, null);
                favourablePrice.setText("-￥" + cardAmount + ".00");

                orderTotalPrice.setText("￥"+calcOrderPrice());
                shouldPay.setText("应付金额:￥"+calcOrderPrice());
            }
        }
    }

    //计算订单价格
    private BigDecimal calcOrderPrice(){
        BigDecimal pt=new BigDecimal(totalPrice);
        return pt.subtract(new BigDecimal(cardAmount));
    }

    private void getAddressList(String userId) {
        String url = Constant.baseUrl + "getAddress";
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(BalanceActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode().equals("0")) {
                    List<Map<String, Object>> s = (List) ws.getContent();
                    int size = s.size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            int isDefault = ((Double) s.get(i).get("isDefault")).intValue();
                            if (isDefault == 1) {
                                receiver.setVisibility(View.VISIBLE);
                                phone.setVisibility(View.VISIBLE);
                                receiver.setText((CharSequence) s.get(i).get("receiver"));
                                phone.setText(s.get(i).get("phoneNum").toString());
                                address.setText(s.get(i).get("city").toString() + s.get(i).get("mapAdd").toString() + s.get(i).get("detailAdd").toString());
                                addressAlready = true;
                            }
                        }
                    } else {
                        receiver.setVisibility(View.GONE);
                        phone.setVisibility(View.GONE);
                        address.setText("请添加配送地址");
                    }
                } else {
                    Toast.makeText(BalanceActivity.this, ws.getResMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getPoint(String userId){
        String url=Constant.baseUrl+"getPoint";
        Map<String,String> map=new HashMap<>();
        map.put("userId",userId);
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(BalanceActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode()!=null){
                    if (ws.getResCode().equals("0")){
                        int p=((Double)ws.getContent()).intValue();
                        if (p>0){
                             b=new BigDecimal(p);
                            point_lay.setVisibility(View.VISIBLE);
                            point.setText("可用"+p+"积分抵扣"+b.divide(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP)+"元");
                        }else {
                            point_lay.setVisibility(View.GONE);
                        }
                    }
                }else{
                    Toast.makeText(BalanceActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private  void showPop(){
        View view= LayoutInflater.from(BalanceActivity.this).inflate(R.layout.pop_pay,null);
        pop=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);

        LinearLayout cod=(LinearLayout)view.findViewById(R.id.cod);
        cod.setOnClickListener(this);
        LinearLayout ali=(LinearLayout)view.findViewById(R.id.ali);
        ali.setOnClickListener(this);
        LinearLayout wechat=(LinearLayout)view.findViewById(R.id.wechat);
        wechat.setOnClickListener(this);

        ColorDrawable cd = new ColorDrawable(0x000000);
        pop.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.4f;
        getWindow().setAttributes(lp);

        pop.setTouchable(true);
        pop.setOutsideTouchable(true);

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);

    }


}
