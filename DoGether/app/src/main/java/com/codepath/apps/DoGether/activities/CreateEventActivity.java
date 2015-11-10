package com.codepath.apps.DoGether.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CreateEventActivity extends ActionBarActivity {

    private EditText etExerciseEvent;
    private EditText etExerciseType;
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
        getAllUsers(communityId);

        broadcastEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

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
                
            }
        });

    }

    public void setUpViews(){
        etExerciseEvent = (EditText)findViewById(R.id.etEventExercise);
        etExerciseType = (EditText)findViewById(R.id.etEventType);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        broadcastEvent = (Button)findViewById(R.id.btnBroadcast);
        broadcastEvent.setEnabled(false);
    }

    public void getAllUsers(String comId) {
        Community currentCom = Community.getCommunityObj(comId);
        ParseRelation<User> communityParseRelation = currentCom.getRelation("userRelation");
        communityParseRelation.getQuery().findInBackground(new FindCallback<User>() {
            public void done(List<User> results, ParseException e) {
                if (e == null) {
                    broadcastEvent.setEnabled(true);
                    userList = new ArrayList<User>();
                    for(User user : results){
                        if(userId.equals(user.getObjectId().toString())){
                            userList.remove(user);
                        }
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
        eventText.append(etExerciseEvent.getText().toString()+ " ");
        eventText.append("type:" +" ");
        eventText.append(etExerciseType.getText().toString() +" ");
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
