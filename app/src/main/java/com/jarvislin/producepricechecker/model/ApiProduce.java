package com.jarvislin.producepricechecker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jarvis Lin on 2015/8/3.
 */
public class ApiProduce implements Serializable {
    private String transactionAmount;

    private String lowPrice;

    private String transactionDate;

    @SerializedName("main_category")
    private String mainCategory;

    private String produceName;

    private String middlePrice;

    private String averagePrice;

    private String marketNumber;

    private String marketName;

    private String topPrice;

    @SerializedName("sub_category")
    private String subCategory;

    private String produceNumber;

    private String result;

    private String code;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String main_category) {
        this.mainCategory = main_category;
    }

    public String getProduceName() {
        return produceName;
    }

    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    public String getMiddlePrice() {
        return middlePrice;
    }

    public void setMiddlePrice(String middlePrice) {
        this.middlePrice = middlePrice;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getMarketNumber() {
        return marketNumber;
    }

    public void setMarketNumber(String marketNumber) {
        this.marketNumber = marketNumber;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getTopPrice() {
        return topPrice;
    }

    public void setTopPrice(String topPrice) {
        this.topPrice = topPrice;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String sub_category) {
        this.subCategory = sub_category;
    }

    public String getProduceNumber() {
        return produceNumber;
    }

    public void setProduceNumber(String produceNumber) {
        this.produceNumber = produceNumber;
    }

    @Override
    public String toString() {
        return "ClassPojo [transactionAmount = " + transactionAmount + ", lowPrice = " + lowPrice + ", transactionDate = " + transactionDate + ", main_category = " + mainCategory + ", produceName = " + produceName + ", middlePrice = " + middlePrice + ", averagePrice = " + averagePrice + ", marketNumber = " + marketNumber + ", marketName = " + marketName + ", topPrice = " + topPrice + ", sub_category = " + subCategory + ", produceNumber = " + produceNumber + "]";
    }
}