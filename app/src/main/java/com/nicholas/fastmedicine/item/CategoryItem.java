package com.nicholas.fastmedicine.item;

/**
 * Created by eggri_000 on 2015/10/19.
 */
public class CategoryItem {



    public CategoryItem(String content, String subtitle,String imgUrl,String price,Integer  sales,String spec)
    {
        this.title=content;
        this.subtitle=subtitle;
        this.imgUrl=imgUrl;
        this.price=price;
        this.sales=sales;
        this.spec=spec;
    }

    public CategoryItem(String content, String subtitle,String imgUrl)
    {
        this.title=content;
        this.subtitle=subtitle;
        this.imgUrl=imgUrl;
    }

    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String content) {
        this.title = content;
    }

    public String subtitle;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String content) {
        this.subtitle = content;
    }

    public String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer  sales;

    public Integer  getSales() {
        return sales;
    }


    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String spec;

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }




}
