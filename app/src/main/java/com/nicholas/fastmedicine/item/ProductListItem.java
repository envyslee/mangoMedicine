package com.nicholas.fastmedicine.item;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2015/11/16.
 */
public class ProductListItem implements Serializable
{

    private Long id;


    private String iconUrl;// 图片


    private String productName;// 名称


    private String productDesc;// 描述


    private Double productId;

    private String productSpec;// 商品规格

    private Double productPrice;// 商品价格

    private Integer productSale;// 商品销量

    private Double pharmacyId;// 药店id

    private String pharmacyName;//药店名称

    private boolean isChecked=true;//是否选中

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    private Integer count=1;//商品数量

    private Double maxCount;//库存

    private Double priceId;//对应药店商品id
    public Double getPriceId() {
        return priceId;
    }

    public void setPriceId(Double priceId) {
        this.priceId = priceId;
    }
    public Double getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Double maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public Double getPharmacyId()
    {
        return pharmacyId;
    }

    public void setPharmacyId(Double pharmacyId)
    {
        this.pharmacyId = pharmacyId;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getIconUrl()
    {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl)
    {
        this.iconUrl = iconUrl;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductDesc()
    {
        return productDesc;
    }

    public void setProductDesc(String productDesc)
    {
        this.productDesc = productDesc;
    }

    public Double getProductId()
    {
        return productId;
    }

    public void setProductId(Double productId)
    {
        this.productId = productId;
    }

    public String getProductSpec()
    {
        return productSpec;
    }

    public void setProductSpec(String productSpec)
    {
        this.productSpec = productSpec;
    }

    public Double getProductPrice()
    {
        return productPrice;
    }

    public void setProductPrice(Double productPrice)
    {
        this.productPrice = productPrice;
    }

    public Integer getProductSale()
    {
        return productSale;
    }

    public void setProductSale(Integer productSale)
    {
        this.productSale = productSale;
    }

}
