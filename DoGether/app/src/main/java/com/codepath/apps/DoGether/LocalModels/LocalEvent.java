package com.codepath.apps.DoGether.LocalModels;

import android.content.Context;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

//import com.orm.SugarRecord;

/**
 * Created by rshah4 on 11/7/15.
 */
public class LocalEvent extends SugarRecord<LocalEvent> {
    String id;

    LocalEvent() {}
    LocalEvent(String id) {
        this.id = id;
    }
    public void saveLocalEvent() {
        this.save();
    }

    public String getById(String id) {
        return LocalEvent.findById(LocalEvent.class, Long.parseLong(id)).id;
    }

    public String[] getAll() {
        List<LocalEvent> list = listAll(LocalEvent.class);
        String[] result = new String[list.size()];
        for (int i=0 ; i<list.size() ; i++) {
            result[i] = list.get(i).id;
        }
        return result;
    }

    public void deleteLocalEvent(String id) {
        LocalEvent.deleteAll(LocalEvent.class, "id = ?", id);
    }

}
