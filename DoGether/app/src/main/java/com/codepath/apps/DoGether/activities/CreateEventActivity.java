package com.codepath.apps.DoGether.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codepath.apps.DoGether.R;

public class CreateEventActivity extends ActionBarActivity {

    private EditText etExerciseEvent;
    private EditText etExerciseType;
    private TimePicker timePicker;
    private Button broadcastEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setUpViews();
        broadcastEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Toast.makeText(CreateEventActivity.this, "HELLO", Toast.LENGTH_LONG).show();

                //Form Event Text

                //Enter data to database

                //Enter data for Push Notification

                //Go to CommunityView activity
            }
        });
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
