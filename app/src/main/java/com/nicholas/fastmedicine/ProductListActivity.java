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

import com.nicholas.fastmedicine.adapter.ProductListAdapter;
import com.nicholas.fastmedicine.item.CategoryItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener {

    private List<CategoryItem> categoryItems;
    private ProductListAdapter productListAdapter;
    private boolean sortbyprice=false;
    private boolean sortbysales=false;
    private int index=0;
    private boolean haveData=true;
    private RelativeLayout loading_lay;
    private ListView product_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
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



        loading_lay=(RelativeLayout)findViewById(R.id.loading_lay);
        loading_lay.setVisibility(View.VISIBLE);

        categoryItems=Images.Get_Product(index);
        productListAdapter = new ProductListAdapter(this, categoryItems);
        product_list = (ListView) findViewById(R.id.product_list);
        product_list.setAdapter(productListAdapter);
        loading_lay.setVisibility(View.INVISIBLE);

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
                String title=((TextView)view.findViewById(R.id.list_name)).getText().toString();
                Intent intent=new Intent(ProductListActivity.this,ProductDetialActivity.class);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });

        Button sale_btn=(Button)findViewById(R.id.sort_sale);
        Button price_btn=(Button)findViewById(R.id.sort_price);
        sale_btn.setOnClickListener(this);
        price_btn.setOnClickListener(this);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    /**
     * 滑动到底部加载更多
     */
    private void AddMoreData()
    {
        if (haveData)
        {
            loading_lay.setVisibility(View.VISIBLE);
            index++;
            List<CategoryItem> tmpItems = Images.Get_Product(index);
            if (tmpItems.size() > 0) {
                categoryItems.addAll(tmpItems);
                productListAdapter.notifyDataSetChanged();
            } else {
                haveData = false;
            }

            loading_lay.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sort_sale:
                if (!sortbysales)
                {
                    Collections.sort(categoryItems, new Comparator<CategoryItem>() {
                        @Override
                        public int compare(CategoryItem lhs, CategoryItem rhs) {
                            return lhs.getSales().compareTo(rhs.getSales());
                        }
                    });
                    sortbysales=true;
                }
                else
                {
                    Collections.reverse(categoryItems);
                }
                productListAdapter.notifyDataSetChanged();
                break;

            case R.id.sort_price:
                if (!sortbyprice)
                {
                    Collections.sort(categoryItems, new Comparator<CategoryItem>() {
                        @Override
                        public int compare(CategoryItem lhs, CategoryItem rhs) {
                            return  lhs.getPrice().compareTo(rhs.getPrice());
                        }
                    });
                    sortbyprice=true;
                }
                else
                {
                    Collections.reverse(categoryItems);
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
