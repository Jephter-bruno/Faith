package com.glamour.faith.adapters;

import android.widget.ImageView;

import com.glamour.faith.models.Movie;

public interface MovieItemClickListener {

    void onMovieClick(Movie movie, ImageView movieImageView); // we will need the imageview to make the shared animation between the two activity

}
