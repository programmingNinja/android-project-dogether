package com.codepath.apps.DoGether;

import android.app.Application;
import android.content.Context;

import com.codepath.apps.DoGether.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

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
public class RestApplication extends Application {
	private static Context context;
	public static final String YOUR_APPLICATION_ID = "1bq93Tp6jYvzToqSS6vdJ6PJrn3ivr32lixRhO7v";
	public static final String YOUR_CLIENT_KEY = "nOUO3ZCDKpbc6nJmcJJF0tGdEArNMzN3hF9z9cMZ";

	@Override
	public void onCreate() {
		super.onCreate();
		RestApplication.context = this;
		//Parse.enableLocalDatastore(this);
		ParseObject.registerSubclass(User.class);
		Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, RestApplication.context);
	}
}