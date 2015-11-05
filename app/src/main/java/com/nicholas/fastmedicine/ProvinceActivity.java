package com.nicholas.fastmedicine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nicholas.fastmedicine.common.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProvinceActivity extends AppCompatActivity {

    private ListView list_province;
    private Bundle bundle;
    private TextView address_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("添加地址");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       /*list_province=(ListView)findViewById(R.id.list_province);
        ArrayAdapter<String> pAdapter=new ArrayAdapter<>(this,R.layout.address_list_item,ReadProvince());
        list_province.setAdapter(pAdapter);*/
        TextView city_et = (TextView) findViewById(R.id.city_et);
        String c = Constant.cityName;
        if (c.isEmpty()) {
            city_et.setText("获取城市信息失败");
        } else {
            city_et.setText(c);
        }

        address_et= (TextView) findViewById(R.id.address_et);
        address_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProvinceActivity.this, MapActivity.class);
                bundle = new Bundle();
                bundle.putDouble("latitude", Constant.latitude);//经度
                bundle.putDouble("lontitude", Constant.lontitude);//维度
                bundle.putFloat("radius", Constant.radius);//方向，0-360度
                bundle.putString("city", Constant.cityName);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0&&resultCode==0)
        {
            Bundle bundle=data.getExtras();
            String poiName=bundle.getString("poiName");
            address_et.setText(poiName);
        }
    }


}
