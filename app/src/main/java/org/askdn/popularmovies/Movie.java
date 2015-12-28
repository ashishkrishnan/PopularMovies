package org.askdn.popularmovies;

/**
 * Created by ashish on 25/12/15.
 */

public class Movie {
    public String title;
    public String original_title;
    public String vote_average ;
    public String release_date;
    public String movie_poster;
    public String overview;

    Movie(String title,String original_title, String vote_average, String release_date,
           String movie_poster,
           String overview) {

        this.title = title;
        this.original_title = original_title;
        this.vote_average = vote_average;
        this.movie_poster = movie_poster;
        this.release_date = release_date;
        this.overview = overview;
    }

    public String gettitle(){
        return title;
    }
    public String getPoster() {return movie_poster;}
    public String getVoteAvg() {
        return vote_average;
    }
    public String getOverview() {
        return overview;
    }
    public String getDate() {return release_date;}

}
