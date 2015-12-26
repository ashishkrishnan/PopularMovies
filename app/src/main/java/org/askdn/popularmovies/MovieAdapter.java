package org.askdn.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import org.askdn.popularmovies.MainActivityFragment;

/**
 * Created by ashish on 25/12/15.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {


    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<Movie> mMovieData = new ArrayList<>();
    public MovieAdapter(Activity context, int resourceid, ArrayList<Movie> movielist) {
        super(context, resourceid, movielist);
        this.mContext = context;
        this.mLayoutResourceId = resourceid;
        this.mMovieData = movielist;
    }
    public void setMovieData(ArrayList<Movie> movieData) {
        this.mMovieData = movieData;
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.movie_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Movie item = mMovieData.get(position);
        Picasso.with(mContext).load(item.movie_poster).into(holder.imageView);
        return row;
    }
    static class ViewHolder {
        ImageView imageView;
    }

}