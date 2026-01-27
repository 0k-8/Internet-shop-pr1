package com.kizlyak.internetshop.domain.model;

import com.kizlyak.internetshop.domain.model.enums.UserRole;

public class User extends BaseEntity implements Comparable<User>, Entity {

    private final String username;
    private final String email;
    private final String lastName;
    private final UserRole role;
    private String password;
    private String firstName;

    public User(String username, String email, String password, String firstName, String lastName,
          UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRole getRole() {
        return role;
    }


    @Override
    public int compareTo(User o) {
        if (this.lastName == null) {
            return -1;
        }
        return this.lastName.compareTo(o.lastName);
    }
}