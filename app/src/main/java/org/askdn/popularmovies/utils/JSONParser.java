package org.askdn.popularmovies.utils;

import org.askdn.popularmovies.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashish on 7/3/16.
 */
public class JSONParser {

    // Helper method for parsing Movie JSON
    public static ArrayList<Movie> parseMovieJSON(JSONObject response) throws JSONException {
        final String RESULTS = "results";
        final String TITLE = "title";
        final String id = "id";
        final String POSTER_PATH = "poster_path";
        final String ORIGINAL_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average";

        String poster_imgtitle, overview, release_date, original_title, title,vote_average;
        long _id;
        ArrayList<Movie> moviesDataList = new ArrayList<>();
        JSONArray movielist = response.getJSONArray(RESULTS);

        for (int i = 0; i <movielist.length(); i++) {

            JSONObject movieJSONdetail = movielist.getJSONObject(i);

            _id = movieJSONdetail.getLong(id);
            overview = movieJSONdetail.getString(OVERVIEW);
            vote_average = movieJSONdetail.getString(VOTE_AVERAGE);
            release_date = movieJSONdetail.getString(RELEASE_DATE);
            title = movieJSONdetail.getString(TITLE);
            original_title = movieJSONdetail.getString(ORIGINAL_TITLE);

            poster_imgtitle = Utility.buildImageUrl(movieJSONdetail.getString(POSTER_PATH),"w185");

            moviesDataList.add(new Movie(_id,title, original_title, vote_average, release_date, poster_imgtitle
                    , overview));
        }
        return moviesDataList;
    }

}
