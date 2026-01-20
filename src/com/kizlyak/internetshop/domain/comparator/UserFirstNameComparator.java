package com.kizlyak.internetshop.domain.comparator;

import com.kizlyak.internetshop.domain.User;
import java.util.Comparator;

public class UserFirstNameComparator implements Comparator<User> {

    @Override
    public int compare(User u1, User u2) {
        if (u1.getFirstName() == null || u2.getFirstName() == null) {
            return 0;
        }
        return u1.getFirstName().compareToIgnoreCase(u2.getFirstName());
    }
}