package com.mta.redditclient;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mta.model.pojo.Child;
import com.mta.redditclient.databinding.RowLayoutBinding;

/**
 * (used my own past project as a reference for this class)
 * <p>
 * Created by amir on 8/21/17.
 */
public class PostRow extends LinearLayout implements View.OnClickListener {
    private static final String TAG = PostRow.class.getSimpleName();
    private final IListView listview;
    RowLayoutBinding binding;

    public PostRow(ViewGroup p, IListView listview) {
        super(p.getContext());
        this.listview = listview;
        LayoutInflater layoutInflater = (LayoutInflater) p.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_layout, p, false);
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_layout, this, true);

    }

    public void bind(Child child) {
        Log.i(TAG, child.getData().getId() + ": " + child.getData().getTitle());
        binding.setChild(child);

        // todo: replace this with data binding
        setOnClickListener(this);
    }

//    public void onClicked(View view) {
//        Log.i(TAG,"got click on "+view);
//    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "got click on " + binding.getChild().getData().getTitle());
        listview.onUserClicked(binding.getChild());
                //openWebView();
                //openUrl(binding.getChild());
    }
}
