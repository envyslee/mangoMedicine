package com.nicholas.fastmedicine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.common.MethodSingleton;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProvinceActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private ListView list_province;
    private Bundle bundle;
    private TextView address_et, city_et;
    private Button address_btn;
    private EditText reciever_et, phone_et, detail_address;
    private boolean locateSuccess = false;
    private String mapLongAdd = "";
    private RelativeLayout loading;

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

        initView();

    }

    private void initView() {
        loading = (RelativeLayout) findViewById(R.id.loading_lay);
        loading.setVisibility(View.GONE);

        city_et = (TextView) findViewById(R.id.city_et);
        String c = Constant.cityName;
        if (c.isEmpty()) {
            city_et.setText("获取城市信息失败");
            locateSuccess = false;

        } else {
            locateSuccess = true;
            city_et.setText(c);
        }

        address_et = (TextView) findViewById(R.id.address_et);
        address_et.setOnClickListener(this);

        address_btn = (Button) findViewById(R.id.address_btn);
        address_btn.setOnClickListener(this);

        reciever_et = (EditText) findViewById(R.id.reciever_et);
        phone_et = (EditText) findViewById(R.id.phone_et);
        detail_address = (EditText) findViewById(R.id.detail_address);

        address_et.addTextChangedListener(this);
        reciever_et.addTextChangedListener(this);
        phone_et.addTextChangedListener(this);
        detail_address.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_btn:
                if (locateSuccess) {
                    String receiver = reciever_et.getText().toString();
                    String phone = phone_et.getText().toString();
                    String mapAdd = address_et.getText().toString();
                    String detailAdd = detail_address.getText().toString();
                    if (!MethodSingleton.getInstance().isMobileNum(phone)) {
                        Toast.makeText(ProvinceActivity.this, "手机号码不正确", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (receiver.length() > 10) {
                        Toast.makeText(ProvinceActivity.this, "请填入合适的姓名", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    submitAddress(Constant.cityName, receiver, phone, mapAdd, detailAdd, mapLongAdd);
                } else {
                    Toast.makeText(ProvinceActivity.this, "定位失败，请允许相应权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.address_et:
                if (locateSuccess) {
                Intent intent = new Intent(ProvinceActivity.this, MapActivity.class);
                bundle = new Bundle();
                bundle.putDouble("latitude", Constant.latitude);//维度
                bundle.putDouble("lontitude", Constant.lontitude);//经度
                bundle.putFloat("radius", Constant.radius);//方向，0-360度
                bundle.putString("city", Constant.cityName);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(ProvinceActivity.this, "定位失败，请允许相应权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void submitAddress(String c, String r, String p, String m, String d, String l) {
        loading.setVisibility(View.VISIBLE);
        String url = Constant.baseUrl + "postAddress";
        Map<String, String> map = new HashMap<>();
        map.put("userId", "2");
        map.put("city", c);
        map.put("receiver", r);
        map.put("phone", p);
        map.put("mapAdd", m);
        map.put("detailAdd", d);
        map.put("mapLongAdd", l);
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                loading.setVisibility(View.GONE);
                Toast.makeText(ProvinceActivity.this, "提交失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode().equals("0")) {
                    finish();
                } else {
                    Toast.makeText(ProvinceActivity.this, ws.getResMsg(), Toast.LENGTH_SHORT).show();
                }
                loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 0) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String poiName = bundle.getString("poiName");
                address_et.setText(poiName);
                mapLongAdd = bundle.getString("poiAddress");
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(reciever_et.getText().toString())||TextUtils.isEmpty(phone_et.getText().toString())||TextUtils.isEmpty(detail_address.getText().toString())||TextUtils.isEmpty(address_et.getText().toString())) {
            address_btn.setEnabled(false);
        }else{
            address_btn.setEnabled(true);
        }
    }
}
