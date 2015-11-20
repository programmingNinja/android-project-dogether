package com.codepath.apps.DoGether.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.widget.ImageView;

import com.codepath.apps.DoGether.LocalModels.LocalEvent;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.adapters.MyProfileAdapter;
import com.codepath.apps.DoGether.helpers.SimpleItemTouchHelperCallback;
import com.codepath.apps.DoGether.models.Event;
import com.codepath.apps.DoGether.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    RecyclerView rv;
    List<Event> events;
    MyProfileAdapter adapter;
    ImageView profilePic;
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
        Picasso.with(this).
                load(User.getProfilePicUrl(LocalUser.getUser())).
                fit().
                placeholder(R.drawable.abc_spinner_mtrl_am_alpha).
                into(profilePic);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
        //instantiate();
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
