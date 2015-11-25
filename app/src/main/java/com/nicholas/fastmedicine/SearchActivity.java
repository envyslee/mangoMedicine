package com.nicholas.fastmedicine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.common.MethodSingleton;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import litepalDB.SearchData;

/**
 * Created by eggri_000 on 2015/10/16.
 */
public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView keyword;
    private  RelativeLayout recent_row;
    private TextView hot_row;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                        if (MethodSingleton.getInstance().isNetAvailable(SearchActivity.this)) {
                            String key = keyword.getText().toString().trim();
                            if (!key.isEmpty()) {
                                if (MethodSingleton.getInstance().containsEmoji(key)) {
                                    Toast.makeText(SearchActivity.this, "不可输入表情符号", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(keyword.getWindowToken(), 0);
                                Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
                                intent.putExtra("key", key);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(SearchActivity.this, Constant.netError, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });

        keyword = (AutoCompleteTextView) findViewById(R.id.keyword);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.array_item, getResources().getStringArray(R.array.searchKeys));
        keyword.setAdapter(adapter);


        initView();
        loadRemoteSearch();
    }


    private void initView() {
        hot_row=(TextView)findViewById(R.id.hot_row);

        recent_row=(RelativeLayout)findViewById(R.id.recent_row);
        ImageView delete_search = (ImageView) findViewById(R.id.delete_search_record);
        delete_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(SearchData.class);
                loadLocalSearch();
                recent_row.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 加载热搜
     */
    private void loadRemoteSearch()
    {
        String url=Constant.baseUrl+"getHotKey";
        new OkHttpRequest.Builder().url(url).get(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                hot_row.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode().equals("0")){
                    List<String> list=(List) ws.getContent();
                    GridView   hot_search=(GridView)findViewById(R.id.hot_search);
                    ArrayAdapter adapter=new ArrayAdapter(SearchActivity.this,R.layout.search_item,list);
                    hot_search.setAdapter(adapter);
                }else{
                    hot_row.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 加载本地搜索记录
     */
    private void loadLocalSearch() {
        List<String> list = new ArrayList<>();
        Cursor cur = DataSupport.findBySQL("SELECT DISTINCT s_k FROM SearchData");
        while (cur.moveToNext()) {
            int d = cur.getColumnIndex("s_k");
            String v = cur.getString(d);
            if (v.length() > 5) {
                list.add(v.substring(0, 5) + "...");
            } else {
                list.add(v);
            }
        }
        if (list.size()>0){
            recent_row.setVisibility(View.VISIBLE);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchActivity.this, R.layout.search_item, list);
        GridView recent_search = (GridView) findViewById(R.id.recent_search_grid_view);
        recent_search.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocalSearch();
    }
}
