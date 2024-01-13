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

public class Item {
    
    private int itemid;
    private String itemname;
    private String itemphoto;
    private double price;

    public Item(int itemid, String itemname, String itemphoto, double price) {
        this.itemid = itemid;
        this.itemname = itemname;
        this.itemphoto = itemphoto;
        this.price = price;
    }
    
    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemphoto() {
        return itemphoto;
    }

    public void setItemphoto(String itemphoto) {
        this.itemphoto = itemphoto;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
}
