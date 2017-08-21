package com.mta.model;

import com.mta.model.pojo.RedditPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by amir on 8/21/17.
 */

public interface RedditList {
    @GET("new.json")
    Call<RedditPojo> getNew();

    @GET("{channel}.json")
    Call<RedditPojo> getChannel(
            @Path("channel") String channel,
            @Query("limit") int limit);
}
