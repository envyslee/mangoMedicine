package com.nicholas.fastmedicine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.CategoryListAdapter;
import com.nicholas.fastmedicine.item.CategoryItem;
import com.nicholas.fastmedicine.item.ProductCategory;
import com.nicholas.fastmedicine.item.ProductListItem;
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
    private ListView listView;
    private List<ProductCategory> itemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, null, false);

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



       /* RequestQueue mQueue= Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest("http://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        content.setText(response);
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                content.setText(error.toString());
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);*/


        return view;
    }

    private void getCategoryList() {
        String url = "http://10.151.11.103:8080/fastMedicine/medicine/getProductCategoryList";
        new OkHttpRequest.Builder().url(url)
                .get(new ResultCallback<WsResponse>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "获取数据出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(WsResponse response) {
                        if (response.getResCode().equals("0")) {
                            List<Map<String, Object>> s = (List) response.getContent();
                            int size = s.size();
                            for (int i = 0; i < size; i++) {
                                ProductCategory category = new ProductCategory();
                                category.setCategoryName(s.get(i).get("categoryName").toString());
                                category.setCategoryDesc(s.get(i).get("categoryDesc").toString());
                                category.setId((Double) s.get(i).get("id"));
                                itemList.add(category);
                            }
                            CategoryListAdapter adapter = new CategoryListAdapter(getActivity(), itemList);
                            listView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity(), "获取数据出错", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
