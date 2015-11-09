package com.codepath.apps.DoGether.models;

import com.codepath.apps.DoGether.LocalModels.LocalEvent;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

/**
 * Created by rshah4 on 11/4/15.
 */

@ParseClassName("Community")
public class Community extends ParseObject  {

    public Community() {}


    public Community(String name, ParseGeoPoint location) {

        setName(name);
        setMemberCount(0);
        setLocation(location);
        this.saveInBackground();

    }

    public void setName(String name) {
        this.put("name", name);
    }
    public void setMemberCount(int count) {
        this.put("member_count", count);
    }
    public void setLocation(ParseGeoPoint location) {
        this.put("location", location);
    }

    public void subscribe(String userId, String communityId) {
        Subscription subscription = new Subscription();
        subscription.subscribe(userId, communityId);
    }


    public static Community getCommunityObj(String id) {
        ParseQuery<Community> queryForCommunity = ParseQuery.getQuery(Community.class);

        Community u = new Community();
        try {
            u =  queryForCommunity.get(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return u;
    }

    public void setUserRelation(User u) {
        ParseRelation relation = this.getRelation("userRelation");
        relation.add(u);
        this.saveInBackground();
    }
    public void saveCommunity() {
        try {
            this.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
