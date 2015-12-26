package com.jarvislin.producepricechecker;

import com.jarvislin.producepricechecker.model.ApiProduce;
import com.jarvislin.producepricechecker.model.HistoryDirectory;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;

/**
 * Created by Jarvis Lin on 2015/8/3.
 */
@Rest(converters = {FormHttpMessageConverter.class, GsonHttpMessageConverter.class, StringHttpMessageConverter.class})
@Accept("application/json")
public interface ApiClient extends RestClientErrorHandling {
    @Post("http://produce.jarvislin.com/provider")
    ArrayList<ApiProduce> getData(MultiValueMap<String, String> map);

    @Get("https://raw.githubusercontent.com/jarvislin/Produce-Price-Data/master/{category}-{marketNumber}")
    String getDataFromGitHub(String category, String marketNumber);

    @Get("https://raw.githubusercontent.com/jarvislin/Produce-Price-Data/master/history/{category}/{marketNumber}/{year}/{date}.json")
    String getHistoryDataFromGitHub(String category, String marketNumber, String year, String date);

    @Get("https://raw.githubusercontent.com/jarvislin/Produce-Price-Data/master/history/{category}/{marketNumber}/directory.json")
    String getHistoryDirectoryFromGitHub(String category, String marketNumber);

    @Get("http://m.coa.gov.tw/OpenData/FarmTransData.aspx?$top=10000&$skip=0&StartDate={startDate}&EndDate={endDate}&Crop={produceName}&Market={marketName}")
    String getOpenData(String startDate, String endDate, String produceName, String marketName);
}
