package com.jarvislin.producepricechecker;

import com.jarvislin.producepricechecker.model.ApiProduce;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;

/**
 * Created by Jarvis Lin on 2015/8/3.
 */
@Rest(rootUrl = "", converters = {GsonHttpMessageConverter.class})
@Accept("application/json")
public interface ApiClient extends RestClientErrorHandling {
    @Post("http://produce.jarvislin.com/json_convertor.php")
    ArrayList<ApiProduce> getData(MultiValueMap<String, String> map);
}
