package com.mta.redditclient;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mta.model.IModel;
import com.mta.model.pojo.Child;

import java.util.ArrayList;
import java.util.List;

/**
 * has ref to context
 * <p>
 * Created by amir on 8/21/17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements IListAdapter {
    /**
     * mutable list
     * represents the actual visible rows (the result of the filter)
     */
    final private List<Child> data = new ArrayList<>();

    private final IListView listview;
    /**
     * reference to the data without any filtering
     * when a filter is called, this is the base list to filter from
     */
    private List<Child> dataCopy;
    private String currentFilter = null;

    /**
     * Don't read again after rotation
     *
     * @param model
     */
    public MyAdapter(IModel model, IListView listview) {
        this.listview = listview;
        setData(model.getPosts());
        // allows add/remove animation
        setHasStableIds(true);
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
    public void setData(List<Child> d) {
        dataCopy = d;
        data.clear();

        data.addAll(d);
        if (currentFilter != null) {
            filterOn(currentFilter);
        }
    }

    @Override
    public Child getData(int row) {
        return data.get(row);
    }

    @Override
    public void filterOn(String s) {
        // the requirements limit the filter to length of 3 or more
        if (s.length() < 3) {
            if (currentFilter != null) {
                // it means we have an active filter, we need to clear it
                data.clear();
                for (Child c : dataCopy) {
                    c.setMatchAt(-1, 0);
                    data.add(c);
                }
                currentFilter = null;
                notifyDataSetChanged();
            }
        } else {
            currentFilter = s;
            data.clear();
            String slc = s.toLowerCase();
            for (Child c : dataCopy) {
                if (match(c, slc) >= 0) {
                    data.add(c);
                }
            }
            notifyDataSetChanged();
        }

    }

    /**
     * @param c
     * @param s
     * @return -1 if filter not found, or greater if found
     */
    private int match(Child c, String s) {
        // lower casing every time is not efficient, so I make this once per child
        String title = c.getLowercaseTitle();
        int indexOfMatch = title.indexOf(s);
        c.setMatchAt(indexOfMatch, s.length());
        return indexOfMatch;
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
