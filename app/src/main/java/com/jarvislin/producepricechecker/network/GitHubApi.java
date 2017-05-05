package com.jarvislin.producepricechecker.network;

import com.jarvislin.producepricechecker.model.ApiProduce;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Jarvis on 2017/4/22.
 */

public interface GitHubApi {
    @POST("http://produce.jarvislin.com/provider")
    ArrayList<ApiProduce> getData(String param);

    @GET("{category}-{marketNumber}")
    Single<ArrayList<ApiProduce>> getDataFromGitHub(@Path("category") String category, @Path("marketNumber") String marketNumber);

    @GET("history/{category}/{marketNumber}/{year}/{date}.json")
    Single<ArrayList<ApiProduce>> getHistoryDataFromGitHub(@Path("category") String category, @Path("marketNumber") String marketNumber, @Path("year") String year, @Path("date") String date);

    @GET("history/{category}/{marketNumber}/directory.json")
    Single<ArrayList<ApiProduce>> getHistoryDirectory(@Path("category") String category, @Path("marketNumber") String marketNumber);
}
