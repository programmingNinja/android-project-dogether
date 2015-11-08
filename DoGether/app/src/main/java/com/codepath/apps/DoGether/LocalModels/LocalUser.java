package com.codepath.apps.DoGether.LocalModels;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by rshah4 on 11/8/15.
 */
public class LocalUser extends SugarRecord<LocalUser> {

    String id;

    public LocalUser() {}
    public LocalUser(String id) {
        this.id = id;
    }
    public void saveLocalUser() {
        this.save();
    }

    public String getUser() {
        return LocalUser.findById(LocalUser.class, Long.parseLong(id)).id;
    }

    public static long getCount() {
        return LocalUser.count(LocalUser.class,"",null);
    }


}
