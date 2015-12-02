package com.codepath.apps.DoGether.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rshah4 on 12/2/15.
 */
public class NetworkConnection {
    public static boolean isNetworkAvailable(Context cxt) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
