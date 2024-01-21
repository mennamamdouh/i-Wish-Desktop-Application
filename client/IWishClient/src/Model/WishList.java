/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author DELL
 */

public class WishList {
    private int userid;
    private Item item;
    private double progress;
    private double remaining;

    public WishList(int userid, Item item, double progress, double remaining) {
        this.userid = userid;
        this.item = item;
        this.progress = progress;
        this.remaining = remaining;
    }



    public int getUser() {
        return userid;
    }

    public void setUser(int user) {
        this.userid = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }


    public int getUserid() {
        return userid;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
    
}