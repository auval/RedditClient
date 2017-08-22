package com.mta.model;

import com.mta.model.pojo.RedditPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * retrofit definitions for code generation
 *
 * Created by amir on 8/21/17.
 */

public interface IRedditRetrofitDef {
    @GET("new.json")
    Call<RedditPojo> getNew();

    @GET("{channel}.json")
    Call<RedditPojo> getChannel(
            @Path("channel") String channel,
            @Query("limit") int limit);

    /**
     * -au/
     * This method is for pagination.
     * One note though- while scrolling down there may be a new post coming.
     * Reading the newest post is not in the scope of the test.
     *
     * @param channel
     * @param limit
     * @param after formatted like: "t3_6v9q96", which is the child's type_id
     * @return
     */
    @GET("{channel}.json")
    Call<RedditPojo> getChannel(
            @Path("channel") String channel,
            @Query("limit") int limit,
            @Query("after") String after);
}
