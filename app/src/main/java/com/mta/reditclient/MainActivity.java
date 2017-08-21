package com.mta.reditclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mta.model.RedditList;
import com.mta.model.RetrofitClient;
import com.mta.model.pojo.Child;
import com.mta.model.pojo.Data;
import com.mta.model.pojo.RedditPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    // I should append: "new.json" and also pagination params
    private static final String REDDIT_BASE_URL = "https://www.reddit.com/r/all/";

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mTextMessage;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * temporary method to work on the retrofit part only
     *
     * @param view
     */
    public void onLoad(View view) {
        Retrofit client = RetrofitClient.getClient(REDDIT_BASE_URL);

        //     RedditPojo reddit = client.create(RedditPojo.class);
        RedditList reddit = client.create(RedditList.class);


        Call<RedditPojo> data = reddit.getNew();

        // async call, so we can call it on the main thread
        data.enqueue(new Callback<RedditPojo>() {
            @Override
            public void onResponse(Call<RedditPojo> call, Response<RedditPojo> response) {
                Log.d(TAG, "" + response);
                RedditPojo body = response.body();
                Data data = body.getData();
                List<Child> children = data.getChildren();
                for (Child c : children) {
                    Log.i(TAG, "child:" + c);
                }

            }

            @Override
            public void onFailure(Call<RedditPojo> call, Throwable t) {
                Log.e(TAG, "failed", t);

            }
        });

    }

}
