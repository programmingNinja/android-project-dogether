package com.codepath.apps.DoGether.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.adapters.LandingActivityViewAdapter;
import com.codepath.apps.DoGether.models.LandingActivityView;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {

    private ArrayList<LandingActivityView> userEvents;
    private LandingActivityViewAdapter aUserEvents;
    private ListView lvCommunityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

    }


    public void setUpViews(){
        lvCommunityView = (ListView) findViewById(R.id.lvCommunityView);
        userEvents = new ArrayList<>();
        aUserEvents = new LandingActivityViewAdapter(this,userEvents);
        lvCommunityView.setAdapter(aUserEvents);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    public void onCreateEventAction(MenuItem mi){
        Intent i = new Intent(LandingActivity.this, CreateEventActivity.class);
        startActivity(i);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}

