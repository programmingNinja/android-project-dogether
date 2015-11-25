package com.codepath.apps.DoGether.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.codepath.apps.DoGether.LocalModels.LocalSubscription;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.RestApplication;
import com.codepath.apps.DoGether.TwitterClient;
import com.codepath.apps.DoGether.adapters.LandingActivityViewAdapter;
import com.codepath.apps.DoGether.helpers.SimpleProgressDialog;
import com.codepath.apps.DoGether.models.Event;
import com.codepath.apps.DoGether.models.LandingActivityView;
import com.codepath.apps.DoGether.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<LandingActivityView> userEvents;
    private LandingActivityViewAdapter aUserEvents;
    private ListView lvCommunityView;
    private String eventText[];

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle drawerToggle;
    NavigationView nvDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null


        // Find our drawer view
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        dlDrawer.setDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        nvDrawer.getMenu().getItem(0).setChecked(true);
        setSupportActionBar(toolbar);

        client = RestApplication.getRestClient();

        SimpleProgressDialog dialog = SimpleProgressDialog.build(this, "loading...");
        dialog.show();
        setUpViews();
        getEventsForCommunity();
        dialog.dismiss();

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
                    for (int j = 0; j < itemList.size(); j++) {
                        try {
                            LandingActivityView lv = new LandingActivityView();
                            lv.setComUserEventText(itemList.get(j).get("text").toString());
                            ParseQuery query = itemList.get(j).getRelation("fromUser").getQuery();
                            ParseObject parseObject = query.getFirst();
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

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                nvDrawer.bringToFront();
                dlDrawer.requestLayout();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
    }

    private void setupDrawerContent(NavigationView navigationView) {
        System.out.println("outer item clicked");
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        System.out.println("item clicked");
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        switch(menuItem.getItemId()) {
            case R.id.my_profile:
                startActivity(new Intent(this, MyProfileActivity.class));
                break;
            case R.id.subscribed_community:
                System.out.println("subscribed clicked");
                startActivity(new Intent(this, CommunityActivity.class));
                break;
            case R.id.search_community:
                System.out.println("search clicked");
                startActivity(new Intent(this, LandingActivity.class));
                break;
            case R.id.logout:
                client.logout();
                break;
            default:
                startActivity(new Intent(this, ProfileActivity.class));
        }
        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

}

