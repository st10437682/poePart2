// Login.java
package com.mycompany.poepartone;

public class Login {
    private String username;
    private String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean loginUser(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public String returnLoginStatus(String username, String password) {
        return loginUser(username, password) ?
                "Welcome " + username + ", it is great to see you again." :
                "Username or password incorrect, please try again.";
    }
}
