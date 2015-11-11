package com.nicholas.fastmedicine.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * Created by eggri_000 on 2015/10/14.
 */
public  class Method {

    /**
     * 手机号校验
     */
    public static boolean isMobileNum(String mobiles) {
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
    public static boolean isNetAvailable(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }
}
