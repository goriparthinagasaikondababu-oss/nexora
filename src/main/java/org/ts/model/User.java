package org.ts.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private String id;
    private String userName;
    private String email;
    private String fullName;
    private String password;
    private LocalDate joinedOn;

    public User(String id, String userName, String email, String fullName, String password) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getJoinedOn(){
        return this.joinedOn;
    }
    public void setJoinedOn(LocalDate joinedOn){
        this.joinedOn =joinedOn;
    }
}
