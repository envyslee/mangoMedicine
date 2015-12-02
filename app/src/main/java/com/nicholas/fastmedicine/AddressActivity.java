package com.nicholas.fastmedicine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.AddressListAdapter;
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.item.AddressListItem;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    private ListView addr_list;
    private List<AddressListItem> addresslist=new ArrayList<>();
    private RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("地址管理");
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
                if (item.getItemId() == R.id.add_address) {
                    Intent intent = new Intent(AddressActivity.this, ProvinceActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        loading=(RelativeLayout)findViewById(R.id.loading_lay);

        addr_list=(ListView)findViewById(R.id.addr_list);
        addr_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loading.setVisibility(View.VISIBLE);
                updateDefaultAddress("1",addresslist.get(position).getId().toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_address, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (addresslist.size()>0) {
            addresslist.clear();
        }
        getAddressList("1");
    }

    private void updateDefaultAddress(String userId,String addressId){
        String url=Constant.baseUrl+"updateDefaultAddress";
        Map<String,String> map=new HashMap<>();
        map.put("userId",userId);
        map.put("addressId",addressId);
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                loading.setVisibility(View.GONE);
                Toast.makeText(AddressActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode()!=null){
                    if (ws.getResCode().equals("0")){
                        finish();
                    }else {
                        Toast.makeText(AddressActivity.this, ws.getResMsg(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddressActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
                }
                loading.setVisibility(View.GONE);
            }
        });
    }

    private void getAddressList(String userId){
        String url= Constant.baseUrl+"getAddress";
        Map<String,String> map=new HashMap<>();
        map.put("userId",userId);
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                loading.setVisibility(View.GONE);
                Toast.makeText(AddressActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode().equals("0")){
                List<Map<String, Object>> s = (List) ws.getContent();
                        int size = s.size();
                        for (int i = 0; i < size; i++) {
                            AddressListItem item=new AddressListItem();
                            item.setCity(s.get(i).get("city").toString());
                            item.setDetailAdd(s.get(i).get("detailAdd").toString());
                            item.setMapAdd(s.get(i).get("mapAdd").toString());
                            item.setPhoneNum(s.get(i).get("phoneNum").toString());
                            item.setReceiver(s.get(i).get("receiver").toString());
                            item.setId(((Double) s.get(i).get("id")).intValue());
                            addresslist.add(item);
                        }
                    AddressListAdapter adapter=new AddressListAdapter(AddressActivity.this,R.layout.address_list,addresslist);
                    addr_list.setAdapter(adapter);
                }else{
                    Toast.makeText(AddressActivity.this, ws.getResMsg(), Toast.LENGTH_SHORT).show();
                }
                loading.setVisibility(View.GONE);
            }
        });
    }
}
