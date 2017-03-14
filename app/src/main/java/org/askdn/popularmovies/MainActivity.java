package org.askdn.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

/*The Launcher Activity*/
public class MainActivity extends AppCompatActivity {

    String MIXPANEL_TOKEN = "bdf2909825dd07af271f75c2ad560407";
    boolean mDualPane;
    public final String DFTAG = "detail_fragment";
    private MixpanelAPI mixpanel;

{
        mixpanel =
                MixpanelAPI.getInstance(getApplicationContext(), MIXPANEL_TOKEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            JSONObject props = new JSONObject();
            props.put("Gender", "Female");
            props.put("Logged in", false);
            mixpanel.track("MainActivity - onCreate called", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        if(findViewById(R.id.movie_detail_container)!=null) {
            mDualPane = true;
            if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container,new DetailActivityFragment(),DFTAG).commit();
        }
        else
            mDualPane = false;
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();

    }
}
