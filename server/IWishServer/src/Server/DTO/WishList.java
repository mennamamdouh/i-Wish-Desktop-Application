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

public class WishList {
    private int userid;
    private Item item;
    private String status;

    public WishList(int user, Item item, String status) {
        this.userid = user;
        this.item = item;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
