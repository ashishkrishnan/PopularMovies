package org.askdn.popularmovies;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private View mRootView;
    private Intent mGetDetils;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGetDetils = getActivity().getIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView dTitle = (TextView) mRootView.findViewById(R.id.detail_title);
        TextView dOverview = (TextView) mRootView.findViewById(R.id.detail_overview);
        TextView dVote = (TextView) mRootView.findViewById(R.id.detail_rating);
        TextView dRelease = (TextView) mRootView.findViewById(R.id.detail_release);
        ImageView dposter = (ImageView) mRootView.findViewById(R.id.detail_poster);

        dTitle.setText(mGetDetils.getStringExtra(getString(R.string.KEY_TITLE)));
        dVote.setText(mGetDetils.getStringExtra(getString(R.string.KEY_VOTE)));
        dOverview.setText(mGetDetils.getStringExtra(getString(R.string.KEY_OVERVIEW)));
        dRelease.setText(mGetDetils.getStringExtra(getString(R.string.KEY_RDATE)));

        Picasso.with(getContext()).load(mGetDetils.getStringExtra(getString(R.string.KEY_IMAGE)))
                .into(dposter);
        return mRootView;
    }
}
