package com.jarvislin.producepricechecker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jarvis Lin on 2015/8/3.
 */
@Getter
@Setter
public class ApiProduce implements Serializable {

    private String transactionAmount;

    private String lowPrice;

    @SerializedName("transactionDate")
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

    @Override
    public String toString() {
        return "ClassPojo [transactionAmount = " + transactionAmount + ", lowPrice = " + lowPrice + ", transactionDate = " + transactionDate + ", main_category = " + mainCategory + ", produceName = " + produceName + ", middlePrice = " + middlePrice + ", averagePrice = " + averagePrice + ", marketNumber = " + marketNumber + ", marketName = " + marketName + ", topPrice = " + topPrice + ", sub_category = " + subCategory + ", produceNumber = " + produceNumber + "]";
    }
}