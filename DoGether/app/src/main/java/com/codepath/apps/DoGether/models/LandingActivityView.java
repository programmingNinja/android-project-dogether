package com.codepath.apps.DoGether.models;

import java.util.Date;

/**
 * Created by msamant on 11/7/15.
 */
public class LandingActivityView {
    String comUserUserName;
    String comUserEventText;
    String comUserUserPhoto;
    Date comCreateTime;

    public Date getComCreateTime() {
        return comCreateTime;
    }

    public void setComCreateTime(Date comCreateTime) {
        this.comCreateTime = comCreateTime;
    }

    public String getComUserUserName() {
        return comUserUserName;
    }

    public void setComUserUserName(String comUserUserName) {
        this.comUserUserName = comUserUserName;
    }

    public String getComUserEventText() {
        return comUserEventText;
    }

    public void setComUserEventText(String comUserEventText) {
        this.comUserEventText = comUserEventText;
    }

    public String getComUserUserPhoto() {
        return comUserUserPhoto;
    }

    public void setComUserUserPhoto(String comUserUserPhoto) {
        this.comUserUserPhoto = comUserUserPhoto;
    }
}
