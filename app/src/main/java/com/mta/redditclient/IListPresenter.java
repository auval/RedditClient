package com.mta.redditclient;

import android.support.v7.widget.RecyclerView;

import com.mta.model.pojo.Child;

/**
 * Created by amir on 8/21/17.
 */

public interface IListPresenter {

     /**
      * a signal for the presenter to get the current data from the model
      * and invalidate the view with it
      */
     void onDataChanged();

     /**
      * when there is a problem getting the data
      */
     void onFetchFailure();

     /**
      * user clicked on a row
      * @param i
      */
     void openUrl(Child c);

//     RecyclerView.Adapter getListAdapter();
}
