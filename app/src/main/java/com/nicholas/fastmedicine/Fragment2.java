package com.nicholas.fastmedicine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nicholas.fastmedicine.adppter.CategoryListAdapter;

/**
 * Created by eggri_000 on 2015/10/13.
 */
public class Fragment2 extends BaseFragment {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, null, false);


        listView = (ListView) view.findViewById(R.id.categry_listview);
        CategoryListAdapter adapter = new CategoryListAdapter(getActivity(), Images.Get_LIST_DATAS());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView titleView = (TextView) view.findViewById(R.id.list_title);
                Intent intent = new Intent(getContext(), ProductListActivity.class);
                intent.putExtra("title", titleView.getText());
                startActivity(intent);

            }
        });

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
}
