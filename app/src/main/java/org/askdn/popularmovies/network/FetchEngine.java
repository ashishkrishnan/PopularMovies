package org.askdn.popularmovies.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ashish on 24/2/16.
 */


//Singleton Class for Volley
public class FetchEngine {
    private static FetchEngine mInstance;
    private RequestQueue mRequestQueue;
    private static Context mFetchContext;

    private FetchEngine(Context context) {
        mFetchContext = context;
        mRequestQueue = getRequestQueue();
    }

    //Creating an Instance of the Singleton Class
    public static synchronized FetchEngine getInstance(Context context) {
        if(mInstance==null) {
            mInstance = new FetchEngine(context);
        }
        return mInstance;
    }

    // Applying the RequestQueue for the Entire Application
    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mFetchContext.getApplicationContext());
        }
      return mRequestQueue;
    }

    // Serving the Singleton RequestQueue for the all types of Volley Requests
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
