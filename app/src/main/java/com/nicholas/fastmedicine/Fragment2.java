package com.nicholas.fastmedicine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.CategoryListAdapter;
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.item.ProductCategory;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by eggri_000 on 2015/10/13.
 */
public class Fragment2 extends Fragment {
    private boolean isRefresh = false;
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private List<ProductCategory> itemList = new ArrayList<>();

    private CategoryListAdapter adapter;
    private RelativeLayout loading;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, null, false);

        loading=(RelativeLayout)view.findViewById(R.id.loading_lay);
        loading.setClickable(true);

        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);

        refreshLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, R.color.beautyGreen);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isRefresh){
                    isRefresh=true;
                    getCategoryList();
                }
            }
        });
        //enter SearchActivity
        ImageView imageView = (ImageView) view.findViewById(R.id.search_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) view.findViewById(R.id.categry_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Double tag = (Double) view.getTag();
                TextView titleView = (TextView) view.findViewById(R.id.list_title);
                Intent intent = new Intent(getContext(), ProductListActivity.class);
                intent.putExtra("title", titleView.getText());
                intent.putExtra("categoryId",tag);
                startActivity(intent);
            }
        });

        getCategoryList();

        return view;
    }

    private void getCategoryList() {
        String url = Constant.baseUrl+ "getProductCategoryList";
        new OkHttpRequest.Builder().url(url)
                .get(new ResultCallback<WsResponse>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), Constant.dataError, Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                        isRefresh=false;
                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(WsResponse response) {
                        if (response.getResCode().equals("0")) {
                            itemList.clear();
                            List<Map<String, Object>> s = (List) response.getContent();
                            int size = s.size();
                            for (int i = 0; i < size; i++) {
                                ProductCategory category = new ProductCategory();
                                category.setCategoryName(s.get(i).get("categoryName").toString());
                                category.setCategoryDesc(s.get(i).get("categoryDesc").toString());
                                category.setId((Double) s.get(i).get("id"));
                                itemList.add(category);
                            }
                            if (adapter==null){
                                adapter = new CategoryListAdapter(getActivity(), itemList);
                            }
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                        refreshLayout.setRefreshing(false);
                        isRefresh=false;
                        loading.setVisibility(View.GONE);
                    }
                });
    }
}
