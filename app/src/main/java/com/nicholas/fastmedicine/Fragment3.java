package com.nicholas.fastmedicine;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
            item.setIscheck(ischeck);
        }
        cea.notifyDataSetChanged();
    }

    @Override
    public void notAllGroup(int groupposition, boolean ischeck) {
        group.get(groupposition).setIsChecked(ischeck);
        cea.notifyDataSetChanged();
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
                        if (ws.getResCode().equals("0")){
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
                                                items.add(item);
                                            }
                                        }
                                        children.put(pn,items);
                                    }
                                }
                                if (cea==null) {
                                    cea = new CarExpandAdapter(getActivity(), group, children);
                                }
                                cea.setIscheck(Fragment3.this);
                                car_list.setAdapter(cea);

                                for (int k=0;k<group.size();k++){
                                    car_list.expandGroup(k);
                                }
                                car_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                    @Override
                                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                        return true;
                                    }
                                });
                                loading.setVisibility(View.GONE);
                            }
                        }else if(ws.getResCode().equals("011")){
                            loading.setVisibility(View.GONE);
                        }
                    }else{
                        loading.setVisibility(View.GONE);
                    }
                }
            });
    }

    private void initView(View view){
        loading=(RelativeLayout)view.findViewById(R.id.loading_lay);
        car_list=(ExpandableListView)view.findViewById(R.id.car_list);
        allChoose=(CheckBox)view.findViewById(R.id.allChoose);
        totalPrice=(TextView)view.findViewById(R.id.totalPrice);
        bottom_banner=(LinearLayout)view.findViewById(R.id.bottom_banner);
        //search_no_img=(ImageView)view.findViewById(R.id.search_no_img);
        no_result=(LinearLayout)view.findViewById(R.id.no_result);
        login_from_car=(Button)view.findViewById(R.id.login_from_car);
        login_from_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
