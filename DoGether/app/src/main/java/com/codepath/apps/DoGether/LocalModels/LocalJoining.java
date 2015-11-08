package com.codepath.apps.DoGether.LocalModels;

//import com.orm.SugarRecord;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by rshah4 on 11/7/15.
 */
public class LocalJoining extends SugarRecord<LocalJoining> {
    String id;
    LocalJoining() {}
    LocalJoining(String id) {
        this.id = id;
    }
    public void saveLocalJoining() {
        this.save();
    }

    public String getById(String id) {
        return LocalJoining.findById(LocalJoining.class, Long.parseLong(id)).id;
    }

    public String[] getAll() {
        List<LocalJoining> list = listAll(LocalJoining.class);
        String[] result = new String[list.size()];
        for (int i=0 ; i<list.size() ; i++) {
            result[i] = list.get(i).id;
        }
        return result;
    }

    public void deleteLocalJoining(String id) {
        LocalJoining.deleteAll(LocalJoining.class, "id = ?", id);
    }
}
