package com.kizlyak.internetshop.domain;

import com.kizlyak.internetshop.domain.enums.UserRole;

public class User extends BaseEntity implements Comparable<User> {

    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final UserRole role;

    public User(String username, String email, String firstName, String lastName, UserRole role) {
        this.username = username;
        this.email = email;
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