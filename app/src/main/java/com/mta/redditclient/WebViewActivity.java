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

import com.mta.redditclient.databinding.WebViewBinding;

/**
 * Created by amir on 8/21/17.
 */
public class WebViewActivity extends Activity {

    WebViewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.web_view);

        binding.contentLoadingProgressBar.show();

        String url = getIntent().getStringExtra("url");

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


    }
}
