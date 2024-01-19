/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Date;

/**
 *
 * @author DELL
 */
public class User {
    private int userid;
    private String email;
    private String fullname;
    private String userphoto;
    private String password;
    private Date DateOfBirth ;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public User(int userid, String fullname, String userphoto) {
        this.userid = userid;
        this.fullname = fullname;
        this.userphoto = userphoto;
    }
     public User(  String email , String fullname , String password , Date date) {
        
        this.fullname = fullname;
       
        this.email = email ;
        this.password = password ;
        this.DateOfBirth = date ; 
        
    }
     
    public User(int userid) {
        this.userid = userid;
    }
    
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

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
