package com.mta.model;

import android.util.Log;

import com.mta.model.pojo.Child;
import com.mta.model.pojo.Data;
import com.mta.model.pojo.RedditPojo;
import com.mta.redditclient.IListPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ref: http://square.github.io/retrofit/
 * <p>
 * Created by amir on 8/21/17.
 */

public class RetrofitClient implements IModel {

    private static final String TAG = RetrofitClient.class.getSimpleName();
    private static final String REDDIT_BASE_URL = "https://www.reddit.com/r/all/";
    private static Retrofit retrofit = null;

    // a mutable list to hold the most recent response from the server
    List<Child> mPostsList = new ArrayList<>();

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    @Override
    public void fetchPostsList(String channel,
                               final IListPresenter callback,
                               final int startIndex,
                               int limit) {

        Retrofit client = RetrofitClient.getClient(REDDIT_BASE_URL);

        RedditList reddit = client.create(RedditList.class);

        Call<RedditPojo> data = reddit.getChannel(channel, 10);

        // async call, so we can call it on the main thread
        data.enqueue(new Callback<RedditPojo>() {
            @Override
            public void onResponse(Call<RedditPojo> call, Response<RedditPojo> response) {

                RedditPojo body = response.body();
                Data data = body.getData();

                List<Child> children = data.getChildren();
                if (startIndex==0) {
                    // we're probably not paginating, so remove the old data
                    mPostsList.clear();
                }
//                for (Child c : children) {
//                    Log.i(TAG, "child:" + c);
//                    mPostsList.add(c);
//                }

                mPostsList.addAll(children);

                callback.onDataChanged();
            }

            @Override
            public void onFailure(Call<RedditPojo> call, Throwable t) {
                Log.e(TAG, "failed", t);
                mPostsList.clear();
                callback.onFetchFailure();

            }
        });
    }

    @Override
    public List<Child> getPosts() {
        return null;
    }

}
