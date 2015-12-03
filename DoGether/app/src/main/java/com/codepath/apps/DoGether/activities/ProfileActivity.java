package com.codepath.apps.DoGether.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.apps.DoGether.LocalModels.LocalSubscription;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.RestApplication;
import com.codepath.apps.DoGether.TwitterClient;
import com.codepath.apps.DoGether.helpers.NetworkConnection;
import com.codepath.apps.DoGether.helpers.SimpleProgressDialog;
import com.codepath.apps.DoGether.models.Subscription;
import com.codepath.apps.DoGether.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private TwitterClient client;
    private User currentUser;
    private Spinner communitiesSpinner;
    private String[] communityName;
    private String[] communityId;
    private Button btnSubscribe;

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle drawerToggle;
    NavigationView nvDrawer;
    FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        SimpleProgressDialog dialog = SimpleProgressDialog.build(this, "loading...");
        dialog.show();
        Log.d("profileAct", "profileAct onCreate");
        instantiate();
        communitiesSpinner.setOnItemSelectedListener(this);
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkConnection.isNetworkAvailable(v.getContext())) {
                    subscribe(communitiesSpinner.getSelectedItemPosition());
                }
                else Toast.makeText(v.getContext(), R.string.networkUnavailable, Toast.LENGTH_LONG).show();
            }
        });

        if(NetworkConnection.isNetworkAvailable(this)) {
            client = RestApplication.getRestClient();
            client.getUserInfo(new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("profileAct", "profileAct callback");
                    super.onSuccess(statusCode, headers, response);
                    currentUser = User.fromJson(response);
                    //System.out.println("Response: " + response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.e("FAILED: ", errorResponse.toString());
                }
            });
        }
        else Toast.makeText(this, R.string.networkUnavailable, Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }



    private void instantiate() {
        if(NetworkConnection.isNetworkAvailable(this)) {
            communitiesSpinner = (Spinner) findViewById(R.id.spinnerCommunities);
            btnSubscribe = (Button) findViewById(R.id.btnSubscribe);
            populateSpinner();
        }
        else Toast.makeText(this, R.string.networkUnavailable, Toast.LENGTH_LONG).show();
    }

    private void populateSpinner() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Community");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> communityList, ParseException e) {
                if (e == null) {
                    Log.d("profileAct", "community Retrieved " + communityList.size() + " communities");
                    communityName = new String[communityList.size()];
                    communityId = new String[communityList.size()];
                    int i = 0;
                    for (ParseObject pO : communityList) {
                        communityName[i] = pO.getString("name");
                        communityId[i] = pO.getObjectId();
                        Log.i("profileAct", "com name " + pO.getString("name"));
                        i++;
                    }
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(ProfileActivity.this, R.layout.all_community, communityName);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAdapter.notifyDataSetChanged();
                    communitiesSpinner.setAdapter(spinnerAdapter);
                    Log.i("profileAct", "getting out of populate spinner");

                } else {
                    Log.d("profileAct", "Community Error: " + e.getMessage());
                }
            }
        });

    }

    public void subscribe(int position){
       if(communitiesSpinner.getSelectedItem() != null){

           //LocalUser localUser = new LocalUser();
           String objectId = LocalUser.getUser();

           //Saving Local subscription
           LocalSubscription localSubscription = new LocalSubscription(communityId[position]);
           localSubscription.saveLocalSubscription();

           Subscription subscription = new Subscription();
           subscription.subscribe(objectId,communityId[position]);

           //Update parse Channel for push notification
           ParseInstallation.getCurrentInstallation().saveInBackground();
           String channel = communityId[position];
           Log.d("CHANNEL",channel);
           ParsePush.subscribeInBackground(objectId);
           ParseAnalytics.trackAppOpenedInBackground(getIntent());

           //System.out.println("selected object id of community" + communityId[position]);
           Log.i("profileAct","objectId of User " + objectId.toString());
           // TODO: 11/8/15  move to a different activity here

           Intent i = new Intent(this, LandingActivity.class);
           startActivity(i);

       }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("profileAct", "onItemClick");
        if(NetworkConnection.isNetworkAvailable(this)) {
            subscribe(position);
        }
        else Toast.makeText(this, R.string.networkUnavailable, Toast.LENGTH_LONG).show();
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
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.subscribed_community:
                startActivity(new Intent(this, LandingActivity.class));
                break;
            case R.id.search_community:
                startActivity(new Intent(this, LandingActivity.class));
                break;
            case R.id.logout:
                if (NetworkConnection.isNetworkAvailable(this)) {
                    client.logout();
                }
                else Toast.makeText(this, R.string.networkUnavailable, Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
