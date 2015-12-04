package com.nicholas.fastmedicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.common.Constant;

import org.litepal.crud.DataSupport;

import litepalDB.UserInfo;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("更多");
        toolbar.setNavigationIcon(R.drawable.backpng);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView about=(TextView)findViewById(R.id.about);
        about.setOnClickListener(this);

        Button exit_btn=(Button)findViewById(R.id.exit_btn);
        exit_btn.setOnClickListener(this);

        TextView clear=(TextView)findViewById(R.id.clear);
        clear.setOnClickListener(this);

        if (Constant.userId.isEmpty()){
            exit_btn.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.about:
                Intent intent=new Intent(MoreActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.exit_btn:
                if (!Constant.userId.isEmpty()){
                    Constant.userId="";
                    Constant.userNum="";
                    DataSupport.deleteAll(UserInfo.class);
                    finish();
                }
                break;
            case R.id.clear:
                new AlertDialog.Builder(MoreActivity.this).setMessage("将清理搜索记录、收藏内容，确定继续吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MoreActivity.this,"da",Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                    }
                }).create().show();
                break;
            default:
                break;
        }
    }
}
