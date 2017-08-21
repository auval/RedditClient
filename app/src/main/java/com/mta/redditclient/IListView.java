package com.mta.redditclient;

import com.mta.model.IModel;

/**
 * Created by amir on 8/21/17.
 */

public interface IListView {
    void showErrorMessage(int res);

    void invalidateList(IModel mockModel);
}
