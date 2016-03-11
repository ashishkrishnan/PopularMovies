package org.askdn.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressWarnings("unused")
public class DetailActivityFragment extends Fragment {


    private Intent mGetDetils;

    public DetailActivityFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the intent
        mGetDetils = getActivity().getIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Bind the ButterKnife to this view
        ButterKnife.bind(this, rootView);

        //set the data from the Intent to be shown on the UI
        dTitle.setText(mGetDetils.getStringExtra(getString(R.string.KEY_TITLE)));
        dVote.setText(mGetDetils.getStringExtra(getString(R.string.KEY_VOTE)));
        dOverview.setText(mGetDetils.getStringExtra(getString(R.string.KEY_OVERVIEW)));
        dRelease.setText(mGetDetils.getStringExtra(getString(R.string.KEY_RDATE)));

        // Load the image from the Cache/Network
        Picasso.with(getContext()).load(mGetDetils.getStringExtra(getString(R.string.KEY_IMAGE))).into(dPoster);
        return rootView;
    }

    //Bind the UI using ref id to variables
    @Bind(R.id.detail_poster) ImageView dPoster;
    @Bind(R.id.detail_overview) TextView dOverview;
    @Bind(R.id.detail_title) TextView dTitle;
    @Bind(R.id.detail_rating) TextView dVote;
    @Bind(R.id.detail_release) TextView dRelease;
}
