package org.askdn.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.askdn.popularmovies.network.CustomRequest;
import org.askdn.popularmovies.network.FetchEngine;
import org.askdn.popularmovies.utils.JSONParser;
import org.askdn.popularmovies.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The Fragment controlling Main UI view and Network Tasks
 * */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener{

    private int mPosition=0;
    private static final int SPIN_POPULAR = 0;
    private static final int SPIN_RATING = 1;
    private static final int SPIN_FAVOURITE = 2;
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName()+":";
    private Spinner mSpinner;
    public MovieAdapter mMovieAdapter;
    public GridView mGridView;

    public MainActivityFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

            final String byRating = getString(R.string.sort_rating);
            final String byPopularity = getString(R.string.sort_popular);

            //onStart Fetch Network Task based on the userPref

            SharedPreferences userpref = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            String userSortType = userpref.getString(getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_default));

            if (userSortType.equals(byRating)) {
                mPosition=1;
                updateMovieUI(Utility.buildURLMovieRating());
            } else if (userSortType.equals(byPopularity)) {
                mPosition=0;
                updateMovieUI(Utility.buildURLMoviePopularity());
            }
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mMovieAdapter = new MovieAdapter(getActivity(),new ArrayList<Movie>());
        mGridView = (GridView) rootView.findViewById(R.id.movies_grid);
        mGridView.setAdapter(mMovieAdapter);
        mGridView.setOnItemClickListener(this);
        initSpinner(rootView);
        return rootView;
    }


    // executes the network tasks
    public void updateMovieUI(String url) {
        Context context = getActivity().getApplication();
        CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Movie> parsedMovie = JSONParser.parseMovieJSON(response);
                    if (parsedMovie != null) {
                        mMovieAdapter.clear();
                        for (Movie movie : parsedMovie) {
                            mMovieAdapter.add(movie);
                        }
                    }
                }
                catch (JSONException e) {
                        Log.e(LOG_TAG+JSONException.class.getSimpleName(),e.toString());
                }
        }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG+VolleyError.class.getSimpleName(),error.toString());
            }
        });

    }

    //Intializes and Activates the Spinner for Quick Sorting
    private void initSpinner(View view) {

        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        //Adapter for setting up values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                              R.array.spinner_data, android.R.layout.simple_spinner_item);
        //Set it for dropdown values
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        //Set a listener
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        updateMovieUI(Utility.buildURLMoviePopularity());
                        //Utility.savedToPrefs(getActivity(),sharedPref,position);
                        break;
                    case 1:
                        updateMovieUI(Utility.buildURLMovieRating());
                        //Utility.savedToPrefs(getActivity(),sharedPref,position);
                        break;
                    case 2:

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

    @Override
    public void onResume() {
        super.onResume();

        if(mPosition==0) mSpinner.setSelection(SPIN_POPULAR);
        else if(mPosition==1) mSpinner.setSelection(SPIN_RATING);
    }
}
