package com.jarvislin.producepricechecker.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jarvislin.producepricechecker.BuildConfig;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jarvis on 2017/4/23.
 */

public class NetworkService {

    private static final String GIT_HUB_REPO_URL = "https://raw.githubusercontent.com/jarvislin/Produce-Price-Data/master/";
    private static final String OPEN_DATA_URL = "http://m.coa.gov.tw/OpenData/";
    private GitHubApi gitHubApi;
    private OpenDataApi openDataApi;
    private Context context;

    public NetworkService(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        // init cache
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        // init client
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG
                        ? HttpLoggingInterceptor.Level.BODY
                        : HttpLoggingInterceptor.Level.NONE))
                .build();

        // init retrofit
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new ToStringConverterFactory())
                .client(client);

        gitHubApi = retrofitBuilder.baseUrl(GIT_HUB_REPO_URL).build().create(GitHubApi.class);
        openDataApi = retrofitBuilder.baseUrl(OPEN_DATA_URL).build().create(OpenDataApi.class);
    }

    @NonNull
    public OpenDataApi getOpenDataApi() {
        return openDataApi;
    }

    @NonNull
    public GitHubApi getGitHubApi() {
        return gitHubApi;
    }

    private final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (ToolsHelper.isNetworkAvailable(context)) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };
}
