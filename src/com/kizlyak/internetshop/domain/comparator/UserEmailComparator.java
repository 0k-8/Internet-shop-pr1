package com.kizlyak.internetshop.domain.comparator;

import com.kizlyak.internetshop.domain.User;
import java.util.Comparator;

public abstract class UserEmailComparator implements Comparator<User> {

    @Override
    public int compare(User u1, User u2) {
        if (u1.getEmail() == null || u2.getEmail() == null) {
            return 0;
        }
        return u1.getEmail().compareToIgnoreCase(u2.getEmail());
    }

}
