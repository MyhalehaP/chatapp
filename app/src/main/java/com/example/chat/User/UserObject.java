package com.example.chat.User;

import java.io.Serializable;

public class UserObject implements Serializable {
    private String name,phone,uid, notificationKey;

    private boolean selected = false;
    public UserObject(String uid){
        this.uid = uid;
    }

    public UserObject(String uid,String name, String phone){
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }

    public String getUid(){ return uid; }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public void setName(String name) {
        this.name = name;
    }
}

