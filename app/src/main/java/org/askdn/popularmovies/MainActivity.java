package org.askdn.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/*The Launcher Activity*/
public class MainActivity extends AppCompatActivity {

    boolean mDualPane;
    public final String DFTAG = "detail_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.movie_detail_container)!=null) {
            mDualPane = true;
            if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container,new DetailActivityFragment(),DFTAG).commit();
        }
        else
            mDualPane = false;
    }
}
