package com.kizlyak.internetshop.service.impl;

import com.kizlyak.internetshop.domain.User;
import java.util.ArrayList;
import java.util.List;

public class UserCollection {

    private final List<User> users = new ArrayList<>();

    public void add(User user) {
        users.add(user);
    }

    public List<User> getAll() {
        return users;
    }
}