package org.askdn.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.askdn.popularmovies.network.FetchEngine;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The Fragment controlling Main UI view and Network Tasks
 * */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener{

    static int count=0;
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private View mRootView;
    private RequestQueue mQueue;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        mMovieAdapter = new MovieAdapter(getActivity(),new ArrayList<Movie>());
        mGridView = (GridView) mRootView.findViewById(R.id.movies_grid);
        mGridView.setAdapter(mMovieAdapter);
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
        Context context = getActivity().getApplication().getApplicationContext();
        String url = getURL(sortOrder);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(LOG_TAG+(count++),response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        FetchEngine.getInstance(context).getRequestQueue().add(jsonObjectRequest);
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



}
