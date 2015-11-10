package com.codepath.apps.DoGether.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.DoGether.LocalModels.LocalSubscription;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.adapters.LandingActivityViewAdapter;
import com.codepath.apps.DoGether.models.Event;
import com.codepath.apps.DoGether.models.LandingActivityView;
import com.codepath.apps.DoGether.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends AppCompatActivity {

    private ArrayList<LandingActivityView> userEvents;
    private LandingActivityViewAdapter aUserEvents;
    private ListView lvCommunityView;
    private String eventText[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        setUpViews();
        getEventsForCommunity();

    }

    public void setUpViews(){
        lvCommunityView = (ListView) findViewById(R.id.lvCommunityView);
        userEvents = new ArrayList<>();
        aUserEvents = new LandingActivityViewAdapter(this,userEvents);
        lvCommunityView.setAdapter(aUserEvents);


    }

    public void getEventsForCommunity() {

        String communityId = LocalSubscription.getCommunity();
        final ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.whereEqualTo("communityId", communityId);
        query.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> itemList, ParseException e) {
                if (e == null) {
                    aUserEvents.clear();
                    for(int j=0;j<itemList.size();j++){
                        try {
                            LandingActivityView lv = new LandingActivityView();
                            lv.setComUserEventText(itemList.get(j).get("text").toString());
                            ParseQuery query = itemList.get(j).getRelation("fromUser").getQuery();
                            ParseObject parseObject= query.getFirst();
                            lv.setComUserUserName(parseObject.get("name").toString());
                            lv.setComUserUserPhoto(parseObject.get("profile_image_url").toString());
                            userEvents.add(lv);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }

                    aUserEvents.notifyDataSetChanged();

                }
            }
        });
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

