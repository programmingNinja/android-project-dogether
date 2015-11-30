package com.codepath.apps.DoGether.activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codepath.apps.DoGether.LocalModels.LocalEvent;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.RestApplication;
import com.codepath.apps.DoGether.TwitterClient;
import com.codepath.apps.DoGether.adapters.MyProfileAdapter;
import com.codepath.apps.DoGether.helpers.ImageUtility;
import com.codepath.apps.DoGether.helpers.SimpleItemTouchHelperCallback;
import com.codepath.apps.DoGether.models.Event;
import com.codepath.apps.DoGether.models.User;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private TwitterClient client;

    RecyclerView rv;
    List<Event> events;
    MyProfileAdapter adapter;
    ImageView profilePic;

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle drawerToggle;
    NavigationView nvDrawer;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Create an adapter
        adapter = new MyProfileAdapter(MyProfileActivity.this);

        // Find RecyclerView and bind to adapter
        rv = (RecyclerView) findViewById(R.id.rvEvents);

        // Bind adapter to list
        rv.setAdapter(adapter);

        // allows for optimizations
        rv.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(MyProfileActivity.this, 1);

        // Unlike ListView, you have to explicitly give a LayoutManager to the RecyclerView to position items on the screen.
        // There are three LayoutManager provided at the moment: GridLayoutManager, StaggeredGridLayoutManager and LinearLayoutManager.
        rv.setLayoutManager(layout);

        // get data
        String[] objectIds = new LocalEvent().getAll();
        events = Event.getEvents(objectIds);

        adapter.setEvents(events);

        adapter.notifyDataSetChanged();

        profilePic = (ImageView) findViewById(R.id.profilePic);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderWidthDp(1)
                .cornerRadiusDp(15)
                .oval(false)
                .build();

        Picasso.with(this).
                load(ImageUtility.getModifiedImageUrl(User.getProfilePicUrl(LocalUser.getUser()))).
                fit().
                placeholder(R.drawable.abc_spinner_mtrl_am_alpha).transform(transformation).
                into(profilePic);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);


        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setIcon(R.drawable.toolbaricon1);
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

        client = RestApplication.getRestClient();
        //instantiate();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.subscribed_community:
                System.out.println("subscribed clicked");
                startActivity(new Intent(this, LandingActivity.class));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



//    private void instantiate() {
//        String[] objectIds = new LocalEvent().getAll();
//        events = new ArrayList<Event>(Event.getEvents(objectIds));
//        rv = (RecyclerView)findViewById(R.id.rvEvents);
//        rv.setHasFixedSize(true);
//        // Create an adapter
//        adapter = new MyProfileAdapter(MyProfileActivity.this, events);
//        // Define 1 column grid layout
//        final GridLayoutManager layout = new GridLayoutManager(MyProfileActivity.this, 1);
//
//        // Unlike ListView, you have to explicitly give a LayoutManager to the RecyclerView to position items on the screen.
//        // There are three LayoutManager provided at the moment: GridLayoutManager, StaggeredGridLayoutManager and LinearLayoutManager.
//        rv.setLayoutManager(layout);
//        rv.setAdapter(adapter);
//        adapter = new MyProfileAdapter(MyProfileActivity.this, events);
//        adapter.notifyDataSetChanged();
//
//    }
}
