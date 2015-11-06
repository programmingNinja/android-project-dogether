package com.codepath.apps.DoGether.models;

import android.text.style.SubscriptSpan;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.Arrays;

/**
 * Created by rshah4 on 11/4/15.
 */
@ParseClassName("Subscription")
public class Subscription extends ParseObject {

    public Subscription() {}

    public void subscribe(String userId, String communityId) {

        // save community ids which user subscribes to
        final Subscription sub = new Subscription();
        sub.addAllUnique("community_ids", Arrays.asList(communityId));
        sub.saveInBackground();

        // create relationship in the user table for this subscription
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        // Specify the object id
        query.getInBackground(userId, new GetCallback<User>() {
            public void done(User user, ParseException e) {
                if (e == null) {
                    user.setRelation(sub);
                } else {
                    // something went wrong
                }
            }
        });
    }
}
