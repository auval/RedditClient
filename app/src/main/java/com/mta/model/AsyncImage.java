package com.mta.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.mta.redditclient.R;
import com.squareup.picasso.Picasso;

/**
 * inspired by:
 * https://android.jlelse.eu/loading-images-with-data-binding-and-picasso-555dad683fdc
 *
 * This binding is used inside row_layout.xml
 *
 * Created by amir on 8/21/17.
 */

public class AsyncImage {

    @BindingAdapter({"app:srcCompat"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_iconmonstr_reddit_4)
                .into(view);
    }
}
