package com.mta.redditclient;

import android.content.Context;

import com.mta.model.IModel;
import com.mta.model.pojo.Child;

/**
 * Created by amir on 8/21/17.
 */

public interface IListView {
    // from presenter to the view
    void openWebView(String url, String id);

    // from presenter to the view
    void showErrorMessage(int res);

    // from presenter to the view
    void invalidateList(IModel mockModel);

    // inbound, from view to the presenter
    void onUserClicked(Child child);


    void userToggledFav(Child child);

    boolean isFavorite(Child child);
}
