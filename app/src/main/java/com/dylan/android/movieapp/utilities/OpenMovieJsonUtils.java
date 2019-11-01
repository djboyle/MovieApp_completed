package com.dylan.android.movieapp.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dyl on 21/05/2018.
 */

public final class OpenMovieJsonUtils {
    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the movie data and poster/Background image paths .
     * <p/>

     * @param movieJsonStr JSON response from server
     *
     * @return Array of Strings describing movie data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */

    public static String[][] getSimpleMovieStringsFromJson(String movieJsonStr)
            throws JSONException {

        /* String array to hold each movie's information String */
        String[][] parsedMovieData;


        JSONObject MovieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = MovieJson.getJSONArray("results");

        parsedMovieData = new String[movieArray.length()+1][movieArray.length()];



        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieDetails = movieArray.getJSONObject(i);
            parsedMovieData[0][i] = movieDetails.getString("poster_path");
            parsedMovieData[i+1][0] = movieDetails.getString("original_title");
            parsedMovieData[i+1][1] = movieDetails.getString("poster_path");
            parsedMovieData[i+1][2] = movieDetails.getString("overview");
            parsedMovieData[i+1][3] = movieDetails.getString("vote_average");
            parsedMovieData[i+1][4] = movieDetails.getString("release_date");
            parsedMovieData[i+1][5] = movieDetails.getString("backdrop_path");
        }


        return parsedMovieData;
    }




}
