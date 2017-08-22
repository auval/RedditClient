package com.mta.redditclient;

import android.os.Bundle;
import android.util.Log;

import com.mta.model.IModel;
import com.mta.model.fav.ChildFavTypeConverter;
import com.mta.model.fav.MyDb;
import com.mta.model.pojo.Child;
import com.mta.util.Functify;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by amir on 8/21/17.
 */

class ListPresenter implements IListPresenter {

    boolean loadingCache = false;
    private IListView view; // has ref to context
    private IModel model;
    private IListAdapter mAdapter; // has ref to context

    public ListPresenter(IListView view, IModel model, IListAdapter adapter) {
        this.view = view;
        this.model = model;
        mAdapter = adapter;
    }

    @Override
    public void onDataChanged() {

        List<Child> posts = model.getPosts();

        mAdapter.setData(posts);

        view.invalidateList(model);
    }

    @Override
    public void onFetchFailure() {
        view.showErrorMessage(R.string.failed_fetch);
    }

    @Override
    public void openUrl(Child c) {
        view.openWebView(c.getData().getUrl(), c.getData().getId());
    }

    @Override
    public void setFavorite(Child c, boolean fav) {

        MyDb instance = model.getFavDb();
        if (fav) {
            instance.saveFavorite(c);
        } else {
            // delete a favorite
            instance.deleteFavorite(c);
        }
    }

    @Override
    public boolean isFavorite(Child child) {
        final MyDb instance = model.getFavDb();

        if (instance.isCacheLoaded()) {
            return instance.isFavofite(ChildFavTypeConverter.getId(child));

        } else {

            loadCache(instance);

            // for now return false, wait until the fav list is ready, and a callback will be sent
            return false;
        }
    }

    /**
     * here I'm using a utility class I've written in the past.
     * It's allowing to setup a series of operations to be performed one after the other,
     * each on its designated thread, user or main.
     * <p>
     * (and released open source https://github.com/auval/Functify)
     *
     * @param instance
     */
    private void loadCache(final MyDb instance) {
        if (loadingCache) {
            // allow to load the cache only once,
            return;
        }
        loadingCache = true;

        Log.i(TAG, "loading cache...");
        Functify.FuncFlow fb = Functify.newFlow();
        fb.setExceptionHandler(new Functify.FExceptionHandler() {
            @Override
            public void onFException(Throwable e) {
                e.printStackTrace();
            }
        });
        fb.runAsync(new Functify.Func() {
            @Override
            public void onExecute(Bundle b) {
                instance.loadCache();
            }
        }).runOnMain(new Functify.Func() {
            @Override
            public void onExecute(Bundle b) {
                Log.i(TAG, "cache is loaded");
                onDataChanged();

            }
        }).execute();

    }


}
