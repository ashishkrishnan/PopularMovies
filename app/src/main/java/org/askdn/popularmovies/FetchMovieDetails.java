package org.askdn.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ashish on 19/2/16.
 */
public class FetchMovieDetails extends AsyncTask<String, Void, ArrayList<Movie>> { // Use okHTTP or Volley for making Network Calls.

    public MovieAdapter mMovieAdapter;
    public Context mContext;
    public final String URI_RESPONSE_TYPE="GET";
    public final String LOG_TAG = FetchMovieDetails.class.getName();

    @Override
    protected void onPreExecute() { }


    FetchMovieDetails(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        //Fetches the Input String URL passed.
        String api_call_string = params[0];

        HttpURLConnection urlConnection = null;
        String inputStringJSON = null;
        BufferedReader reader = null;

        Log.i(LOG_TAG,api_call_string);
        try {
            // Create a new URL object
            URL url = new URL(api_call_string);
            // Open a URL connection
            urlConnection = (HttpURLConnection) url.openConnection();
            // Set the Connection Request Method
            urlConnection.setRequestMethod(URI_RESPONSE_TYPE);
            // Establish a connection
            urlConnection.connect();

            // Connect to a stream
            InputStream is = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (is == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) return null; // stream was empty
            inputStringJSON = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing data stream", e);
                }
            }
        }
        Log.i(LOG_TAG,inputStringJSON);
        try {
            return getMovieData(inputStringJSON);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    //parses the Movie Data
    public ArrayList<Movie> getMovieData(String inputStringJSON) throws JSONException {

        final String RESULTS = "results";
        final String TITLE = "title";
        final String POSTER_PATH = "poster_path";
        final String ORIGINAL_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average";

        String poster_imgtitle, overview, release_date, original_title, title,vote_average;
        ArrayList<Movie> moviesDataList = new ArrayList<>();

        JSONObject data = new JSONObject(inputStringJSON);
        JSONArray movielist = data.getJSONArray(RESULTS);

        for (int i = 0; i <movielist.length(); i++) {

            JSONObject movieJSONdetail = movielist.getJSONObject(i);
            overview = movieJSONdetail.getString(OVERVIEW);
            vote_average = movieJSONdetail.getString(VOTE_AVERAGE);
            release_date = movieJSONdetail.getString(RELEASE_DATE);
            title = movieJSONdetail.getString(TITLE);
            original_title = movieJSONdetail.getString(ORIGINAL_TITLE);
            poster_imgtitle = getImageURL(movieJSONdetail.getString(POSTER_PATH));

            moviesDataList.add(new Movie(title,original_title,vote_average,release_date,poster_imgtitle
                    ,overview));

        }
        return moviesDataList;
    }

    // Return a full image URL required for Imaging Processing and Caching
    public String getImageURL(String poster_imgtitle) {
        return mContext.getString(R.string.base_url) + mContext.getString(R.string.img_size) + poster_imgtitle;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> results) {
        if (results != null)
        {
            mMovieAdapter.clear();
            for (Movie movie : results)
            {
                mMovieAdapter.add(movie);
            }
        }
    }
}