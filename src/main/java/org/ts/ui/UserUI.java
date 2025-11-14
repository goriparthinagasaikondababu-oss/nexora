package org.ts.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserUI {
    private String id;
    private String userName;
    private String email;
    private String fullName;
    private LocalDate joinedOn;

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

    public UserUI setId(String id) {
        this.id = id;
        return this;
    }

    public UserUI setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserUI setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserUI setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public LocalDate getJoinedOn() {
        return joinedOn;
    }

    public UserUI setJoinedOn(LocalDate joinedOn) {
        this.joinedOn = joinedOn;
        return this;
    }

    public String toString(){
        String usernameLabel = String.format("%-15s", "Username");
        String fullnameLabel = String.format("%-15s", "Full Name");
        String emailLabel = String.format("%-15s", "Email");
        String joinedonLabel = String.format("%-15s", "Joined On");
        return String.format("%s: %s\n%s: %s\n%s: %s\n%s: %s",usernameLabel, userName, fullnameLabel, fullName, emailLabel, email, joinedonLabel, joinedOn);
    }
}
