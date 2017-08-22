package com.mta.redditclient;

import com.mta.model.IModel;
import com.mta.model.fav.TypeConverters;
import com.mta.model.pojo.Child;

import java.util.List;

/**
 * Created by amir on 8/21/17.
 */

class ListPresenter implements IListPresenter {

    private static final int TAB_LIVE = 0;
    private static final int TAB_FAV = 1;
    private IListView mView; // has ref to context
    private IModel mModel;
    private IListAdapter mAdapter; // has ref to context
    private int currentTab = TAB_LIVE;

    public ListPresenter(IListView view, IModel model, IListAdapter adapter) {
        this.mView = view;
        this.mModel = model;
        mAdapter = adapter;
    }

    @Override
    public void onDataChanged() {

        if (currentTab == TAB_LIVE) {
            List<Child> posts = mModel.getPosts();

            mAdapter.setData(posts);

            mView.invalidateList(mModel);
        } else {
            List<Child> posts = mModel.getFavorites();
            mAdapter.setData(posts);

            mView.invalidateList(mModel);
        }
    }

    @Override
    public void onFetchFailure() {
        mView.showErrorMessage(R.string.failed_fetch);
    }

    @Override
    public void openUrl(Child c) {
        mView.openWebView(c);
    }

    @Override
    public void setFavorite(Child c, boolean isFav) {

        if (isFav) {
            mModel.saveFavorite(c);
        } else {
            // delete a favorite
            mModel.deleteFavorite(c);
        }
    }

    @Override
    public boolean isFavorite(Child child) {
        return mModel.isFavofite(TypeConverters.getId(child), this);
    }

    @Override
    public void showLiveChannel() {
        currentTab = TAB_LIVE;
        onDataChanged();
    }

    @Override
    public void showFavorites() {
        currentTab = TAB_FAV;
        onDataChanged();
    }

    @Override
    public boolean isInLiveTab() {
        return currentTab == TAB_LIVE;
    }


}
