package com.nicholas.fastmedicine;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.nicholas.fastmedicine.common.Constant;

import org.litepal.crud.DataSupport;

import litepalDB.UserInfo;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UserInfo info= DataSupport.find(UserInfo.class, 1);
        if (info!=null){
            Constant.userId=info.getU_i();
            Constant.userNum=info.getU_n();
        }


       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        },1000);

    }
}
