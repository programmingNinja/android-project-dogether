package com.codepath.apps.DoGether.models;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.util.Log;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/**
 * Created by rshah4 on 11/4/15.
 */
@ParseClassName("User")
public class User extends ParseObject {

    private String id_str;
//    private String name;
//    private Date createdAt;
//    private String screen_name;
//    private String profile_image_url;


    public User() {
        super();
    }
    public void setUser(String objectId, String name, String screen_name, String profile_image_url) {
        this.id_str = objectId;
        setIdStr(objectId);
        setName(name);
        setScreenName(screen_name);
        setProfileImageURL(profile_image_url);
    }
    public void setName(String value) {
        this.put("name", value);
    }
    public void setScreenName(String value) {
        this.put("screen_name", value);
    }
    public void setProfileImageURL(String value) {
        this.put("profile_image_url", value);
    }

    public void setIdStr(String value) {
        this.put("id_str", value);
    }
    public static User fromJson(JSONObject jsonObject) {

        try {
            User u = new User();
            u.setUser(jsonObject.getString("id_str"), jsonObject.getString("name"), jsonObject.getString("screen_name"), jsonObject.getString("profile_image_url"));
            //u.saveInBackground();
            save(u);
            return u;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object

    }

    private static void save(final User u) {

        final ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo("id_str", u.id_str);
        query.findInBackground(new FindCallback<User>() {
            public void done(List<User> itemList, ParseException e) {
                if (e == null) {
                    if (itemList.size() <= 0) {
                        u.saveInBackground();
                        //System.out.println("saved user ");
                    }
                    //else System.out.println("duplicate user");
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

    public void setRelation(Subscription subscription) {

        ParseRelation relation = this.getRelation("subscriptions");
        relation.add(subscription);
        this.saveInBackground();
    }

    public ParseRelation<Subscription> getSubscriptions() {
        ParseRelation relation = this.getRelation("subscriptions");
        return relation;
    }



}
