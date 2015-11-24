package com.nicholas.fastmedicine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.RecylerAdapter;
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.common.MethodSingleton;
import com.nicholas.fastmedicine.item.ProductListItem;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import litepalDB.SearchData;

public class SearchListActivity extends AppCompatActivity {

    private List<ProductListItem> productList=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecylerAdapter adapter;
    private AutoCompleteTextView keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        keyword=(AutoCompleteTextView)findViewById(R.id.keyword);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.array_item, getResources().getStringArray(R.array.searchKeys));
        keyword.setAdapter(adapter);

        //recyclerView获取焦点，否则软键盘会自动弹出
        recyclerView=(RecyclerView)findViewById(R.id.my_recylerview);
        recyclerView.setFocusable(true);
        recyclerView.setFocusableInTouchMode(true);
        recyclerView.requestFocus();
        recyclerView.requestFocusFromTouch();
        String key= getIntent().getExtras().getString("key");
        postSearch(Constant.baseUrl + "postSearch", key);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
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
                switch (item.getItemId()) {
                    case R.id.search_btn:
                        if (MethodSingleton.getInstance().isNetAvailable(SearchListActivity.this)) {
                            String key = keyword.getText().toString().trim();
                            if (!key.isEmpty()) {
                                InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(keyword.getWindowToken(),0);
                                postSearch(Constant.baseUrl + "postSearch", key);
                            }
                        } else {
                            Toast.makeText(SearchListActivity.this, Constant.netError, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    private void postSearch(String url,String key) {
        if (Constant.lontitude != null && Constant.latitude != null) {
            if (MethodSingleton.getInstance().containsEmoji(key)){
                Toast.makeText(SearchListActivity.this,"不可输入表情符号",Toast.LENGTH_SHORT).show();
                return;
            }
            productList.clear();
            Map<String, String> map = new HashMap<>();
            map.put("keyword", key);
            map.put("lo", Constant.lontitude.toString());
            map.put("la", Constant.latitude.toString());
            map.put("userId", Constant.userId);

            new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
                @Override
                public void onError(Request request, Exception e) {
                    Toast.makeText(SearchListActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(WsResponse ws) {
                    if (ws.getResCode().equals("0")) {
                        List<Map<String, Object>> s = (List) ws.getContent();
                        int size = s.size();
                        for (int i = 0; i < size; i++) {
                            ProductListItem item = new ProductListItem();
                            item.setProductName(s.get(i).get("productName").toString());
                            //item.setProductDesc(s.get(i).get("productDesc").toString());
                            //item.setProductSpec(s.get(i).get("productSpec").toString());
                            double tp = (double) s.get(i).get("productSale");
                            item.setProductSale((int) tp);
                            item.setProductPrice((Double) s.get(i).get("productPrice"));
                            item.setProductId((Double) s.get(i).get("productId"));
                            item.setPharmacyId((Double) s.get(i).get("pharmacyId"));
                            productList.add(item);
                        }
                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        if (adapter == null) {
                            adapter = new RecylerAdapter(SearchListActivity.this, productList);
                        }
                        adapter.notifyDataSetChanged();
                        adapter.SetOnClickListener(new RecylerAdapter.OnClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String productName = ((TextView) view.findViewById(R.id.search_list_name)).getText().toString();
                                Intent intent = new Intent(SearchListActivity.this, ProductDetailActivity.class);
                                Map<String, Double> map = (Map) view.getTag();
                                intent.putExtra("productName", productName);
                                intent.putExtra("productId", map.get("productId"));
                                intent.putExtra("pharmacyId", map.get("pharmacyId"));
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                        //recyclerView.addItemDecoration(new RecylerDivider(this));
                    } else {
                        Toast.makeText(SearchListActivity.this, ws.getResMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //保存搜索记录
            SearchData searchData=new SearchData(key);
            searchData.save();
        }else{
            Toast.makeText(SearchListActivity.this, "经纬度加载失败，请稍后再试", Toast.LENGTH_SHORT).show();
        }
    }

}
