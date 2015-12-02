package com.nicholas.fastmedicine;

import android.graphics.LinearGradient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.CardAdapter;
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.item.MyCard;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardActivity extends AppCompatActivity {

    private  ImageView noresult;
    private ListView card_list;
    private RelativeLayout loading;
    private double totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("优惠券");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        totalPrice=getIntent().getExtras().getDouble("totalPrice");
        initView();
    }

    private void initView(){
        loading=(RelativeLayout)findViewById(R.id.loading_lay);
        noresult=(ImageView)findViewById(R.id.search_no_img);
        card_list=(ListView)findViewById(R.id.card_list);
        card_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CardActivity.this,position+"",Toast.LENGTH_SHORT).show();
            }
        });
        if (!Constant.userId.isEmpty()){
            getCardData(Constant.userId);
        }
    }

    private void getCardData(String userId){
        String url= Constant.baseUrl+"getCard";
        Map<String,String> map=new HashMap<>();
        map.put("userId",userId);
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                loading.setVisibility(View.GONE);
                noresult.setVisibility(View.VISIBLE);
                Toast.makeText(CardActivity.this,Constant.dataError,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode()!=null){
                    if (ws.getResCode().equals("0")){
                        List<MyCard> cards=new ArrayList<>();
                        List<Map<String,Object>> s=(List)ws.getContent();
                       int size= s.size();
                        if (size>0){
                                for (int i=0;i<size;i++){
                                    Map<String,Object> m=s.get(i);
                                    double time=(double)m.get("overTime");
                                    SimpleDateFormat format=new SimpleDateFormat("yyyy.MM.dd");
                                    MyCard myCard=new MyCard(((Double)m.get("cardAmount")).intValue(),((Double)m.get("useConditon")).intValue(), format.format(time),m.get("cardName").toString(),m.get("cardDesc").toString());
                                    cards.add(myCard);
                                }
                            CardAdapter ca=new CardAdapter(CardActivity.this,R.layout.card_list_item,cards,totalPrice);
                            card_list.setAdapter(ca);
                            loading.setVisibility(View.GONE);
                        }else{
                            loading.setVisibility(View.GONE);
                            noresult.setVisibility(View.VISIBLE);
                            Toast.makeText(CardActivity.this,ws.getResMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        loading.setVisibility(View.GONE);
                        noresult.setVisibility(View.VISIBLE);
                        Toast.makeText(CardActivity.this,ws.getResMsg(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loading.setVisibility(View.GONE);
                    noresult.setVisibility(View.VISIBLE);
                    Toast.makeText(CardActivity.this,Constant.dataError,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

