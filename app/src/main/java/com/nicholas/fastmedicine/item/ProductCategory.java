package com.nicholas.fastmedicine.item;

/**
 * Created by Administrator on 2015/11/16.
 */
public class ProductCategory {
    public Double getId()
    {
        return id;
    }

    public void setId(Double id)
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

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc()
    {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc)
    {
        this.categoryDesc = categoryDesc;
    }

    private Double id;// 自增序列号


    private String iconUrl;// 图片

    private String categoryName;// 分类名称


    private String categoryDesc;// 分类描述
}
