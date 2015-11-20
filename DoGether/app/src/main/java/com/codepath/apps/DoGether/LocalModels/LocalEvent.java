package com.codepath.apps.DoGether.LocalModels;

import com.codepath.apps.DoGether.models.Event;
import com.orm.SugarRecord;

import java.util.List;


/**
 * Created by rshah4 on 11/7/15.
 */
public class LocalEvent extends SugarRecord<LocalEvent> {
    String objId;

    public LocalEvent() {}
    public LocalEvent(String id) {
        this.objId = id;
    }
    public void saveLocalEvent() {
        this.save();
    }

    public String getById(String id) {
        return LocalEvent.findById(LocalEvent.class, Long.parseLong(id)).objId.toString();
    }

    public String[] getAll() {
        List<LocalEvent> list = listAll(LocalEvent.class);
        String[] result = new String[list.size()];
        for (int i=0 ; i<list.size() ; i++) {
            result[i] = list.get(i).objId.toString();
        }
        return result;
    }

    public static void deleteLocalEvent(String id) {
        LocalEvent.deleteAll(LocalEvent.class, "obj_id = ?", id);
    }

    public static void deleteAll(){
        LocalEvent.deleteAll(LocalEvent.class);
    }
}
