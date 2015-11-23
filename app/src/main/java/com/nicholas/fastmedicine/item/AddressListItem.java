package com.nicholas.fastmedicine.item;

/**
 * Created by Administrator on 2015/11/23.
 */
public class AddressListItem {

    private String receiver;// 收货人

    private String city;// 城市

    private String mapAdd;// 地图选择地址

    private String detailAdd;// 手动填写部分地址

    private String phoneNum;// 收货人手机号

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMapAdd() {
        return mapAdd;
    }

    public void setMapAdd(String mapAdd) {
        this.mapAdd = mapAdd;
    }

    public String getDetailAdd() {
        return detailAdd;
    }

    public void setDetailAdd(String detailAdd) {
        this.detailAdd = detailAdd;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
