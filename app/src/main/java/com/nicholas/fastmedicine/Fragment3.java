package com.nicholas.fastmedicine;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.CarExpandAdapter;
import com.nicholas.fastmedicine.adapter.RecylerAdapter;
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.item.ExpandGroup;
import com.nicholas.fastmedicine.item.ProductListItem;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eggri_000 on 2015/10/13.
 */
public class Fragment3 extends Fragment implements CarExpandAdapter.isCheckListener {
    private ExpandableListView car_list;
    private CheckBox allChoose;
    private TextView totalPrice;
    private RelativeLayout loading;
    private CarExpandAdapter cea;
    private Map<String ,List<ProductListItem>> children;
    private List<ExpandGroup> group;
    private LinearLayout bottom_banner;
    private Button login_from_car;
    private LinearLayout no_result;
    private LinearLayout sub_login;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, null, false);
        initView(view);

        if (!Constant.userId.isEmpty()) {
            GetCarData();
        }else {
            loading.setVisibility(View.GONE);
            no_result.setVisibility(View.VISIBLE);
        }

        //((loadDataListener) getActivity()).clearCarCount();
        return view;
    }

    @Override
    public void ischeckgroup(int groupposition, boolean ischeck) {
        group.get(groupposition).setIsChecked(ischeck);
        List<ProductListItem> items=children.get(group.get(groupposition).getGName());
        for (ProductListItem item:items){
            item.setIsChecked(ischeck);
        }
        cea.notifyDataSetChanged();
        setTotalPrice();
    }

    @Override
    public void notAllGroup(int groupposition, boolean ischeck) {
        group.get(groupposition).setIsChecked(ischeck);
        cea.notifyDataSetChanged();
    }

    @Override
    public void updateCount(int gp, int cp, final int count) {
        final ProductListItem item=children.get(group.get(gp).getGName()).get(cp);
        item.setCount(count);
        setTotalPrice();
        /*loading.setVisibility(View.VISIBLE);
        final ProductListItem item=children.get(group.get(gp).getGName()).get(cp);
        String url=Constant.baseUrl+"checkStock";
        Map<String,String> map=new HashMap<>();
        map.put("priceId", String.valueOf(item.getPriceId().intValue()));
        map.put("count", String.valueOf(count));
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                loading.setVisibility(View.GONE);
                Toast.makeText(getActivity(),Constant.dataError,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode() != null) {
                    if (ws.getResCode().equals("0")) {
                        loading.setVisibility(View.GONE);
                        item.setCount(count);
                        setTotalPrice();
                    }else{
                        loading.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),ws.getResMsg(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    loading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),Constant.dataError,Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    @Override
    public void childIsCheck(int gp, int cp, boolean ischeck) {
        children.get(group.get(gp).getGName()).get(cp).setIsChecked(ischeck);
        cea.notifyDataSetChanged();
        setTotalPrice();
    }

    @Override
    public void clickListener(int gp, int cp) {
        ProductListItem item=children.get(group.get(gp).getGName()).get(cp);
        Intent intent=new Intent(getActivity(),ProductDetailActivity.class);
        intent.putExtra("productName",item.getProductName());
        intent.putExtra("productId",item.getProductId());
        intent.putExtra("pharmacyId", item.getPharmacyId());
        startActivity(intent);
    }


    public interface loadDataListener {
        void clearCarCount();
    }


    public   void GetCarData(){

        String url= Constant.baseUrl+"getCarList";
            Map<String ,String> map=new HashMap<>();
            map.put("userId",Constant.userId);
            new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
                @Override
                public void onError(Request request, Exception e) {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),Constant.dataError,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(WsResponse ws) {
                    if (ws.getResCode()!=null){
                        if (ws.getResCode().equals("0")){//购物车有商品
                            loading.setVisibility(View.GONE);
                            no_result.setVisibility(View.GONE);
                            bottom_banner.setVisibility(View.VISIBLE);
                            group=new ArrayList<>();
                            List<Map<String, Object>> s = (List) ws.getContent();
                            int size = s.size();
                            List<ProductListItem> tmp=new ArrayList<>();
                            if (size > 0) {
                                children=new HashMap<>();
                                for (int i = 0; i < size; i++) {
                                    ProductListItem listItem=new ProductListItem();
                                    //listItem.setIconUrl(s.get(i).get("iconUrl").toString());
                                    listItem.setProductName(s.get(i).get("productName").toString());
                                    listItem.setProductPrice((double) s.get(i).get("productPrice"));
                                    listItem.setProductSpec(s.get(i).get("productSpec").toString());
                                    listItem.setPharmacyName(s.get(i).get("pharmacyName").toString());
                                    listItem.setPharmacyId((Double) s.get(i).get("pharmacyId"));
                                    listItem.setProductId((Double) s.get(i).get("productId"));
                                    listItem.setMaxCount((Double) s.get(i).get("maxCount"));
                                    listItem.setPriceId((Double)s.get(i).get("priceId"));
                                    tmp.add(listItem);
                                }
                                for (ProductListItem item:tmp){
                                    String pn=item.getPharmacyName();
                                    if (!children.containsKey(pn)){
                                        ExpandGroup g=new ExpandGroup();
                                        g.setGName(pn);
                                        g.setIsChecked(true);
                                        group.add(g);
                                        List<ProductListItem> items=new ArrayList<>();
                                        for (ProductListItem i:tmp){
                                            if (i.getPharmacyName().equals(pn)){
                                                items.add(i);
                                            }
                                        }
                                        children.put(pn,items);
                                    }
                                }
                                    cea = new CarExpandAdapter(getActivity(), group, children);
                                car_list.setAdapter(cea);
                                cea.setIscheck(Fragment3.this);
                                setTotalPrice();
                                for (int k=0;k<group.size();k++){
                                    car_list.expandGroup(k);//全部展开
                                }
                                car_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                    @Override
                                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                        return true;//取消点击伸展
                                    }
                                });

                            }
                        }else if(ws.getResCode().equals("011")){//购物车中没有商品
                            loading.setVisibility(View.GONE);
                            if (!Constant.userId.isEmpty()) {
                                sub_login.setVisibility(View.GONE);
                            }
                        }
                    }else{//返回数据为null
                        loading.setVisibility(View.GONE);
                    }
                }
            });
    }

    /**
     * 设置总价
     */
    private void setTotalPrice(){
        BigDecimal price=new BigDecimal("0");
        for (List<ProductListItem> item:children.values()){
            for (ProductListItem p:item){
                if (p.isChecked()&&p.getMaxCount()>0) {
                    BigDecimal pb = new BigDecimal(p.getProductPrice());
                    BigDecimal cb = new BigDecimal(p.getCount());
                    price = price.add(pb.multiply(cb)).setScale(1,RoundingMode.HALF_UP);
                }
            }
        }
        totalPrice.setText("总价:"+price);
    }



    private void initView(View view){
        loading=(RelativeLayout)view.findViewById(R.id.loading_lay);
        car_list=(ExpandableListView)view.findViewById(R.id.car_list);
        allChoose=(CheckBox)view.findViewById(R.id.allChoose);
        totalPrice=(TextView)view.findViewById(R.id.totalPrice);
        bottom_banner=(LinearLayout)view.findViewById(R.id.bottom_banner);
        //search_no_img=(ImageView)view.findViewById(R.id.search_no_img);
        no_result=(LinearLayout)view.findViewById(R.id.no_result);
        sub_login=(LinearLayout)view.findViewById(R.id.sub_login);
        login_from_car=(Button)view.findViewById(R.id.login_from_car);
        login_from_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        allChoose.setChecked(true);
        allChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    totalPrice.setText("总价:" + 0);
                }
                int size = group.size();
                for (int i = 0; i < size; i++) {
                    group.get(i).setIsChecked(isChecked);
                    ischeckgroup(i, isChecked);
                    cea.notifyDataSetChanged();
                }
            }
        });
        car_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getActivity(),  "dd"  ,Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

}
