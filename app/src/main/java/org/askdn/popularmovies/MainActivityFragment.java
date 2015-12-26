package org.askdn.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private View mRootView;
    public final String URI_RESPONSE_TYPE="GET";
    public final String URI_SCHEME;
    public final String URI_DOMAIN;
    public final String URI_VERSION;
    public final String URI_DISCOVER;
    public final String URI_MOVIE;
    public final String URI_SORT;
    public final String URI_API_QUERY;
    public final String URI_API_KEY;
    private boolean mNetworkState=false;


    public MovieAdapter mMovieAdapter;
    public GridView mGridView;
    public ArrayList<Movie> mMovieData;
    public MainActivityFragment() {
        URI_SCHEME = getString(R.string.scheme);
        URI_DOMAIN = getString(R.string.domain);
        URI_VERSION = getString(R.string.version);
        URI_DISCOVER = getString(R.string.path1);
        URI_MOVIE = getString(R.string.path2);
        URI_SORT =getString(R.string.sort);
        URI_API_QUERY = getString(R.string.api_query_param);
        URI_API_KEY = getString(R.string.moviedb_api);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);

        mMovieData = new ArrayList<>();
        mNetworkState=((ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
        setHasOptionsMenu(true);
        mMovieAdapter = new MovieAdapter(getActivity(), R.layout.movie_single_item, mMovieData);
        mGridView.setAdapter(mMovieAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.most_pop) {
            //Execute API call for Most Popular Movies
            UpdateUI(getString(R.string.sort_popular));
        }
        if(id == R.id.highestrated)
        {
            //Execute API call for Highest Rated Movies
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(mNetworkState==false) {
            // Check with replacing the Fragment with the No Internet Fragment
            mRootView = inflater.inflate(R.layout.fragment_nointernet, container, false);
        } else {
            mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public String parseURI(String sortOrder) {
        String url=null;
        Uri.Builder builder = new Uri.Builder();
        if(sortOrder==getString(R.string.sort_popular)) {

            builder.scheme(URI_SCHEME)
                    .authority(URI_DOMAIN)
                    .appendPath(URI_VERSION)
                    .appendPath(URI_DISCOVER)
                    .appendPath(URI_MOVIE)
                    .appendQueryParameter(URI_SORT, sortOrder)
                    .appendQueryParameter(URI_API_QUERY, URI_API_KEY);
             url = builder.build().toString();
            }

        return url;

    }
    public void UpdateUI(String sortOrder) {

        FetchMovieDetails task = new FetchMovieDetails();
        task.execute(parseURI(sortOrder));

    }

    public class FetchMovieDetails extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            String api_call_string = params[0];

            HttpURLConnection urlConnection = null;
            String inputStringJSON = null;
            BufferedReader reader = null;

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

            try {
                return getMovieData(inputStringJSON);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }


            return null;
        }

        public ArrayList<Movie> getMovieData(String inputStringJSON) throws JSONException {

            String poster_imgtitle, overview, release_date, original_title, title;
            double vote_average;
            JSONObject data = new JSONObject(inputStringJSON);
            JSONArray movielist = data.getJSONArray("results");
            for (int i = 0; i <= movielist.length(); i++) {

                JSONObject moviedetail = movielist.getJSONObject(i);
                overview = moviedetail.getString("overview");
                vote_average = moviedetail.getDouble("vote_average");
                release_date = moviedetail.getString("release_date");
                title = moviedetail.getString("title");
                original_title = moviedetail.getString("original_title");
                poster_imgtitle = getImageURL(moviedetail.getString("poster_path"));

                mMovieData.add(new Movie(title, original_title, vote_average, release_date,
                        poster_imgtitle, overview));
            }


            return mMovieData;
        }

        public String getImageURL(String poster_imgtitle) {
            return getString(R.string.base_url) + getString(R.string.img_size) + poster_imgtitle;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {
                mMovieAdapter.setMovieData(movies);
            } else {
                Log.i(LOG_TAG, "Fetch Data failure");
            }
        }
    }

}
