package org.askdn.popularmovies;

import android.graphics.Bitmap;

/**
 * Created by ashish on 25/12/15.
 */

public class Movie {
    public String title;
    public String original_title;
    public double vote_average ;
    public String release_date;
    public String movie_poster;
    public String overview;

    public Movie(String title, String original_title,
                 double vote_average,
                 String release_date,
                 String movie_poster,
                 String overview) {

        this.title = title;
        this.original_title = original_title;
        this.vote_average = vote_average;
        this.movie_poster = movie_poster;
        this.release_date = release_date;
        this.overview = overview;
    }
}
