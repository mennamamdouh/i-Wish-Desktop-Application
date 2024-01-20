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
public class Contribution {
    private User friend ;
    private double amount;

    public Contribution(User friend, double amount) {
        this.friend = friend;
        this.amount = amount;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
}
