/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DTO;

import java.sql.Blob;

/**
 *
 * @author DELL
 */
public class User {
    private int userid;
    private String email;
    private String fullname;
    private Blob userphoto;
    private String password;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Blob getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(Blob userphoto) {
        this.userphoto = userphoto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
