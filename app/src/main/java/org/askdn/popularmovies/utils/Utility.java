package org.askdn.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import org.askdn.popularmovies.BuildConfig;
import org.askdn.popularmovies.Movie;
import org.askdn.popularmovies.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashish on 7/3/16.
 */
public class Utility {

    private final static String LOG_TAG = Utility.class.getSimpleName();

    // Strings for API Calls
    private final static String TMDB_URL_SCHEME = "https";
    private final static String TMDB_BASE_URL_AUTHORITY = "api.themoviedb.org";
    private final static String TMDB_BASE_URL_IMAGE_AUTHORITY = "image.tmdb.org";
    private final static String TMDB_VERSION = "3";
    private final static String TMDB_IMG_PATH_T = "t";
    private final static String TMDB_IMG_PATH_P = "p";
    private final static String TMDB_PATH_MOVIE = "movie";
    private final static String TMDB_PATH_DISCOVER = "discover";
   // private final static String TMDB_QUERY_PAGE = "page";
    private static final String TMDB_QUERY_REVIEWS = "reviews";
    private static final String TMDB_QUERY_VIDEOS = "videos" ;
    private static final String TMDB_QUERY_VIDEOS_REVIEWS = "videos, reviews" ;

    // Sort preferences
    private final static String TMDB_SORT_BY = "sort_by";
    private final static String TMDB_QUERY_SORT_BY_POPULARITY_DESC = "popularity.desc";
    private final static String TMDB_QUERY_SORT_BY_VOTE_AVERAGE_DESC = "vote_average.desc";
    private final static String TMDB_QUERY_APPEND_TO_RESPONSE = "append_to_response";

    // API KEY
    private final static String TMDB_API_KEY = BuildConfig.TMDB_API_KEY;
    private final static String TMDB_QUERY_API_KEY = "api_key";


        /*
         * This method build a URL for fetching Movie by Popularity
         */
    public static String buildURLMoviePopularity() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(TMDB_URL_SCHEME)
            .authority(TMDB_BASE_URL_AUTHORITY)
            .appendPath(TMDB_VERSION)
            .appendPath(TMDB_PATH_DISCOVER)
            .appendPath(TMDB_PATH_MOVIE)
            .appendQueryParameter(TMDB_QUERY_API_KEY, TMDB_API_KEY)
            .appendQueryParameter(TMDB_SORT_BY, TMDB_QUERY_SORT_BY_POPULARITY_DESC);
        return builder.toString();
    }


    /*
     * This method build a URL for fetching Movie by Highest Rating
     */
    public static String buildURLMovieRating() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(TMDB_URL_SCHEME)
                .authority(TMDB_BASE_URL_AUTHORITY)
                .appendPath(TMDB_VERSION)
                .appendPath(TMDB_PATH_DISCOVER)
                .appendPath(TMDB_PATH_MOVIE)
                .appendQueryParameter(TMDB_QUERY_API_KEY, TMDB_API_KEY)
                .appendQueryParameter(TMDB_SORT_BY, TMDB_QUERY_SORT_BY_VOTE_AVERAGE_DESC);
        return builder.toString();
    }

        /*
         * This method creates a Url for fetching videos and reviews for a movie
        */
        public static String buildUriMovieWithDetailsVideosAndReviews(String movieId) {
            Uri.Builder builder = new Uri.Builder()
                    .scheme(TMDB_URL_SCHEME)
                    .authority(TMDB_BASE_URL_AUTHORITY)
                    .appendPath(TMDB_VERSION)
                    .appendPath(TMDB_PATH_MOVIE)
                    .appendPath(movieId)
                    .appendQueryParameter(TMDB_QUERY_API_KEY, TMDB_API_KEY)
                    .appendQueryParameter(TMDB_QUERY_APPEND_TO_RESPONSE,TMDB_QUERY_VIDEOS_REVIEWS);
            Log.d(LOG_TAG, builder.toString());
            return builder.toString();
        }

        /*
         * This method creates a Url for fetching trailers for a movie
        */
        public static String buildUriMovieWithDetailsAndTrailers(String movieId) {
            Uri.Builder builder = new Uri.Builder()
                    .scheme(TMDB_URL_SCHEME)
                    .authority(TMDB_BASE_URL_AUTHORITY)
                    .appendPath(TMDB_VERSION)
                    .appendPath(TMDB_PATH_MOVIE)
                    .appendPath(movieId)
                    .appendQueryParameter(TMDB_QUERY_API_KEY, TMDB_API_KEY)
                    .appendQueryParameter(TMDB_QUERY_APPEND_TO_RESPONSE, TMDB_QUERY_VIDEOS);

            return builder.toString();
        }


        /*
         * This method creates a Url for fetching reviews for a movie
        */
        public static String buildUriMovieReviews(int page, String movieId) {
            Uri.Builder builder = new Uri.Builder()
                .scheme(TMDB_URL_SCHEME)
                .authority(TMDB_BASE_URL_AUTHORITY)
                .appendPath(TMDB_VERSION)
                .appendPath(TMDB_PATH_MOVIE)
                .appendPath(movieId)
                .appendPath(TMDB_QUERY_REVIEWS)
                .appendQueryParameter(TMDB_QUERY_API_KEY, TMDB_API_KEY);
            return builder.toString();
        }

    /*
     * This method creates a Url for fetching image for particular size
     */
            public static String buildImageUrl(String path, String size) {
                path = path.replaceFirst("\\/", "");
                Uri.Builder builder = new Uri.Builder()
                    .scheme(TMDB_URL_SCHEME)
                    .authority(TMDB_BASE_URL_IMAGE_AUTHORITY)
                    .appendPath(TMDB_IMG_PATH_T)
                    .appendPath(TMDB_IMG_PATH_P)
                    .appendPath(size)
                    .appendPath(path);
                return builder.toString();
            }

}