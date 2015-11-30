package com.codepath.apps.DoGether.models;

import com.activeandroid.util.Log;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by rshah4 on 11/4/15.
 */
@ParseClassName("Joining")
public class Joining extends ParseObject  {

    public Joining() {super();}

    public void createJoining(String eventId){
        this.put("eventId", eventId);
        this.saveInBackground();
    }

    public static Joining getJoining(String eventId) {
        ParseQuery<Joining> getJoining = ParseQuery.getQuery(Joining.class);
        getJoining.whereEqualTo("eventId", eventId);
        Joining j = new Joining();
        try {
            List<Joining> joinings = getJoining.find();
            if(joinings.size() > 0)
                j = joinings.get(0);
            else j = null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return j ;
    }

    public void setRelation(User u) {
        ParseRelation relation = this.getRelation("users");
        relation.add(u);
        this.saveInBackground();
    }
    public static void setJoiningUser(String eventId, final String userId) {
        ParseQuery<Joining> getJoining = ParseQuery.getQuery(Joining.class);
        getJoining.whereEqualTo("eventId", eventId);

        getJoining.findInBackground(new FindCallback<Joining>() {
            public void done(List<Joining> itemList, ParseException e) {
                if (e == null) {
                    if (itemList.size() > 0) {
                        Joining j = itemList.get(0);
                        User u = User.getUser(userId);
                        ParseRelation relation = j.getRelation("users");
                        relation.add(u);
                        j.saveInBackground();
                    }
                } else {
                    //Log.d("item", "Error: " + e.getMessage());
                    Log.e("User", "Error in saving " + e.getMessage());
                }
            }
        });
    }
}
