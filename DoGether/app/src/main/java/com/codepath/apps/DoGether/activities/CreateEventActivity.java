package com.codepath.apps.DoGether.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codepath.apps.DoGether.LocalModels.LocalSubscription;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.codepath.apps.DoGether.models.Community;
import com.codepath.apps.DoGether.models.Event;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.models.User;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseRelation;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateEventActivity extends ActionBarActivity  {

    private Spinner spEventExercise;
    private Spinner spEventExerciseType;
    private TimePicker timePicker;
    private Button broadcastEvent;
    private String userId;
    private String communityId;
    private List<User>userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setUpViews();
        communityId = LocalSubscription.getCommunity();
        userId = LocalUser.getUser();
        broadcastEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                broadcastEventToUsers(communityId);
            }
        });
        spEventExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spEventExercise.getSelectedItem().equals("Upper body workout")) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CreateEventActivity.this, R.array.ubw, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spEventExerciseType.setAdapter(adapter);

                }
                else if (spEventExercise.getSelectedItem().equals("Lower body workout")) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CreateEventActivity.this, R.array.lbw, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spEventExerciseType.setAdapter(adapter);

                }
                else if  (spEventExercise.getSelectedItem().equals("Cardio")) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CreateEventActivity.this, R.array.other, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spEventExerciseType.setAdapter(adapter);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setUpViews(){
        spEventExercise = (Spinner)findViewById(R.id.spEventExercise);
        spEventExerciseType =(Spinner)findViewById(R.id.spEventExerciseType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Exercise, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEventExercise.setAdapter(adapter);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        broadcastEvent = (Button)findViewById(R.id.btnBroadcast);
        // Apply the adapter to the spinner

    }

    public void broadcastEventToUsers (String comId) {
        Community currentCom = Community.getCommunityObj(comId);
        ParseRelation<User> communityParseRelation = currentCom.getRelation("userRelation");
        communityParseRelation.getQuery().findInBackground(new FindCallback<User>() {
            public void done(List<User> results, ParseException e) {
                if (e == null) {
                    userList = new ArrayList<User>();
                    for(User user : results){
                        if(!userId.equals(user.getObjectId().toString())){
                            userList.add(user);
                        }
                    }
                    if(userList !=null && userList.size()>0) {
                        Toast.makeText(CreateEventActivity.this, "HELLO", Toast.LENGTH_LONG).show();
                        //Form Event Text
                        String eventText = formEventText();
                        //Enter data to database
                        saveEventToParseDb(eventText);
                        //Enter data for Push Notification
                        broadcast(eventText.toString());
                        //Go to CommunityView
                        Intent i = new Intent(CreateEventActivity.this, LandingActivity.class);
                        startActivity(i);
                    }
                    // access your user list here

                } else {
                    // results have all the developer objects in the Icon
                }
            }
        });

    }

    public void removeFromUserList(){
        for(int j=0;j<userList.size();j++){
            if(userId.equals(userList.get(j).getObjectId().toString())){
                userList.remove(j);
            }
        }

    }

    private void saveEventToParseDb(String eventText){
        try{
            Event event = new Event(eventText,communityId);
            event.saveEvent();
            event.setUserRelation();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private String formEventText(){
        StringBuffer eventText = new StringBuffer();
        eventText.append("Hi all, I am going to do"+" ");
        eventText.append("workout:" +" ");
        //eventText.append(etExerciseEvent.getText().toString()+ " ");
        eventText.append("type:" +" ");
        //eventText.append(etExerciseType.getText().toString() +" ");
        eventText.append("at" +" ");
        //using deprecated api to support lower version android devices
        eventText.append(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
        return eventText.toString();
    }

    public void broadcast(String eventText){
        ParsePush push = new ParsePush();
        LinkedList<String> channels = new LinkedList<String>();
        for(User user : userList){
            channels.add(user.getObjectId().toString());
        }
        push.setChannels(channels); // Notice we use setChannels not setChannel
        push.setMessage(eventText);
        push.sendInBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
