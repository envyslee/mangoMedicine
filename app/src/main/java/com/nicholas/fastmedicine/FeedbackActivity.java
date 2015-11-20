package com.nicholas.fastmedicine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.common.MethodSingleton;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    private TextView charctorcountTv;
    private Button feedback_btn;
    private EditText contentEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.feedback_str));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        charctorcountTv = (TextView) findViewById(R.id.charctorcount);
        contentEt = (EditText) findViewById(R.id.contentEt);
        contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 120) {
                    charctorcountTv.setText(s.length() + "/120");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        feedback_btn = (Button) findViewById(R.id.feedback_btn);
        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = contentEt.getText().toString();
                if (c == null || c.isEmpty()) {
                    return;
                }
                String appVersion = MethodSingleton.getInstance().getVersionName(FeedbackActivity.this);
                String deviceModel = MethodSingleton.getInstance().getDeviceModel();
                String osVersion = MethodSingleton.getInstance().getOSVersion();
                String url = Constant.baseUrl + "postFeedback";
                postFeedback(url, appVersion, deviceModel, osVersion, "1", c);
            }
        });
    }

    private void postFeedback(String url, String v, String d, String o, String userId, String c) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("feedContent", c);
        map.put("appVersion", v);
        map.put("deviceModel", d);
        map.put("deviceOS", "Android");
        map.put("osVersion", o);

        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(FeedbackActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode().equals("0")) {
                    Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FeedbackActivity.this, ws.getResMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
