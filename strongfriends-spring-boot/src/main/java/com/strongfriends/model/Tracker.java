package com.strongfriends.model;

import java.util.Date;

public class Tracker {
    private int id;
    private int userId;
    private String originVideoName;
    private String originImgName;
    private int status;
    private String trackerVideoName;
    private String trackerImgName;
    private Date createdDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOriginVideoName() {
        return originVideoName;
    }

    public void setOriginVideoName(String originVideoName) {
        this.originVideoName = originVideoName;
    }

    public String getOriginImgName() {
        return originImgName;
    }

    public void setOriginImgName(String originImgName) {
        this.originImgName = originImgName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTrackerVideoName() {
        return trackerVideoName;
    }

    public void setTrackerVideoName(String trackerVideoName) {
        this.trackerVideoName = trackerVideoName;
    }

    public String getTrackerImgName() {
        return trackerImgName;
    }

    public void setTrackerImgName(String trackerImgName) {
        this.trackerImgName = trackerImgName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
