package com.codepath.apps.DoGether.models;

import com.codepath.apps.DoGether.LocalModels.LocalEvent;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

/**
 * Created by rshah4 on 11/4/15.
 */
@ParseClassName("Event")
public class Event extends ParseObject {

    private String text;

    public Event(){}
    public Event(String text, String comId) {
        setCommunityId(comId);
        setText(text);

        // needs to be tested, dont know if this will work. Alternative for setUserRelation method
//        ParseRelation parseRelation = this.getRelation("fromUser");
//        ParseQuery<User> queryForUser = ParseQuery.getQuery(User.class);
//        User userFromParse = null;
//        try {
//            userFromParse = queryForUser.get(LocalUser.getUser());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        parseRelation.add(userFromParse);
    }

    public void saveEvent() {
        try {
            this.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    // buggy  // TODO: 11/8/15 Fix this method
//    public Event[] getByCommunityId(String userId, String comId) {
//
//        final ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
//        query.whereEqualTo("communityId", comId);
//        query.whereNotEqualTo("userId", userId);
//        Event[] resultArr;
//        query.findInBackground(new FindCallback<Event>() {
//            public void done(List<Event> results, ParseException e) {
//                // results has the list of users with a hometown team with a winning record
//                //resultArr = results.toArray();
//            }
//        });
//
//    }

    public void setCommunityId(String communityId) {
        this.put("communityId", communityId);
    }

    public void setUserRelation() {

        // get current users objId from localdatastore
        String currentUserObjId = LocalUser.getUser();

        //access the user object from parse using the objId
        ParseQuery<User> queryForUser = ParseQuery.getQuery(User.class);
        ParseQuery<Event> queryForEvent = ParseQuery.getQuery(Event.class);
        try {
            final User userFromParse = queryForUser.get(currentUserObjId);
            queryForEvent.getInBackground(this.getObjectId(), new GetCallback<Event>() {
                public void done(Event event, ParseException e) {
                    if (e == null) {
                        LocalEvent localEvent = new LocalEvent(event.getObjectId());
                        localEvent.save();
                        ParseRelation relation = event.getRelation("fromUser");
                        System.out.println("relation=" + relation.toString());
                        relation.add(userFromParse);
                        event.saveInBackground();
                    } else {
                        // something went wrong
                    }
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void setText(String text) {
        this.put("text", text);
    }
}
