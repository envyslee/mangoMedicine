package com.nicholas.fastmedicine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SelfCheckListActivity extends AppCompatActivity {

    private int index;//记录上一次被选中的area item
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_check_list);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("常见病症");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Map<String,String> map=new HashMap<>();
        //读取json
        try {
            InputStream is = this.getResources().openRawResource(R.raw.self_check);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            //中文乱码
            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = (JSONObject) jsonArray.opt(i);
                map.put(item.getString("area"),item.getString("disease"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ListView areaList=(ListView)findViewById(R.id.area);
        final ListView diseaseList=(ListView)findViewById(R.id.disease);
        ArrayAdapter areaAdapter=new ArrayAdapter(this,R.layout.array_item,map.keySet().toArray());

        areaList.setAdapter(areaAdapter);
        areaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v=(TextView)parent.getChildAt(index);
                v.setTextColor(getResources().getColor(R.color.smssdk_black));
                index=position;
                TextView tv=(TextView)view;
                tv.setTextColor(getResources().getColor(R.color.beautyGreen));
                String area=tv.getText().toString();
                String diseaseStr=map.get(area);
                String[] diseaseData=  diseaseStr.split("\\|");
                ArrayAdapter diseaseAdapter=new ArrayAdapter(SelfCheckListActivity.this,R.layout.array_item,diseaseData);
                diseaseList.setAdapter(diseaseAdapter);

            }
        });

    }

}
