package com.nicholas.fastmedicine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nicholas.fastmedicine.adapter.ReviewListAdapter;
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.item.ProductListItem;
import com.nicholas.fastmedicine.item.ReviewItem;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {


    private RelativeLayout loading;
    private String priceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("商品评价");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
    }

    private void initData(){
        ListView review_list=(ListView)findViewById(R.id.review_list);
        Bundle bundle=getIntent().getExtras();
        List<ReviewItem> reviews = (List<ReviewItem>) bundle.getSerializable("reviews");
        ReviewListAdapter a=new ReviewListAdapter(ReviewActivity.this,R.layout.review_list_item,reviews);
        review_list.setAdapter(a);
    }


}
