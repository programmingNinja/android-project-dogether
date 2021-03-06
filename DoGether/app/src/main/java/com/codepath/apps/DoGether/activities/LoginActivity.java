package com.codepath.apps.DoGether.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.DoGether.LocalModels.LocalSubscription;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.TwitterClient;
import com.codepath.apps.DoGether.helpers.NetworkConnection;
import com.codepath.oauth.OAuthLoginActionBarActivity;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("loginAct","loginAct onCreate");
		setContentView(R.layout.activity_login);
		//ParseTwitterUtils.initialize("fjzW8u4XxQC2oj973r7m2oYhK", "T0anSzwnOjNX2KP6Xw7D5EhhWvewfVQKmMtdgP2qvoZkTDdJd6");
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		if (LocalSubscription.getAll().length <= 0) {
			Intent i = new Intent(this, ProfileActivity.class);
			startActivity(i);
		} else {
			Intent i = new Intent(this, LandingActivity.class);
			startActivity(i);
			//card transition
			overridePendingTransition(R.anim.from_middle, R.anim.to_middle);

//			Intent i = new Intent(this, MyProfileActivity.class);
//			startActivity(i);
		}

	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		if (NetworkConnection.isNetworkAvailable(this)) {
			getClient().connect();
		} else Toast.makeText(this, R.string.networkUnavailable, Toast.LENGTH_LONG).show();


		/*ParseTwitterUtils.logIn(this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				if (user == null) {
					Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
				} else if (user.isNew()) {
					Log.d("MyApp", "User signed up and logged in through Twitter!");
				} else {
					Log.d("MyApp", "User logged in through Twitter!");
				}
			}
		});

		if (!ParseTwitterUtils.isLinked(user)) {
			  ParseTwitterUtils.link(user, this, new SaveCallback() {
				@Override
				public void done(ParseException ex) {
				  if (ParseTwitterUtils.isLinked(user)) {
					Log.d("MyApp", "Woohoo, user logged in with Twitter!");
				  }
				}
			  });
			}*/
	}

}
