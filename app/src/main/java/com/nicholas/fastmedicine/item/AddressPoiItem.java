package com.nicholas.fastmedicine.item;

/**
 * Created by eggri_000 on 2015/11/4.
 */
public class AddressPoiItem {


    private String poiName;
    private String poiAddress;

    public AddressPoiItem(String name,String address)
    {
        this.poiAddress=address;
        this.poiName=name;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String name) {
        this.poiName = name;
    }

    public String getPoiAddress() {
        return poiAddress;
    }

    public void setPoiAddress(String address) {
        this.poiAddress = address;
    }
}
