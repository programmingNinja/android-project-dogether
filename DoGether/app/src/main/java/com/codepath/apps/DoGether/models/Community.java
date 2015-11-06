package com.codepath.apps.DoGether.models;

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

    public void getCommunity(ParseRelation<Subscription> parseRelation) {
        ParseQuery query = parseRelation.getQuery();
        //execute the above query
    }

    public void getCommunity(String id) {
        final ParseQuery<Community> query = ParseQuery.getQuery(Community.class);
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<Community>() {
            public void done(List<Community> communityList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    //String firstItemId = itemList.get(0).getObjectId();
                    //Toast.makeText(TodoItemsActivity.this, firstItemId, Toast.LENGTH_SHORT).show();
                } else {
                    //Log.d("item", "Error: " + e.getMessage());
                    System.out.println("Error in saving "+e.getMessage());
                }
            }
        });
    }
}
