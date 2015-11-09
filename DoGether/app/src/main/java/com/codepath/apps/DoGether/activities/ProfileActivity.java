package com.codepath.apps.DoGether.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.codepath.apps.DoGether.LocalModels.LocalSubscription;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.RestApplication;
import com.codepath.apps.DoGether.TwitterClient;
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

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private TwitterClient client;
    private User currentUser;
    private Spinner communitiesSpinner;
    private String[] communityName;
    private String[] communityId;
    private Button btnSubscribe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("profileAct", "profileAct onCreate");
        setContentView(R.layout.activity_profile);
        instantiate();
        communitiesSpinner.setOnItemSelectedListener(this);
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe(communitiesSpinner.getSelectedItemPosition());
            }
        });

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
                System.out.println("FAILED: " + errorResponse);
            }
        });
    }



    private void instantiate() {
        communitiesSpinner = (Spinner)findViewById(R.id.spinnerCommunities);
        btnSubscribe = (Button)findViewById(R.id.btnSubscribe);
        populateSpinner();
    }

    private void populateSpinner() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Community");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> communityList, ParseException e) {
                if (e == null) {
                    Log.d("community", "Retrieved " + communityList.size() + " communities");
                    communityName = new String[communityList.size()];
                    communityId = new String[communityList.size()];
                    int i=0;
                    for(ParseObject pO : communityList) {
                        communityName[i] = pO.getString("name");
                        communityId[i] = pO.getObjectId();
                        System.out.println("com name "+pO.getString("name"));
                        i++;
                    }

                    ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<String>(ProfileActivity.this, R.layout.all_community, communityName);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAdapter.notifyDataSetChanged();
                    communitiesSpinner.setAdapter(spinnerAdapter);
                    Log.d("profileAct", "getting out of populate spinner");

                } else {
                    Log.d("community", "Error: " + e.getMessage());
                }
            }
        });

    }

    public void subscribe(int position){
       if(communitiesSpinner.getSelectedItem() != null){

           //LocalUser localUser = new LocalUser();
           String objectId = LocalUser.getUser();

           // update local
           LocalSubscription localSubscription = new LocalSubscription(communityId[position]);
           localSubscription.saveLocalSubscription();

           //update parse DB
           Subscription subscription = new Subscription();
           subscription.subscribe(objectId,communityId[position]);

           //Update parse Channel for push notification
           ParseInstallation.getCurrentInstallation().saveInBackground();
           ParsePush.subscribeInBackground(communityId[position]);
           ParseAnalytics.trackAppOpenedInBackground(getIntent());

           //System.out.println("selected object id of community" + communityId[position]);
           System.out.println("selected object id of user " + objectId);
           // TODO: 11/8/15  move to a different activity here

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
        System.out.println("profileAct onItemClick");
        subscribe(position);
    }
}
