package com.nicholas.fastmedicine.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

/**
 * Created by eggri_000 on 2015/10/14.
 */
public  class MethodSingleton {

    private MethodSingleton(){}

    private static MethodSingleton instance=null;

    public  synchronized static MethodSingleton getInstance(){
        if (instance==null){
            instance=new MethodSingleton();
        }
        return instance;
    }


    /**
     * 手机号校验
     */
    public    boolean isMobileNum(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 网络检查
     * @return
     */
    public  boolean isNetAvailable(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    /**
     * 获取版本号
     * @return
     */
    public String getVersionName(Context context){
        PackageManager manager=context.getPackageManager();
        try {
            PackageInfo info=manager.getPackageInfo("com.nicholas.fastmedicine",0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "null version";
        }
    }


    /**
     * 获取设备名称
     * @return
     */
    public String getDeviceModel(){
        return Build.MODEL;
    }

    /**
     * 获取os版本
     * @return
     */
    public String getOSVersion(){
        return Build.VERSION.RELEASE;
    }


}
