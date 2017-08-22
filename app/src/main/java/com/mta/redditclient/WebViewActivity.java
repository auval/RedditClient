package com.mta.redditclient;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mta.model.IModel;
import com.mta.model.RedditModel;
import com.mta.model.pojo.Child;
import com.mta.redditclient.databinding.WebViewBinding;

/**
 * Created by amir on 8/21/17.
 */
public class WebViewActivity extends Activity implements View.OnClickListener {

    WebViewBinding binding;
    IModel mModel;
    //    IListPresenter mPresenter;
    String id;
    boolean favofite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.web_view);

        binding.contentLoadingProgressBar.show();

        String url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");


        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(url);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                // TODO: show error message to the user and return to the main list
                // or ask if to try and open in in an external browser

            }

        });
        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                binding.contentLoadingProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    binding.contentLoadingProgressBar.hide();
                    binding.webView.setVisibility(View.VISIBLE);
                }
            }


        });

        binding.homeButton.setOnClickListener(this);
        binding.starButton.setOnClickListener(this);

        initModel();

    }

    private void initModel() {
        mModel = new RedditModel(this);
        favofite = mModel.isFavofite(id, null);

        setupFavButton();
    }

    private void setupFavButton() {
        // todo: can move this logic to an observable data class that uses both Child and Favorite
        if (favofite) {
            binding.starButton.setImageResource(R.drawable.ic_star_24dp);
        } else {
            binding.starButton.setImageResource(R.drawable.ic_star_border_24dp);
        }
    }

    @Override
    public void onClick(View view) {

        if (view == binding.homeButton) {
            finish();
        } else if (view == binding.starButton) {
            // toggle
            favofite = !favofite;
            Child c = mModel.getChild(id);

            if (c==null) {
                // error occurred.
                // (production app should report this for fixing)
                binding.starButton.setVisibility(View.GONE);
                return;
            }

            if (favofite) {
                mModel.saveFavorite(c);
            } else {
                mModel.deleteFavorite(c);
            }
            setupFavButton();
        }
    }
}
