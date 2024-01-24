/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DTO;

/**
 *
 * @author DELL
 */
public class Notification {
    private int notificationid;
    private int userid;
    private int friendid;
    private int itemid;
    private boolean seen;
    private String notificationtype;
    private String notification;

    public Notification(int notificationid, int userid, int friendid, int itemid, boolean seen, String notificationtype, String notification) {
        this.notificationid = notificationid;
        this.userid = userid;
        this.friendid = friendid;
        this.itemid = itemid;
        this.seen = seen;
        this.notificationtype = notificationtype;
        this.notification = notification;
    }

    public int getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(int notificationid) {
        this.notificationid = notificationid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getFriendid() {
        return friendid;
    }

    public void setFriendid(int friendid) {
        this.friendid = friendid;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getNotificationtype() {
        return notificationtype;
    }

    public void setNotificationtype(String notificationtype) {
        this.notificationtype = notificationtype;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    
    
}
