package com.mta.redditclient;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mta.model.IModel;
import com.mta.model.pojo.Child;

import java.util.Collections;
import java.util.List;

/**
 *
 * has ref to context
 *
 * Created by amir on 8/21/17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements IListAdapter {
    private List<Child> data = Collections.emptyList();
    private final IListView listview;

    /**
     * Don't read again after rotation
     *
     * @param model
     */
    public MyAdapter(IModel model, IListView listview) {
        data = model.getPosts();
        this.listview = listview;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(new PostRow(parent, listview));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(data.get(position));
    }


    @Override
    public int getItemCount() {
        return /*data == null ? 0 :*/ data.size();
    }

    @Override
    public void setData(List<Child> data) {
        this.data = data;
    }

    @Override
    public Child getData(int row)  {
        return data.get(row);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getData().getId().hashCode();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final PostRow mView;

        MyViewHolder(PostRow item) {
            super(item);
            mView = item;
        }

        void bind(Child child) {
            mView.bind(child);
        }
    }

}
