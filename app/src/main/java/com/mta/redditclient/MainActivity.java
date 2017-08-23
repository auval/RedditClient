package com.mta.redditclient;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.mta.model.IModel;
import com.mta.model.RedditModel;
import com.mta.model.fav.TypeConverters;
import com.mta.model.pojo.Child;
import com.mta.redditclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements IListView, SearchView.OnQueryTextListener {

    private static final String REDDIT_CHANNEL = "top";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FETCH_LIMIT = 25;
    ActivityMainBinding binding;
    IModel mModel;
    MyAdapter mAdapter;
    IListPresenter mPresenter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mPresenter.showLiveChannel();
                    return true;
                case R.id.navigation_fav:
                    // replace the adapter with favorites
                    mPresenter.showFavorites();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMVP();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // requirement 1A
        binding.navigation.getMenu().getItem(0).setTitle(REDDIT_CHANNEL);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        binding.recyclerView.setAdapter(mAdapter);

        // requirement 2
        binding.recyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        binding.recyclerView.addOnScrollListener(new EndlessScroll(linearLayoutManager));
        //setOnScrollChangeListener(new EndlessScroll());

        if (mModel.getPosts().isEmpty()) {
            mModel.fetchPostsList(REDDIT_CHANNEL, mPresenter, 0, FETCH_LIMIT);
        }

        // requirement 4
        // looked at reference: https://stackoverflow.com/a/30429439/1180898
        // but my solution is simpler
        binding.searchView.setOnQueryTextListener(this);

    }

    private void initMVP() {
        mModel = new RedditModel(getApplicationContext()); // M
        mAdapter = new MyAdapter(mModel, this); // (+ this activity) V
        mPresenter = new ListPresenter(this, mModel, mAdapter); // P
    }

    @Override
    public void showErrorMessage(int res) {
        Toast.makeText(this, res, Toast.LENGTH_LONG).show();
    }

    @Override
    public void invalidateList(IModel model) {
        binding.recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onUserClicked(Child child) {
        mPresenter.openUrl(child);
    }

    @Override
    public void userToggledFav(Child child) {
        mPresenter.setFavorite(child, !mPresenter.isFavorite(child));
    }

    @Override
    public boolean isFavorite(Child child) {
        return mPresenter.isFavorite(child);
    }

    @Override
    public void openWebView(Child c) { //String url, String id) {
        Intent i = new Intent(this, WebViewActivity.class);

        i.putExtra("url", c.getData().getUrl());
        i.putExtra("id", TypeConverters.getId(c));
        mModel.cacheChildForWebView(c);

        startActivity(i);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mAdapter.filterOn(s);
        return true;
    }

    // used code from here:
    // https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
    class EndlessScroll extends EndlessRecyclerViewScrollListener {

        public EndlessScroll(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            Log.i(TAG, "load more: p" + page + " tot=" + totalItemsCount);
            if (mPresenter.isInLiveTab()) {
                // todo: pipe this call via the Presenter, so the functionality could be unit tested:
                mModel.fetchPostsList(REDDIT_CHANNEL, mPresenter, totalItemsCount, FETCH_LIMIT);
            }
        }
    }
}
