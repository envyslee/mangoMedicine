package com.nicholas.fastmedicine;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.ProductListAdapter;
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.item.CategoryItem;
import com.nicholas.fastmedicine.item.ProductListItem;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener {
    private ProductListAdapter productListAdapter;
    private boolean sortbyprice = false;
    private boolean sortbysales = false;
    private Integer index = 0;
    private boolean haveData = true;
    private RelativeLayout loading_lay;
    private ListView product_list;
    private List<ProductListItem> productList = new ArrayList<>();
    private Integer categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        categoryId = (int) bundle.getDouble("categoryId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loading_lay = (RelativeLayout) findViewById(R.id.loading_lay);

        product_list = (ListView) findViewById(R.id.product_list);

        getData(categoryId, index);
        product_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        AddMoreData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        //进入商品详情
        product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String productName = ((TextView) view.findViewById(R.id.list_name)).getText().toString();
                Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                Map<String, Double> map = (Map) view.getTag();
                intent.putExtra("productName", productName);
                intent.putExtra("productId", map.get("productId"));
                intent.putExtra("pharmacyId", map.get("pharmacyId"));
                startActivity(intent);
            }
        });

        Button sale_btn = (Button) findViewById(R.id.sort_sale);
        Button price_btn = (Button) findViewById(R.id.sort_price);
        sale_btn.setOnClickListener(this);
        price_btn.setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    private void getData(Integer categoryId, Integer i) {
        String url = Constant.baseUrl;
        if (categoryId == 0) {
            url += "postSprecialPrice";
        } else {
            url += "postProductList";
        }
        Map<String, String> map = new HashMap<>();
        map.put("categoryId", categoryId.toString());
        if (Constant.lontitude != null && Constant.latitude != null) {
            map.put("lo", Constant.lontitude.toString());
            map.put("la", Constant.latitude.toString());
            map.put("index", i.toString());
            new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
                @Override
                public void onError(Request request, Exception e) {
                    Toast.makeText(ProductListActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
                    loading_lay.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(WsResponse response) {
                    if (response.getResCode()==null){
                        Toast.makeText(ProductListActivity.this, Constant.dataError, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    if (response.getResCode().equals("0")) {
                        List<Map<String, Object>> s = (List) response.getContent();
                        int size = s.size();
                        if (size > 0) {
                            for (int i = 0; i < size; i++) {
                                ProductListItem item = new ProductListItem();
                                item.setProductName(s.get(i).get("productName").toString());
                                item.setProductDesc(s.get(i).get("productDesc").toString());
                                item.setProductSpec(s.get(i).get("productSpec").toString());
                                double tp = (double) s.get(i).get("productSale");
                                item.setProductSale((int) tp);
                                item.setProductPrice((Double) s.get(i).get("productPrice"));
                                item.setProductId((Double) s.get(i).get("productId"));
                                item.setPharmacyId((Double) s.get(i).get("pharmacyId"));
                                productList.add(item);
                            }
                            if (productListAdapter == null) {
                                productListAdapter = new ProductListAdapter(ProductListActivity.this, productList);
                                product_list.setAdapter(productListAdapter);
                            } else {
                                productListAdapter.notifyDataSetChanged();
                            }
                            loading_lay.setVisibility(View.GONE);
                            index++;
                        }else{
                            haveData=false;
                            loading_lay.setVisibility(View.GONE);
                        }
                    }
                }
            });
        } else {
            Toast.makeText(ProductListActivity.this, Constant.mapError, Toast.LENGTH_SHORT).show();
            loading_lay.setVisibility(View.GONE);
        }
    }

    /**
     * 滑动到底部加载更多
     */
    private void AddMoreData() {
        if (haveData) {
            loading_lay.setVisibility(View.VISIBLE);
            getData(categoryId, index);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sort_sale:
                if (!sortbysales) {
                    Collections.sort(productList, new Comparator<ProductListItem>() {
                        @Override
                        public int compare(ProductListItem lhs, ProductListItem rhs) {
                            return lhs.getProductSale().compareTo(rhs.getProductSale());
                        }
                    });
                    sortbysales = true;
                } else {
                    Collections.reverse(productList);
                }
                productListAdapter.notifyDataSetChanged();
                break;

            case R.id.sort_price:
                if (!sortbyprice) {
                    Collections.sort(productList, new Comparator<ProductListItem>() {
                        @Override
                        public int compare(ProductListItem lhs, ProductListItem rhs) {
                            return lhs.getProductPrice().compareTo(rhs.getProductPrice());
                        }
                    });
                    sortbyprice = true;
                } else {
                    Collections.reverse(productList);
                }

                //productListAdapter.setData(categoryItems);
                productListAdapter.notifyDataSetChanged();
                break;
            case R.id.fab:
                //直接回到顶部
                //product_list.setSelection(0);
                //滚到顶部
                product_list.smoothScrollToPosition(0);
                break;
            default:
                break;
        }
    }
}
