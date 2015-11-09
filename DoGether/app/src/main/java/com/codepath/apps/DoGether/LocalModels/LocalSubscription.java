package com.codepath.apps.DoGether.LocalModels;


import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by rshah4 on 11/7/15.
 */
public class LocalSubscription extends SugarRecord<LocalSubscription> {
    String objId;
    public LocalSubscription() {}
    public LocalSubscription(String id) {
        this.objId = id;
    }
    public void saveLocalSubscription() {
        List<LocalSubscription> localSubscriptions = LocalSubscription.find(LocalSubscription.class, "obj_id = ?", this.objId);
        if(localSubscriptions.size() <= 0)
            this.save();
    }

    public String getById(String id) {
        return LocalSubscription.findById(LocalSubscription.class, Long.parseLong(id)).objId;
    }

    public static String[] getAll() {
        List<LocalSubscription> list = listAll(LocalSubscription.class);
        String[] result = new String[list.size()];
        for (int i=0 ; i<list.size() ; i++) {
            result[i] = list.get(i).objId;
        }
        return result;
    }

    public void deleteLocalSubscription(String id) {
        LocalSubscription.deleteAll(LocalSubscription.class, "id = ?", id);
    }
}
