package com.codepath.apps.DoGether.helpers;

/**
 * Created by msamant on 11/24/15.
 */
public class ImageUtility {

 public static String getModifiedImageUrl(String url){
     url = url.replaceAll("_normal","");
     return url;
 }
}
