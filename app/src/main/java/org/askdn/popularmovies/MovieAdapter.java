package org.askdn.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by ashish on 25/12/15.
 */
public class MovieAdapter extends BaseAdapter{

    private final Context mContent;

    ArrayList<Movie> list;
    MovieAdapter(Context content) {
        list=new ArrayList<>();
        mContent = content;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView==null)  { //Check whether view is not initialised.

        }
        else {
            imageView = (ImageView) convertView;
        }



        return null;
    }
}
