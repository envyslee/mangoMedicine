package com.nicholas.fastmedicine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nicholas.fastmedicine.adapter.CategoryListAdapter;

/**
 * Created by eggri_000 on 2015/10/13.
 */
public class Fragment2 extends BaseFragment {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, null, false);

        //enter SearchActivity
        ImageView imageView=(ImageView)view.findViewById(R.id.search_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });


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
