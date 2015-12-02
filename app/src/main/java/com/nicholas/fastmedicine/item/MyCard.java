package com.nicholas.fastmedicine.item;

import java.util.Date;

/**
 * Created by Administrator on 2015/12/2.
 */
public class MyCard {
    private Integer cardAmount;// 卡券面值

    private Integer useConditon;// 使用条件（0：通用 1：满10使用 2：满20...）

    private String overTime;// 过期时间

    private String cardName;// 卡券名称

    private String cardDesc;// 卡券描述


    public MyCard(Integer cardAmount, Integer useConditon, String overTime, String cardName, String cardDesc) {
        this.cardAmount = cardAmount;
        this.useConditon = useConditon;
        this.overTime = overTime;
        this.cardName = cardName;
        this.cardDesc = cardDesc;
    }

    public Integer getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(Integer cardAmount) {
        this.cardAmount = cardAmount;
    }

    public Integer getUseConditon() {
        return useConditon;
    }

    public void setUseConditon(Integer useConditon) {
        this.useConditon = useConditon;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }
}
