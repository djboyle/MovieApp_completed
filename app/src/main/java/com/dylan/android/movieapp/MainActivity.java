package com.dylan.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dylan.android.movieapp.utilities.NetworkUtils;
import com.dylan.android.movieapp.utilities.OpenMovieJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ImageAdapterOnClickHandler {


    private RecyclerView mRecyclerView;
    private  ImageAdapter mImageAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    public String sortType;

    public final String Base_API_Query = "https://api.themoviedb.org/3/movie/";
    //Please place API key here after the equals sign
    public final String API_Key = "api_key=";

    public String movieDataArray[][]= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = findViewById(R.id.rv_grid);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
/*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        /*
         * GridLayoutManager is defined with 2 columns. .
         */
        int numberOfColumns = 2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        mRecyclerView.setHasFixedSize(true);
        /*
         * The ForecastAdapter is responsible for linking our movie data with the Views that
         * will end up displaying the poster images.
         */
        mImageAdapter = new ImageAdapter(this);
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mImageAdapter);
        /* Setting the default search type to sort by popular movies. */
        if (sortType == null){
            sortType = "popular?";
        }

        /* Define the API search query to get the movie JSON data. */
        String API_Query = Base_API_Query + sortType+ API_Key;

        /* This will call the backgorund method to fetch the movie JSON data */
        if (isOnline()) {
            showMovieDataView();
            new FetchMovieTask().execute(API_Query);
        }



    }
    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movieInformation The movie data for the poster that was clicked
     */
    @Override
    public void onClick(String[] movieInformation) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra ("movieInfo", movieInformation);
        startActivity(intentToStartDetailActivity);
    }
    /** EDIT########################
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    /**
     * This method will make the error message visible and hide the movie poster views
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[][]> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[][] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String url = params[0];

            URL MovieRequestUrl = NetworkUtils.buildUrl(url);

            try {

                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(MovieRequestUrl);


                String[][] simpleJsonWeatherData = OpenMovieJsonUtils
                        .getSimpleMovieStringsFromJson(jsonWeatherResponse );

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String[][] MovieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (MovieData != null) {
                movieDataArray = MovieData;
                mImageAdapter.setImageData(MovieData);
            } else {
                showErrorMessage();
            }
        }
    }
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()){
            return true;
        }else {
            Toast.makeText(MainActivity.this, getString(R.string.connectivity_fail_msg), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_pop) {
            sortType = "popular?";
            String API_Query = Base_API_Query + sortType+ API_Key;
            new FetchMovieTask().execute(API_Query);
            return true;
        }


        if (id == R.id.action_sort_rating) {
            sortType = "top_rated?";
            String API_Query = Base_API_Query + sortType + API_Key;
            new FetchMovieTask().execute(API_Query);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
