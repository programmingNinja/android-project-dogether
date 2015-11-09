package com.codepath.apps.DoGether.LocalModels;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by rshah4 on 11/8/15.
 */
public class LocalUser extends SugarRecord<LocalUser> {

    private String objId;

    public LocalUser() {}
    public LocalUser(String id) {
        this.objId = id;
    }
    public void saveLocalUser() {
        this.save();
    }

    public static String getUser() {
        LocalUser localUser = LocalUser.findById(LocalUser.class, 1L);
        return localUser.objId;
    }

    public static long getCount() {
        return LocalUser.count(LocalUser.class,"",null);
    }


}
