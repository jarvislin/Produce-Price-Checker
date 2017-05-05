package com.jarvislin.producepricechecker.network;

import com.jarvislin.producepricechecker.model.ApiProduce;
import com.jarvislin.producepricechecker.model.OpenData;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Jarvis on 2017/4/22.
 */

public interface OpenDataApi {
    @GET("FarmTransData.aspx?$top=10000&$skip=0&StartDate={startDate}&EndDate={endDate}&Crop={produceName}&Market={marketName}")
    Single<ArrayList<OpenData>> getOpenData(@Path("startDate") String startDate, @Path("endDate") String endDate, @Path("produceName") String produceName, @Path("marketName") String marketName);
}
