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
        System.out.println("userid=" + userId + "comid=" + communityId);
        sub.addAllUnique("community_ids", Arrays.asList(communityId));
        try {
            sub.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // testing new database schema
//        User u = User.getUser(userId);
//        Community c = Community.getCommunityObj(communityId);
//        u.setRelation(c);
//        c.setUserRelation(u);

        /**
         * If we eliminate the Subscription table from parse and local altogether
         * ParseQuery<Community> query = new ParseQuery(Community.class)
         * Community com = query.get(communityId);
         */

        // create relationship in the user table for this subscription
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        // Specify the object id
        query.getInBackground(userId, new GetCallback<User>() {
            public void done(User user, ParseException e) {
                if (e == null) {
                    ParseRelation relation = user.getRelation("subscriptions");
                    System.out.println("relation="+relation.toString());
                    relation.add(sub);
                    user.saveInBackground();

                    /**
                     * If we eliminate the Subscription table from parse and local altogether
                     * ParseRelation relation = user.getRelation("subscriptions");
                     * relation.add(com);
                     * user.saveInBackground();
                     */
                } else {
                    // something went wrong
                }
            }
        });
    }
}
