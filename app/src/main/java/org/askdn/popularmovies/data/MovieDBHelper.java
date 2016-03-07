package org.askdn.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.askdn.popularmovies.data.MovieContract.*;

/**
 * Created by ashish on 7/3/16.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "tmdb.db";
    public static final int DATABASE_VERSION = 1;
    private static final String LOG_TAG = MovieDBHelper.class.getSimpleName();

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_REVIEW_TABLE =
                "CREATE TABLE  " +
                        ReviewEntry.TABLE_NAME +
                        " (" +
                        ReviewEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                        ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                        ReviewEntry.COLUMN_REVIEW + " TEXT NOT NULL,  " +
                        " PRIMARY KEY (" + ReviewEntry.COLUMN_ID + ")," +
                        "FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_ID +
                        ") references movie (" + MovieEntry.COLUMN_ID +
                        ");";

        Log.d(LOG_TAG, "Review Table Successfully created");

        final String SQL_CREATE_VIDEO_TABLE =
                "CREATE TABLE  " +
                        VideoEntry.TABLE_NAME +
                        " (" +
                        VideoEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                        VideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        VideoEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                        VideoEntry.COLUMN_NAME + " TEXT NOT NULL,  " +
                        VideoEntry.COLUMN_SITE + " TEXT NOT NULL,  " +
                        VideoEntry.COLUMN_SIZE + " TEXT NOT NULL,  " +
                        VideoEntry.COLUMN_TYPE + " TEXT NOT NULL,  " +
                        " PRIMARY KEY (" + VideoEntry.COLUMN_ID + "," + VideoEntry.COLUMN_KEY + ")," +
                        "FOREIGN KEY (" + VideoEntry.COLUMN_MOVIE_ID +
                        ") references movie (" + MovieEntry.COLUMN_ID +
                        ");";

        Log.d(LOG_TAG, "Video Table Successfully created");


        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE  " +
                        MovieEntry.TABLE_NAME +
                        " (" +
                        MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                        MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                        MovieEntry.COLUMN_RUNTIME + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_FAVORITE + " INTEGER DEFAULT 0 " +
                        ");";

        Log.d(LOG_TAG, "Movie table successfully created");

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.w(LOG_TAG, " Old database version is : " + oldVersion
                    + " New database version is:" + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME);
            onCreate(db);

        }
    }
}
