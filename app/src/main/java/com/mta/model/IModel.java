package com.mta.model;

import com.mta.model.pojo.Child;
import com.mta.redditclient.IListPresenter;
//import com.mta.redditclient.ListPresenter;

import java.util.List;

/**
 * Created by amir on 8/21/17.
 */
public interface IModel {


    /**
     *
     * an async, non blocking method to get a list of posts ("Child" objects) in a callback
     * can be called on the main thread
     *
     * @param channel name (reddit public channels)
     * @param startIndex index for pagination
     * @param limit post count
     */
     void fetchPostsList(String channel, IListPresenter callback, int startIndex, int limit);

     List<Child> getPosts();

//    MyDb getFavDb();

    List<Child> getFavorites();

    void saveFavorite(Child c);

    void deleteFavorite(Child c);

    void loadCache();

    boolean isFavCacheLoaded();

    /**
     * callback relevant only while cache is not loaded
     *
     * @param id
     * @param callback
     * @return
     */
    boolean isFavofite(String id, IListPresenter callback);

    Child getChild(String id);

    void cacheChildForWebView(Child c);
}
