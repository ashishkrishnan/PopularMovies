package org.askdn.popularmovies;

import android.content.Context;
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
    private View mrootView;
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

        mNetworkState=((ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
        setHasOptionsMenu(true);
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
            mrootView = inflater.inflate(R.layout.fragment_nointernet, container, false);
        } else {
            mrootView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        return mrootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public String parseURI(String sortOrder) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(URI_SCHEME)
                .authority(URI_DOMAIN)
                .appendPath(URI_VERSION)
                .appendPath(URI_DISCOVER)
                .appendPath(URI_MOVIE)
                .appendQueryParameter(URI_SORT,sortOrder)
                .appendQueryParameter(URI_API_QUERY,URI_API_KEY);
        return builder.build().toString();

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
                if(is==null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while((line=reader.readLine())!=null) {
                    buffer.append(line+"\n");
                }
                if(buffer.length()==0) return null; // stream was empty
                inputStringJSON = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG,"Error", e);

            }

            finally {
                if(urlConnection!=null) {
                    urlConnection.disconnect();
                }
                if(reader!=null) {
                    try {
                        reader.close();
                    }
                    catch(IOException e) {
                        Log.e(LOG_TAG,"Error closing data stream", e);
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

        public ArrayList<Movie> getMovieData(String inputStringJSON) throws JSONException{

            JSONObject data = new JSONObject(inputStringJSON);

            // Begin Here;



            return null;
        }


    }

}
