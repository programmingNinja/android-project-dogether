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
import com.codepath.apps.DoGether.models.Event;
import com.codepath.apps.DoGether.R;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

import java.util.LinkedList;

public class CreateEventActivity extends ActionBarActivity {

    private EditText etExerciseEvent;
    private EditText etExerciseType;
    private TimePicker timePicker;
    private Button broadcastEvent;
    private String userId;
    private String communityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setUpViews();
        communityId = LocalSubscription.getCommunity();
        broadcastEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               Toast.makeText(CreateEventActivity.this, "HELLO", Toast.LENGTH_LONG).show();
                //Form Event Text
                String eventText = formEventText();
                //Enter data to database
                saveEventToParseDb(eventText);
                //Enter data for Push Notification
                broadcast(communityId,eventText.toString());
                //Go to CommunityView
                Intent i = new Intent(CreateEventActivity.this, LandingActivity.class);
                startActivity(i);
            }
        });
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

    public void broadcast(String communityId, String eventText){
        ParsePush push = new ParsePush();
        push.setChannel(communityId); // Notice we use setChannels not setChannel
        push.setMessage(eventText);
        push.sendInBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    public void setUpViews(){
        etExerciseEvent = (EditText)findViewById(R.id.etEventExercise);
        etExerciseType = (EditText)findViewById(R.id.etEventType);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        broadcastEvent = (Button)findViewById(R.id.btnBroadcast);
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
