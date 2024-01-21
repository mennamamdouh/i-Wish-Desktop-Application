/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author attia
 */

public class WishListItem{
    public String image;
    public String name;
    public double price;
    public double progress;
    private int itemid;

    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public WishListItem(String image, String name, double price, double progress) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.progress = progress;
    }

}