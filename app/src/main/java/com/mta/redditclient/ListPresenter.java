package com.mta.redditclient;

import com.mta.model.IModel;

/**
 * Created by amir on 8/21/17.
 */

class ListPresenter implements IListPresenter {

    private IListView view;
    private IModel model;

    public ListPresenter(IListView view, IModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onFetchFailure() {
        view.showErrorMessage(R.string.failed_fetch);
    }
}
