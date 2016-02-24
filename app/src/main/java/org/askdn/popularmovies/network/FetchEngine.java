package org.askdn.popularmovies.network;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
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
            // Using Custom Implementation of Network and Cache
            Cache cache = new DiskBasedCache(mFetchContext.getCacheDir(),10*1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache,network);
            mRequestQueue.start();
        }
      return mRequestQueue;
    }

    // Serving the Singleton RequestQueue for the all types of Volley Requests
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
