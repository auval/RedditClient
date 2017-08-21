package com.mta.redditclient;

import com.mta.model.IModel;
import com.mta.model.pojo.Child;

import java.util.List;

/**
 * Created by amir on 8/21/17.
 */

class ListPresenter implements IListPresenter {


    private IListView view;
    private IModel model;
    private IListAdapter mAdapter;

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


}
