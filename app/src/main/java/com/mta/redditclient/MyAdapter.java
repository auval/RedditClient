package com.mta.redditclient;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mta.model.pojo.Child;

import java.util.List;

/**
 * Created by amir on 8/21/17.
 */
public class MyAdapter extends RecyclerView.Adapter implements IListAdapter {
    private List<Child> data;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void setData(List<Child> data) {
        this.data = data;
    }
}
