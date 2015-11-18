package com.codepath.apps.DoGether;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.codepath.apps.DoGether.LocalModels.LocalEvent;
import com.codepath.apps.DoGether.LocalModels.LocalSubscription;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.codepath.apps.DoGether.models.Community;
import com.codepath.apps.DoGether.models.Event;
import com.codepath.apps.DoGether.models.Subscription;
import com.codepath.apps.DoGether.models.User;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     RestClient client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
//com.activeandroid.app.Application
public class RestApplication extends com.orm.SugarApp {
	private static Context context;
	public static final String YOUR_APPLICATION_ID = "1bq93Tp6jYvzToqSS6vdJ6PJrn3ivr32lixRhO7v";
	public static final String YOUR_CLIENT_KEY = "nOUO3ZCDKpbc6nJmcJJF0tGdEArNMzN3hF9z9cMZ";

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d("restApp", "RestApp onCreate");
		RestApplication.context = this;
		//Parse.enableLocalDatastore(this);
		ParseObject.registerSubclass(User.class);
		ParseObject.registerSubclass(Event.class);
		ParseObject.registerSubclass(Community.class);
		Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

		// community records added hence commenting out
		//insertIntoCommunities();
//		LocalUser.deleteAll();
//        LocalSubscription.deleteAll();
//        LocalEvent.deleteAll();

		// testing new database schema
//		User u = User.getUser(LocalUser.getUser());
//		ParseRelation<Community> parseRelation1 = u.getRelation("comRelation");
//		try {
//			Community test = parseRelation1.getQuery().find().get(0);
//			ParseRelation<User> userParseRelation = test.getRelation("userRelation");
//			User t = userParseRelation.getQuery().find().get(0);
//			System.out.println("community Test "+test.getString("name"));
//			System.out.println("getting relation "+userParseRelation);
//			System.out.println("user test " + t.getString("screen_name"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

	}

	private void insertIntoCommunities() {
		Community[] communitites = new Community[5];
		for(int i=0 ; i<5 ; i++) {
			Community c = new Community("Com"+i, new ParseGeoPoint(new Double(10.0), new Double(10.0)));
			try {
				c.save();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, RestApplication.context);
	}
}