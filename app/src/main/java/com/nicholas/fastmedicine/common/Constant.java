package com.nicholas.fastmedicine.common;

/**
 * Created by eggri_000 on 2015/10/15.
 */
public class Constant {

    //短信验证
    public final static String mobAppkey="b16814218218";
    public final static String mobAppsecret="8a6bf44eda5c81380dd77ca7292911d6";

    //百度地图
    public final static String baiduMapAKDebug="YiBuHIZMEYakZajUsFn61kg3";
    public final static String baiduMapAKRelease="AaqFAGSpGEAegpsLNpdQ3uZT";

    //摇一摇
    public static final int SENSOR_SHAKE = 10;

    //获取到的城市
    public static String cityName="";
    public static Double latitude;
    public static Double lontitude;
    public static float radius;

    //服务端url
    public static String baseUrl="http://10.151.11.103:8080/fastMedicine/medicine/";

    public static String dataError="获取数据出错";
}
