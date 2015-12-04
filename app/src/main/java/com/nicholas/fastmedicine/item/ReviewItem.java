package com.nicholas.fastmedicine.item;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ReviewItem implements Serializable {

    private Integer id;// 自增序列号

    private Integer userId;// 用户id

    private Integer priceId;//对应商店药品id

    private String content;// 评论内容

    private String createdTime;// 创建时间

    private String userName;// 评论者名称，目前为手机号


    public ReviewItem(Integer id,  String content, String createdTime,String userName) {
        this.id = id;
        this.content = content;
        this.createdTime = createdTime;
        this.userName=userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
