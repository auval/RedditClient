package com.mta.redditclient;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mta.model.IModel;
import com.mta.model.RetrofitClient;
import com.mta.model.pojo.Child;
import com.mta.redditclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements IListView {

    private static final String REDDIT_CHANNEL = "top";

    private static final String TAG = MainActivity.class.getSimpleName();
    // the mModel can be mocked for testing purpose
    static IModel mModel = new RetrofitClient();
    ActivityMainBinding binding;
    MyAdapter mAdapter = new MyAdapter(mModel, this);
    IListPresenter mPresenter = new ListPresenter(this, mModel, mAdapter);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    binding.message.setText(REDDIT_CHANNEL);
                    return true;
                case R.id.navigation_fav:
                    binding.message.setText(R.string.title_favirotes);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // requirement 1A
        binding.navigation.getMenu().getItem(0).setTitle(REDDIT_CHANNEL);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.recyclerView.setAdapter(mAdapter);
    }

    /**
     * temporary method to work on the retrofit part only.
     * Called when the title is clicked
     *
     * @param view
     */
    public void onLoadClicked(View view) {
        mModel.fetchPostsList(REDDIT_CHANNEL, mPresenter, 0, 25);


    }

    @Override
    public void showErrorMessage(int res) {
        Toast.makeText(this, res, Toast.LENGTH_LONG).show();
    }

    @Override
    public void invalidateList(IModel model) {
        binding.recyclerView.getAdapter().notifyDataSetChanged();
        //invalidate();
    }

    @Override
    public void onUserClicked(Child child) {
        mPresenter.openUrl(child);
    }

    @Override
    public void openWebView(String url, String id) {
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra("url", url);
        i.putExtra("id", id);
        startActivity(i);
    }
}
