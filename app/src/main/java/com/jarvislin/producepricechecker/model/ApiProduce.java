package com.jarvislin.producepricechecker.model;

import java.io.Serializable;

/**
 * Created by Jarvis Lin on 2015/8/3.
 */
public class ApiProduce implements Serializable {
    private String transactionAmount;

    private String averagePrice;

    private String marketName;

    private String marketNumber;

    private String topPrice;

    private String lowPrice;

    private String transactionDate;

    private String middlePrice;

    private String produceName;

    private String produceNumber;

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
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

    public String getProduceName() {
        return produceName;
    }

    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    public String getProduceNumber() {
        return produceNumber;
    }

    public void setProduceNumber(String produceNumber) {
        this.produceNumber = produceNumber;
    }

    @Override
    public String toString() {
        return "ClassPojo [transactionAmount = " + transactionAmount + ", averagePrice = " + averagePrice + ", marketName = " + marketName + ", marketNumber = " + marketNumber + ", topPrice = " + topPrice + ", lowPrice = " + lowPrice + ", transactionDate = " + transactionDate + ", middlePrice = " + middlePrice + ", produceName = " + produceName + ", produceNumber = " + produceNumber + "]";
    }
}