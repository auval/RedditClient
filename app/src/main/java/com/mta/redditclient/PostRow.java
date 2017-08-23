package com.mta.redditclient;

import android.databinding.DataBindingUtil;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mta.model.pojo.Child;
import com.mta.redditclient.databinding.RowLayoutBinding;

/**
 * (used my own past project as a reference for this class)
 * <p>
 * Created by amir on 8/21/17.
 */
public class PostRow extends LinearLayout implements View.OnClickListener {
    private static final String TAG = PostRow.class.getSimpleName();
    private final IListView listview;
    RowLayoutBinding binding;

    public PostRow(ViewGroup p, IListView listview) {
        super(p.getContext());
        this.listview = listview;

        binding = DataBindingUtil.inflate(LayoutInflater.from(p.getContext()),
                R.layout.row_layout, this, true);

    }

    public void bind(Child child) {
        binding.setChild(child);

        // todo: replace this with data binding
        setOnClickListener(this);

        binding.favStar.setOnClickListener(this);

        if (child.getMatchAt() >= 0) {
            // https://stackoverflow.com/a/2120190/1180898
            Spannable highlighted = Spannable.Factory.getInstance().newSpannable(child.getData().getTitle());
            highlighted.setSpan(new BackgroundColorSpan(0xFFFFFF00),
                    child.getMatchAt(), child.getMatchAt() + child.getLength(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            binding.title.setText(highlighted);
        } else {
            binding.title.setText(child.getData().getTitle());
        }
        setupFavButton();

    }

    private void setupFavButton() {
        // todo: can move this logic to an observable data class that uses both Child and Favorite
        if (isFav()) {
            binding.setFavicon(R.drawable.ic_star_24dp);
        } else {
            binding.setFavicon(R.drawable.ic_star_border_24dp);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == this) {
            // the entire row was clicked, open a webview
            listview.onUserClicked(binding.getChild());
        } else if (view == binding.favStar) {
            // toggle the fav on/off
            Log.i(TAG, "fav clicked");

            listview.userToggledFav(binding.getChild());
            setupFavButton();
        }
    }

    public boolean isFav() {
        return listview.isFavorite(binding.getChild());
    }
}
