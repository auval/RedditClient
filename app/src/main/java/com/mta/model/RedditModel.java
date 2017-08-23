package com.mta.model;

import android.content.Context;
import android.util.Log;

import com.mta.model.fav.MyDb;
import com.mta.model.fav.TypeConverters;
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

//import com.mta.redditclient.ListPresenter;

/**
 * ref: http://square.github.io/retrofit/
 * <p>
 * Created by amir on 8/21/17.
 */

public class RedditModel implements IModel {

    private static final String TAG = RedditModel.class.getSimpleName();
    private static final String REDDIT_BASE_URL = "https://www.reddit.com/r/all/";

    // a mutable list to cache the most recent response from the server
    // it's static so it will survive configuration change, and prevent extra network calls
    // a Child is a plain Pojo, so no danger for memory leak
    private static List<Child> sPostsList = new ArrayList<>();

    // private static doesn't harm testability, since access is via methods in the interface,
    // and they can be mocked.
    private static Child sCurrentlyWebViewd = null;

    private static Retrofit retrofit = null;
    private Context appContext;

    /**
     * appContext is stored, so no memory leak here even by accident
     *
     * @param context
     */
    public RedditModel(Context context) {
        this.appContext = context.getApplicationContext();
    }


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

        Retrofit client = RedditModel.getClient(REDDIT_BASE_URL);

        IRedditRetrofitDef reddit = client.create(IRedditRetrofitDef.class);

        Call<RedditPojo> data;
        if (startIndex == 0) {
            data = reddit.getChannel(channel, limit);
        } else {
            Child child = sPostsList.get(sPostsList.size() - 1);
            String after = child.getKind() + "_" + child.getData().getId();
            data = reddit.getChannel(channel, limit, after);
        }

        // async call, so we can call it on the main thread
        data.enqueue(new Callback<RedditPojo>() {
            @Override
            public void onResponse(Call<RedditPojo> call, Response<RedditPojo> response) {

                RedditPojo body = response.body();
                if (body == null) {
                    callback.onFetchFailure();
                    Log.e(TAG, "" + response);
                    return;
                }
                Data data = body.getData();

                List<Child> children = data.getChildren();
                if (startIndex == 0) {
                    // we're probably not paginating, so remove the old data
                    sPostsList.clear();
                }

                sPostsList.addAll(children);

                callback.onDataChanged();
            }

            @Override
            public void onFailure(Call<RedditPojo> call, Throwable t) {
                Log.e(TAG, "failed", t);
                sPostsList.clear();
                callback.onFetchFailure();

            }
        });
    }

    @Override
    public List<Child> getPosts() {
        return sPostsList;
    }

    @Override
    public List<Child> getFavorites() {
        return MyDb.getInstance(appContext).getFavorites();
    }

    @Override
    public void saveFavorite(Child c) {
        MyDb.getInstance(appContext).saveFavorite(c);
    }

    @Override
    public void deleteFavorite(Child c) {
        MyDb.getInstance(appContext).deleteFavorite(c);
    }

    @Override
    public void loadCache() {
        MyDb.getInstance(appContext).loadCacheBlocking();
    }

    @Override
    public boolean isFavCacheLoaded() {
        return MyDb.getInstance(appContext).isCacheLoaded();
    }

    @Override
    public boolean isFavofite(String id, IListPresenter callback) {
        return MyDb.getInstance(appContext).isFavorite(id, callback);
    }

    @Override
    public Child getChild(String id) {
        if (sCurrentlyWebViewd != null && TypeConverters.getId(sCurrentlyWebViewd).equals(id)) {
            return sCurrentlyWebViewd;
        }
        // we're not supposed to reach here.
        // If it does happen:
        // I can search for the child inside the cached list (but it'll be inefficient)
        // It could also be cached as Favorite
        return null;
    }

    @Override
    public void cacheChildForWebView(Child c) {
        sCurrentlyWebViewd = c;
    }


}
