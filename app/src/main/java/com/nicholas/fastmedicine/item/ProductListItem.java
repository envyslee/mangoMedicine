package com.nicholas.fastmedicine.item;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2015/11/16.
 */
public class ProductListItem
{

    private Long id;


    private String iconUrl;// 图片


    private String productName;// 名称


    private String productDesc;// 描述


    private Double productId;

    private String productSpec;// 商品规格

    private Double productPrice;// 商品价格

    private Double productSale;// 商品销量

    private Double pharmacyId;// 药店id

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

    public Double getProductSale()
    {
        return productSale;
    }

    public void setProductSale(Double productSale)
    {
        this.productSale = productSale;
    }

}
