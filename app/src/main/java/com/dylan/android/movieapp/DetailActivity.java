package com.dylan.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Dyl on 23/05/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private TextView mMovieTitle;
    private TextView mMovieSynopsis;
    private TextView mMovieRating;
    private TextView mMovieRelease;
    private ImageView mMoviePoster;
    private ImageView mMovieBackdrop;
    private String[] mTitleText;

    private String BasePosterAddress= "http://image.tmdb.org/t/p/w500/";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*Define all of the view references used in the detailActivity    */
        mMovieTitle =  findViewById(R.id.tv_movie_title);
        mMovieSynopsis =  findViewById(R.id.tv_synopsis);
        mMovieRating =  findViewById(R.id.tv_user_rating);
        mMovieRelease =  findViewById(R.id.tv_release_date);
        mMoviePoster =  findViewById(R.id.iv_poster);
        mMovieBackdrop =  findViewById(R.id.iv_backdrop);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movieInfo")) {
                mTitleText = intentThatStartedThisActivity.getStringArrayExtra("movieInfo");
                mMovieTitle.setText(mTitleText[0]);
                mMovieSynopsis.setText(mTitleText[2]);
                String releaseYear = mTitleText[4];

                mMovieRelease.setText(releaseYear.substring(0,4));
                String movieRating = mTitleText[3] + getString(R.string.movie_rate_denom);
                mMovieRating.setText(movieRating);
                Context context = mMoviePoster.getContext();
                Picasso.with(context).load(BasePosterAddress + mTitleText[1]).into(mMoviePoster);
                Picasso.with(context).load(BasePosterAddress + mTitleText[5]).into(mMovieBackdrop);
            }
        }
    }
}
