package com.nicholas.fastmedicine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.common.MD5Util;
import com.nicholas.fastmedicine.common.MethodSingleton;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

import litepalDB.UserInfo;

/**
 * Created by eggri_000 on 2015/10/13.
 */
public class LoginActivity extends AppCompatActivity implements TextWatcher {

    private EditText userName;
    private EditText password;
    private Button login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("登录");
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
                String msg = "";
                switch (item.getItemId()) {
                    case R.id.register_activity:
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivityForResult(intent, 1);
                        //startActivity(intent);
                        break;
                }
                return true;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            if (data != null) {
                if (data.getExtras().getBoolean("done")) {
                    finish();
                }
            }
        }
    }

    private void initView() {
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        userName.addTextChangedListener(this);
        password.addTextChangedListener(this);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(Constant.baseUrl + "postLogin", userName.getText().toString(), password.getText().toString());
            }
        });
    }

    private void Login(String url,final String name, String password) {
        final String mp=MD5Util.MD5(password);
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", name);
        map.put("password",mp);
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(LoginActivity.this, "登录失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode()==null){
                    Toast.makeText(LoginActivity.this, "登录失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ws.getResCode().equals("0")) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //本地保存用户数据
                    DataSupport.deleteAll(UserInfo.class);
                    UserInfo info=new UserInfo(name,mp,ws.getContent().toString());
                    info.save();
                    Constant.userId=info.getU_i();
                    Constant.userNum=info.getU_n();
                    //返回
                    finish();
                } else if (ws.getResCode().equals("005") || ws.getResCode().equals("004")) {
                    Toast.makeText(LoginActivity.this, ws.getResMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String n = userName.getText().toString();
        String p = password.getText().toString();
        if (n == null || n.isEmpty()||!MethodSingleton.getInstance().isMobileNum(n)||p == null || p.isEmpty()) {
           login_btn.setEnabled(false);
        }else {
            login_btn.setEnabled(true);
        }
    }
}
