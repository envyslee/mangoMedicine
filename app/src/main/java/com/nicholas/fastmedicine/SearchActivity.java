package com.nicholas.fastmedicine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eggri_000 on 2015/10/16.
 */
public class SearchActivity extends AppCompatActivity {




    private TextView keyword;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
/*     MenuItem   delItem=  menu.findItem(R.id.delete_btn);
        delItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);*/
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        keyword=(TextView)findViewById(R.id.keyword);


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
                    /*case R.id.delete_btn:
                        keyword.setText("");
                        break;*/
                    case R.id.search_btn:
                        Toast.makeText(SearchActivity.this, keyword.getText(), Toast.LENGTH_SHORT).show();

                    break;
                }

                return true;
            }
        });
    }

    private void postSearch(String url,String key){
        Map<String ,String> map=new HashMap<>();
        map.put("keyword",key);
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(SearchActivity.this, "请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode().equals("0")){

                }else{
                    Toast.makeText(SearchActivity.this, ws.getResMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
