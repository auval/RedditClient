package com.mta.model;

import com.mta.model.pojo.Data;
import com.mta.model.pojo.RedditPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by amir on 8/21/17.
 */

public interface RedditList {
    @GET("new.json")
    Call<RedditPojo> getNew();
}
