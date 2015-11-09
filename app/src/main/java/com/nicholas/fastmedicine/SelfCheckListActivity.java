package com.nicholas.fastmedicine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.ArrayColorAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelfCheckListActivity extends AppCompatActivity {


    private List<String> diseaseData=new ArrayList<>();
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
            Toast.makeText(this,"数据读取错误",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        final ListView areaList=(ListView)findViewById(R.id.area);
        final ListView diseaseList=(ListView)findViewById(R.id.disease);

        final ArrayColorAdapter areaAdapter=new ArrayColorAdapter(this,new ArrayList<>(map.keySet()));
        areaList.setAdapter(areaAdapter);
        for (String i:map.get("头面部").split("\\|"))
        {
            diseaseData.add(i);
        }
        final ArrayAdapter diseaseAdapter=new ArrayAdapter(SelfCheckListActivity.this,R.layout.array_item, diseaseData);
        diseaseList.setAdapter(diseaseAdapter);

        areaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaAdapter.CurrentPosition(position);
                areaAdapter.notifyDataSetChanged();
                TextView tv = (TextView) view;
                String area = tv.getText().toString();
                String diseaseStr = map.get(area);
                diseaseData.clear();
                for (String item : diseaseStr.split("\\|")) {
                    diseaseData.add(item);
                }
                diseaseAdapter.notifyDataSetChanged();
            }
        });

    }

}
