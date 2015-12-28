package org.askdn.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;

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
 * The Fragment controlling Main UI view and Network Tasks
 * */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener{

    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private View mRootView;
    private ProgressBar mProgressBar=null;
    public final String URI_RESPONSE_TYPE="GET";
    public MovieAdapter mMovieAdapter;
    public GridView mGridView;
    public ArrayList<Movie> mMovieData;

    public MainActivityFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mMovieData = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences userpref = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        String userSortType = userpref.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default));

        //onStart Fetch Network Task based on the userPref
        updateMovieUI(userSortType);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent launchSettings = new Intent(getActivity(),SettingsActivity.class);
            startActivity(launchSettings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        mMovieAdapter = new MovieAdapter(getActivity(),new ArrayList<Movie>());
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mGridView = (GridView) mRootView.findViewById(R.id.movies_grid);
        mGridView.setAdapter(mMovieAdapter);
        mProgressBar.setVisibility(View.VISIBLE);
        mGridView.setOnItemClickListener(this);
        initSpinner(mRootView);
        return mRootView;
    }

    // To build a Fetch URL for the data
    public String getURL(String sortOrder) {

        Uri.Builder builder = new Uri.Builder();
            builder.scheme(getString(R.string.scheme))
                    .authority(getString(R.string.domain))
                    .appendPath(getString(R.string.version))
                    .appendPath(getString(R.string.path1))
                    .appendPath(getString(R.string.path2))
                    .appendQueryParameter(getString(R.string.sort), sortOrder)
                    .appendQueryParameter(getString(R.string.api_query_param),
                            getString(R.string.moviedb_api));
        return builder.build().toString();

    }

    // executes the network tasks
    public void updateMovieUI(String sortOrder) {
        FetchMovieDetails task = new FetchMovieDetails();
        task.execute(getURL(sortOrder));
    }

    //Intializes and Activates the Spinner for Quick Sorting
    public void initSpinner(View view) {
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        //Adapter for setting up values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                              R.array.spinner_data, android.R.layout.simple_spinner_item);
        //Set it for dropdown values
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Set a listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        updateMovieUI(getString(R.string.sort_popular));
                        break;
                    case 1:
                        updateMovieUI(getString(R.string.sort_rating));
                        break;
                    default:
                        SharedPreferences userpref = PreferenceManager
                                .getDefaultSharedPreferences(getActivity());
                        String userSortType = userpref.getString(getString(R.string.pref_sort_key),
                                getString(R.string.pref_sort_default));
                        //onStart Fetch Network Task based on the userPref
                        updateMovieUI(userSortType);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //selects a single Movie Item from the GridView
        Movie selectItem = (Movie) parent.getItemAtPosition(position);

        Intent showDetails = new Intent(getActivity(),DetailActivity.class);
        showDetails.putExtra(getString(R.string.KEY_IMAGE),selectItem.getPoster());
        showDetails.putExtra(getString(R.string.KEY_OVERVIEW),selectItem.getOverview());
        showDetails.putExtra(getString(R.string.KEY_RDATE),selectItem.getDate());
        showDetails.putExtra(getString(R.string.KEY_TITLE),selectItem.gettitle());
        showDetails.putExtra(getString(R.string.KEY_VOTE),selectItem.getVoteAvg());
        startActivity(showDetails);

    }

    public class FetchMovieDetails extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            //Fetches the Input String URL passed.
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
            return getString(R.string.base_url) + getString(R.string.img_size) + poster_imgtitle;
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
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

}
