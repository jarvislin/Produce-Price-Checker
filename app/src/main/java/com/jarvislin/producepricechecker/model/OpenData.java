package com.jarvislin.producepricechecker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jarvis on 15/8/26.
 */
public class OpenData implements Serializable, Comparable<OpenData> {
    @SerializedName("交易量")
    private String transactionAmount;

    @SerializedName("下價")
    private String lowPrice;

    @SerializedName("交易日期")
    private String transactionDate;

    @SerializedName("中價")
    private String middlePrice;

    @SerializedName("市場代號")
    private String marketNumber;

    @SerializedName("上價")
    private String topPrice;

    @SerializedName("平均價")
    private String averagePrice;

    @SerializedName("作物代號")
    private String produceNumber;

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMiddlePrice() {
        return middlePrice;
    }

    public void setMiddlePrice(String middlePrice) {
        this.middlePrice = middlePrice;
    }

    public String getMarketNumber() {
        return marketNumber;
    }

    public void setMarketNumber(String marketNumber) {
        this.marketNumber = marketNumber;
    }

    public String getTopPrice() {
        return topPrice;
    }

    public void setTopPrice(String topPrice) {
        this.topPrice = topPrice;
    }


    public String getProduceNumber() {
        return produceNumber;
    }

    public void setProduceNumber(String produceNumber) {
        this.produceNumber = produceNumber;
    }

    @Override
    public String toString() {
        return "ClassPojo [transactionAmount = " + transactionAmount + ", lowPrice = " + lowPrice + ", transactionDate = " + transactionDate + ", middlePrice = " + middlePrice + ", marketNumber = " + marketNumber + ", topPrice = " + topPrice + ", produceNumber = " + produceNumber + "]";

    }

    @Override
    public int compareTo(OpenData data) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.getTransactionDate(), data.getTransactionDate());
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }
}
