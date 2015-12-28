package org.askdn.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ashish on 25/12/15.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {


    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param griddata A ArrayList of Movie objects to display in a list
     */

    public MovieAdapter(Activity context, ArrayList<Movie> griddata) {
        super(context, 0, griddata);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param view The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Movie item = getItem(position);

        // If the view is being set for the first time.
        // View has not be recycled
        if(view == null) {
            view = LayoutInflater.from(
                    getContext()).inflate(R.layout.movie_single_item, parent, false);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load(item.getPoster()).into(imageView);
        return view;
    }


}