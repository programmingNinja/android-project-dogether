package com.codepath.apps.DoGether.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.DoGether.activities.NotificationClickActivity;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by msamant on 11/22/15.
 */
public class NotificationProcessor extends ParsePushBroadcastReceiver {


    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
        JSONObject pushData = null;
        try {
            Toast.makeText(context,"HELLO NOTIFIER",Toast.LENGTH_LONG).show();
            pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            String userId = pushData.getString("userId");
            String message = pushData.getString("alert");
            Intent i = new Intent();
            i.setClass(context,NotificationClickActivity.class);
            i.putExtra("userId",userId);
            i.putExtra("userMsg",message);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

            Log.d("PUSHDATA", pushData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}