package com.nicholas.fastmedicine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.RecylerAdapter;
import com.nicholas.fastmedicine.controller.RecylerDivider;

import java.util.ArrayList;
import java.util.List;

public class SearchListActivity extends AppCompatActivity {

    private List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("商品列表");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
        RecylerAdapter adapter=new RecylerAdapter(this,mDatas);
        adapter.SetOnClickListener(new RecylerAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(SearchListActivity.this,position+" Click",Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.my_recylerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new RecylerDivider(this));



    }

    protected void initData()
    {
        mDatas = new ArrayList<>();
        for (int i = 0; i <6; i++)
        {
            mDatas.add(i+"");
        }
    }
}
