package com.mta.redditclient;

import com.mta.model.IModel;
import com.mta.model.pojo.Child;

/**
 * Created by amir on 8/21/17.
 */

public interface IListView {
    /**
     * from presenter to the view
     */
    void openWebView(Child c);

    /**
     * from presenter to the view
     */
    void showErrorMessage(int res);

    /**
     * from presenter to the view
     */
    void invalidateList(IModel mockModel);

    /**
     * inbound, from view to the presenter
     */
    void onUserClicked(Child child);

    /**
     * switches on/off from current state
     * @param child
     */
    void userToggledFav(Child child);

    /**
     * tests current state
     * @param child
     * @return
     */
    boolean isFavorite(Child child);
}
