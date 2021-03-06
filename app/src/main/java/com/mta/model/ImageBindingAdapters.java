package com.mta.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.mta.redditclient.R;
import com.squareup.picasso.Picasso;

/**
 * data binding converters
 *
 * inspired by:
 * https://android.jlelse.eu/loading-images-with-data-binding-and-picasso-555dad683fdc
 *
 * This binding is used inside row_layout.xml
 *
 * Created by amir on 8/21/17.
 */

public class ImageBindingAdapters {

    /**
     * used for the thumbnail
     * @param view
     * @param imageUrl
     */
    @BindingAdapter({"srcCompat"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_iconmonstr_reddit_4)
                .into(view);
    }

    /**
     * used for the favorite icon
     * @param view
     * @param res
     */
    @BindingAdapter({"srcCompat"})
    public static void loadImage(ImageView view, int res) {
        view.setImageResource(res);
    }



}
