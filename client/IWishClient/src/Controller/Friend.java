/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author mennatallah
 */
public class Friend {
    private String profilePicture;
    private String fullName;
 
    Friend(String pp, String fName) {
        this.profilePicture = pp;
        this.fullName = fName;
    }
 
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String pp) {
         this.profilePicture = pp;
    }
        
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fName) {
        this.fullName = fName;
    }
}
