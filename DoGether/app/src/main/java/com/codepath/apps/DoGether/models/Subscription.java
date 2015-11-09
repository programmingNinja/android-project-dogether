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
public class Subscription {

    public Subscription() {}

    public void subscribe(String userId, String communityId) {

        User u = User.getUser(userId);
        Community c = Community.getCommunityObj(communityId);
        u.setRelation(c);
        c.setUserRelation(u);
    }
}
