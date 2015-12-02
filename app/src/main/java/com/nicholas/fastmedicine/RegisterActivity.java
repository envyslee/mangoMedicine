package com.nicholas.fastmedicine;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.common.MD5Util;
import com.nicholas.fastmedicine.common.MethodSingleton;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import litepalDB.UserInfo;

/**
 * Created by eggri_000 on 2015/10/14.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private int recLen = 0;
    private Button sensmsButton, verificationButton;
    private EditText phonEditText, verEditText,passwordEditText,passwordConfirmEditText;

    public String phString;
    private String passwordStr;
    private String passwordConfirmStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //短信sdk
        SMSSDK.initSDK(this, Constant.mobAppkey, Constant.mobAppsecret);

        sensmsButton = (Button) findViewById(R.id.button1);
        verificationButton = (Button) findViewById(R.id.button2);
        phonEditText = (EditText) findViewById(R.id.editText1);
        verEditText = (EditText) findViewById(R.id.editText2);
        passwordEditText=(EditText)findViewById(R.id.password);
        passwordConfirmEditText=(EditText)findViewById(R.id.password_confirm);
        sensmsButton.setOnClickListener(this);
        verificationButton.setOnClickListener(this);



        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1://发送验证码
                if (MethodSingleton.getInstance().isMobileNum(phonEditText.getText().toString())) {
                    SMSSDK.getVerificationCode("86", phonEditText.getText().toString());
                    timerHandler.postDelayed(runnable, 1000);
                    phString = phonEditText.getText().toString();
                } else {
                    Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button2://提交注册，校验验证码
                if (!TextUtils.isEmpty(verEditText.getText().toString())||!TextUtils.isEmpty(passwordEditText.getText().toString())||!TextUtils.isEmpty(passwordConfirmEditText.getText().toString())){
                    passwordStr=passwordEditText.getText().toString();
                    passwordConfirmStr=passwordConfirmEditText.getText().toString();
                    if (!passwordStr.equals(passwordConfirmStr))
                    {
                        Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SMSSDK.submitVerificationCode("86", phString, verEditText.getText().toString());
                    }
                } else {
                    Toast.makeText(this, "信息填写不完整", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码
                    //Toast.makeText(getApplicationContext(), "验证成功", Toast.LENGTH_SHORT).show();
                    //验证成功 注册入库
                    register(Constant.baseUrl+"postRegister",phonEditText.getText().toString(),passwordStr,MethodSingleton.getInstance().getDeviceModel(),MethodSingleton.getInstance().getVersionName(RegisterActivity.this),MethodSingleton.getInstance().getOSVersion());
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
                }
            } else {
                try {
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    int status = object.optInt("status");
                    if (status > 0 && !TextUtils.isEmpty(des)) {
                        Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "短信验证码接口出错", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

private void register(String url,final String n,String p,String d,String v,String o)
{
    final String mp=MD5Util.MD5(p);
    Map<String ,String> map=new HashMap<>();
    map.put("phoneNum",n);
    map.put("password",mp);
    map.put("deviceModel",d);
    map.put("appVersion",v);
    map.put("osVersion",o);
    new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
        @Override
        public void onError(Request request, Exception e) {
            Toast.makeText(RegisterActivity.this, "注册失败，请稍后再试", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(WsResponse ws) {
            if (ws.getResCode()==null){
                Toast.makeText(RegisterActivity.this, "注册失败，请稍后再试", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ws.getResCode().equals("0")){
                String userId=ws.getContent().toString();
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                //本地保存用户数据
                UserInfo info=new UserInfo(n,mp,userId);
                info.save();
                //返回
                Intent intent=getIntent();
                intent.putExtra("done",true);
                RegisterActivity.this.setResult(1,intent);
                finish();
            }else{
                Toast.makeText(RegisterActivity.this, ws.getResMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    });
}

    //sdk销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }


    //倒计时
    Handler timerHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (recLen<60) {
                recLen++;
                sensmsButton.setEnabled(false);
                sensmsButton.setText("重新获取("+(60-recLen)+")");
                timerHandler.postDelayed(this, 1000);
            }
            else
            {
                recLen=0;
                sensmsButton.setEnabled(true);
                sensmsButton.setText("获取验证码");
            }
        }
    };


}
